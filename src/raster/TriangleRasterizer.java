package raster;

import solid.Vertex;
import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        // Dehomogenizace
        Optional<Vertex> newA = a.dehomog();
        Optional<Vertex> newB = a.dehomog();
        Optional<Vertex> newC = a.dehomog();

        if (newA.isEmpty() || newB.isEmpty() || newC.isEmpty()) return;

        Vertex nA = newA.get();
        Vertex nB = newB.get();
        Vertex nC = newC.get();

        // Transformace do okna
        Vec3D vec3D1 = transformToWindow(nA.getPosition());
        Vertex aa = new Vertex(new Point3D(vec3D1), nA.getColor());
        Vec3D vec3D2 = transformToWindow(nB.getPosition());
        Vertex bb = new Vertex(new Point3D(vec3D2), nB.getColor());
        Vec3D vec3D3 = transformToWindow(nC.getPosition());
        Vertex cc = new Vertex(new Point3D(vec3D3), nC.getColor());

        // Seřazení podle y


        int aX = (int) Math.round(a.getPosition().getX());
        int aY = (int) Math.round(a.getPosition().getY());
        double aZ = a.getPosition().getZ();

        int bX = (int) Math.round(b.getPosition().getX());
        int bY = (int) Math.round(b.getPosition().getY());
        double bZ = b.getPosition().getZ();

        int cX = (int) Math.round(c.getPosition().getX());
        int cY = (int) Math.round(c.getPosition().getY());
        double cZ = c.getPosition().getZ();

        for (int y = aY; y <= bY; y++) {
            double tAB = (y - aY) / (double) (bY - aY);
            int xAB = (int) Math.round((1 - tAB) * aX + tAB * bX);
            // TODO: zAB
            Vertex vAB = a.mul(1 - tAB).add(b.mul(tAB));
            // Vertex vAB = lerp.lerp(a, b, tAB);

            double tAC = (y - aY) / (double) (cY - aY);
            int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
            Vertex vAC = a.mul(1 - tAC).add(c.mul(tAC));
            // TODO: zAC

//            int xAB = (int) AB.getPosition().getX();
//            int xAC = (int) (AC.getPosition().getX() + 1);

            // TODO: xAB musí být menší než xAC
            for (int x = xAB; x <= xAC; x++) {
                double t = (x - xAB) / (double) (xAC - xAB);
                Vertex pixel = vAB.mul(1 - t).add(vAC.mul(t));

                // TODO: nový interpolační koef. -> počítám z xAB a xAC
                // TODO: spočítám z
                zBuffer.setPixelWithZTest(x, y, 0.5, pixel.getColor());
            }
        }

        for (int y = bY; y <= cY; y++) {
            double tBC = (y - bY) / (double) (cY - bY);
            int xBC = (int) Math.round((1 - tBC) * bX + tBC * cX);
            // TODO: zAB
            Vertex vBC = b.mul(1 - tBC).add(c.mul(tBC));
            // Vertex vAB = lerp.lerp(a, b, tAB);

            double tAC = (y - aY) / (double) (cY - aY);
            int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
            Vertex vAC = a.mul(1 - tAC).add(c.mul(tAC));
            // TODO: zAC

            // TODO: xAB musí být menší než xAC
            for (int x = xBC; x <= xAC; x++) {
                double t = (x - xBC) / (double) (xAC - xBC);
                Vertex pixel = vBC.mul(1 - t).add(vAC.mul(t));

                // TODO: nový interpolační koef. -> počítám z xAB a xAC
                // TODO: spočítám z
                zBuffer.setPixelWithZTest(x, y, 0.5, pixel.getColor());
            }
        }
    }

    private Vec3D transformToWindow(Point3D vec) {
        return new Vec3D(vec)
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D(zBuffer.getWidth() / 2f, zBuffer.getHeight() / 2f, 1));
    }
}
