package raster;

import solid.Vertex;
import transforms.Col;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        // TODO: seřadit vrcholy pod y od min

        // TODO: odebrat, jen pro debug
        ((ImageBuffer) zBuffer.getImageBuffer()).getImg().getGraphics().drawLine(
                (int) a.getPosition().getX(), (int) a.getPosition().getY(),
                (int) b.getPosition().getX(), (int) b.getPosition().getY()
        );
        ((ImageBuffer) zBuffer.getImageBuffer()).getImg().getGraphics().drawLine(
                (int) a.getPosition().getX(), (int) a.getPosition().getY(),
                (int) c.getPosition().getX(), (int) c.getPosition().getY()
        );
        ((ImageBuffer) zBuffer.getImageBuffer()).getImg().getGraphics().drawLine(
                (int) b.getPosition().getX(), (int) b.getPosition().getY(),
                (int) c.getPosition().getX(), (int) c.getPosition().getY()
        );

        int xA = (int) a.getPosition().getX();
        int yA = (int) a.getPosition().getY();
        int yB = (int) b.getPosition().getY();
        int xB = (int) b.getPosition().getX();
        int yC = (int) c.getPosition().getY();
        int xC = (int) c.getPosition().getX();

        // Cyklus od A do B (první část)
        for (int y = yA; y <= (int) b.getPosition().getY(); y++) {
            // V1
            double t1 = (y - yA) / (double) (yB - yA);
            int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
            //double z1 = (1 - t1) * zA + t1 * zB;
            Col col1 = a.getColor().mul(1 - t1).add(b.getColor().mul(t1));

            // V2
            double t2 = (y - yA) / (double) (yC - yA);
            int x2 = (int) Math.round((1 - t2) * xA + t2 * xC);
            //double z2 = (1 - t2) * zA + t2 * zC;
            Col col2 = a.getColor().mul(1 - t2).add(c.getColor().mul(t2));

            // TODO: kontrola, jestli x1 < x2
            for (int x = x1; x <= x2; x++) {
                double t3 = (x - x1) / (double)(x2 - x1);
                //double z = (1 - t3) * z1 + t3 * z2;
                Col col = col1.mul(1 - t3).add(col2.mul(t3));

                zBuffer.setPixelWithZTest(x, y, 0.5, col);
            }
        }

        // TODO: Cyklus od B do C (druhá část)

    }
}
