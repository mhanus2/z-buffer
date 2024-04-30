package raster;

import shader.Shader;
import solid.Vertex;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class LineRasterizer extends Rasterizer {

    public LineRasterizer(ZBuffer zBuffer) {
        super(zBuffer);
    }

    @Override
    public void rasterize(Vertex a, Vertex b, Vertex c, Shader shader) {
        rasterize(a, b, shader);
        rasterize(b, c, shader);
        rasterize(a, c, shader);
    }

    public void rasterize(Vertex a, Vertex b, Shader shader) {
        // Dehomogenization
        Optional<Vertex> aDehomogenized = a.dehomog();
        Optional<Vertex> bDehomogenized = b.dehomog();
        if (aDehomogenized.isEmpty() || bDehomogenized.isEmpty()) return;
        a = aDehomogenized.get();
        b = bDehomogenized.get();

        // Transformation to window
        Vec3D vector1 = transformToWindow(a.getPosition());
        a = new Vertex(new Point3D(vector1), a.getColor());
        Vec3D vector2 = transformToWindow(b.getPosition());
        b = new Vertex(new Point3D(vector2), b.getColor());

        // Sorting y
        if (a.getPosition().getY() > b.getPosition().getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        double dx = a.getPosition().getX() - b.getPosition().getX();
        double dy = a.getPosition().getY() - b.getPosition().getY();
        double dz = a.getPosition().getZ() - b.getPosition().getZ();

        double steps = Math.max(Math.abs(dx), Math.abs(dy));
        steps = Math.max(steps, Math.abs(dz));

        double xIncrement = dx / steps;
        double yIncrement = dy / steps;
        double zIncrement = dz / steps;

        double x = a.getPosition().getX();
        double y = a.getPosition().getY();
        double z = a.getPosition().getZ();

        for (int i = 0; i <= steps; i++) {
            int xTmp = (int) Math.round(x);
            int yTmp = (int) Math.round(y);

            zBuffer.setPixelWithZTest(xTmp, yTmp, z, shader.getColor(a));

            x -= xIncrement;
            y -= yIncrement;
            z -= zIncrement;
        }
    }
}
