package raster;

import shader.Shader;
import solid.Vertex;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class TriangleRasterizer extends Rasterizer {

    public TriangleRasterizer(ZBuffer zBuffer) {
        super(zBuffer);
    }

    public void rasterize(Vertex a, Vertex b, Vertex c, Shader shader) {
        // Dehomogenization
        Optional<Vertex> newA = a.dehomog();
        Optional<Vertex> newB = b.dehomog();
        Optional<Vertex> newC = c.dehomog();

        if (newA.isEmpty() || newB.isEmpty() || newC.isEmpty()) return;

        Vertex nA = newA.get();
        Vertex nB = newB.get();
        Vertex nC = newC.get();

        // Transformation to window
        Vec3D vec3D1 = transformToWindow(nA.getPosition());
        a = new Vertex(new Point3D(vec3D1), nA.getColor(), nA.getUv());
        Vec3D vec3D2 = transformToWindow(nB.getPosition());
        b = new Vertex(new Point3D(vec3D2), nB.getColor(), nB.getUv());
        Vec3D vec3D3 = transformToWindow(nC.getPosition());
        c = new Vertex(new Point3D(vec3D3), nC.getColor(), nC.getUv());

        // Sorting y
        if (a.getPosition().getY() > b.getPosition().getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getPosition().getY() > c.getPosition().getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getPosition().getY() > b.getPosition().getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        a = a.mul(1/a.getPosition().getW());
        b = b.mul(1/b.getPosition().getW());
        c = c.mul(1/c.getPosition().getW());

        int aX = (int) Math.round(a.getPosition().getX());
        int aY = (int) Math.round(a.getPosition().getY());

        int bX = (int) Math.round(b.getPosition().getX());
        int bY = (int) Math.round(b.getPosition().getY());

        int cX = (int) Math.round(c.getPosition().getX());
        int cY = (int) Math.round(c.getPosition().getY());

        int yAStart = Math.max(0, (int) a.getPosition().getY() +1);
        double yBEnd = Math.min(zBuffer.getHeight() - 1, b.getPosition().getY());

        for (int y = yAStart; y <= yBEnd; y++) {
            double tAB = (y - aY) / (double) (bY - aY);
            int xAB = (int) Math.round((1 - tAB) * aX + tAB * bX);
            Vertex vAB = a.mul(1 - tAB).add(b.mul(tAB));

            double tAC = (y - aY) / (double) (cY - aY);
            int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
            Vertex vAC = a.mul(1 - tAC).add(c.mul(tAC));

            if (xAB > xAC) {
                int tmp = xAB;
                xAB = xAC;
                xAC = tmp;

                Vertex tmp2 = vAB;
                vAB = vAC;
                vAC = tmp2;
            }

            int xABStart = Math.max(0, (int) vAB.getPosition().getX() + 1);
            double xACEnd = Math.min(zBuffer.getWidth() - 1, vAC.getPosition().getX());

            for (int x = xABStart; x <= xACEnd; x++) {
                double t = (x - xAB) / (double) (xAC - xAB);
                Vertex pixel = vAB.mul(1 - t).add(vAC.mul(t));
                zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), shader.getColor(pixel));
            }
        }

        int yBStart = Math.max(0, (int) b.getPosition().getY() +1);
        double yCEnd = Math.min(zBuffer.getHeight() - 1, c.getPosition().getY());

        for (int y = yBStart; y <= yCEnd; y++) {
            double tBC = (y - bY) / (double) (cY - bY);
            int xBC = (int) Math.round((1 - tBC) * bX + tBC * cX);
            Vertex vBC = b.mul(1 - tBC).add(c.mul(tBC));

            double tAC = (y - aY) / (double) (cY - aY);
            int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
            Vertex vAC = a.mul(1 - tAC).add(c.mul(tAC));

            if (xBC > xAC) {
                int tmp = xBC;
                xBC = xAC;
                xAC = tmp;

                Vertex tmp2 = vBC;
                vBC = vAC;
                vAC = tmp2;
            }

            int xBCStart = Math.max(0, (int) vBC.getPosition().getX() + 1);
            double xACEnd = Math.min(zBuffer.getWidth() - 1, vAC.getPosition().getX());

            for (int x = xBCStart; x <= xACEnd; x++) {
                double t = (x - xBC) / (double) (xAC - xBC);
                Vertex pixel = vBC.mul(1 - t).add(vAC.mul(t));
                zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), shader.getColor(pixel));
            }
        }
    }
}
