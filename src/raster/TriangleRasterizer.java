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
        Optional<Vertex> aDehomogenized = a.dehomog();
        Optional<Vertex> bDehomogenized = b.dehomog();
        Optional<Vertex> cDehomogenized = c.dehomog();
        if (aDehomogenized.isEmpty() || bDehomogenized.isEmpty() || cDehomogenized.isEmpty()) return;
        a = aDehomogenized.get();
        b = bDehomogenized.get();
        c = cDehomogenized.get();

        // Transformation to window
        Vec3D vec3D1 = transformToWindow(a.getPosition());
        a = new Vertex(new Point3D(vec3D1), a.getColor(), a.getUv());
        Vec3D vec3D2 = transformToWindow(b.getPosition());
        b = new Vertex(new Point3D(vec3D2), b.getColor(), b.getUv());
        Vec3D vec3D3 = transformToWindow(c.getPosition());
        c = new Vertex(new Point3D(vec3D3), c.getColor(), c.getUv());

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

        a = a.mul(1 / a.getPosition().getW());
        b = b.mul(1 / b.getPosition().getW());
        c = c.mul(1 / c.getPosition().getW());

        int aY = (int) Math.round(a.getPosition().getY());
        int bY = (int) Math.round(b.getPosition().getY());
        int cY = (int) Math.round(c.getPosition().getY());

        int yAStart = Math.max(0, (int) a.getPosition().getY() + 1);
        double yBEnd = Math.min(zBuffer.getHeight() - 1, b.getPosition().getY());

        for (int y = yAStart; y <= yBEnd; y++) {
            double t1 = (y - aY) / (double) (bY - aY);
            Vertex vAB = a.mul(1 - t1).add(b.mul(t1));

            double t2 = (y - aY) / (double) (cY - aY);
            Vertex vAC = a.mul(1 - t2).add(c.mul(t2));

            if (vAB.getPosition().getX() > vAC.getPosition().getX()) {
                Vertex tmp = vAB;
                vAB = vAC;
                vAC = tmp;
            }

            int xStart = Math.max(0, (int) vAB.getPosition().getX() + 1);
            double xEnd = Math.min(zBuffer.getWidth() - 1, vAC.getPosition().getX());

            for (int x = xStart; x <= xEnd; x++) {
                double t = (x - vAB.getPosition().getX()) / (vAC.getPosition().getX() - vAB.getPosition().getX());
                Vertex pixel = vAB.mul(1 - t).add(vAC.mul(t));
                zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), shader.getColor(pixel));
            }
        }

        int yBStart = Math.max(0, (int) b.getPosition().getY() + 1);
        double yCEnd = Math.min(zBuffer.getHeight() - 1, c.getPosition().getY());

        for (int y = yBStart; y <= yCEnd; y++) {
            double tBC = (y - bY) / (double) (cY - bY);
            Vertex vBC = b.mul(1 - tBC).add(c.mul(tBC));

            double tAC = (y - aY) / (double) (cY - aY);
            Vertex vAC = a.mul(1 - tAC).add(c.mul(tAC));

            if (vBC.getPosition().getX() > vAC.getPosition().getX()) {
                Vertex tmp = vBC;
                vBC = vAC;
                vAC = tmp;
            }

            int xBCStart = Math.max(0, (int) vBC.getPosition().getX() + 1);
            double xACEnd = Math.min(zBuffer.getWidth() - 1, vAC.getPosition().getX());

            for (int x = xBCStart; x <= xACEnd; x++) {
                double t = (x - vBC.getPosition().getX()) / (vAC.getPosition().getX() - vBC.getPosition().getX());
                Vertex pixel = vBC.mul(1 - t).add(vAC.mul(t));
                zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), shader.getColor(pixel));
            }
        }
    }
}
