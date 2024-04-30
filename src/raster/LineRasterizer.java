package raster;

import shader.Shader;
import solid.Vertex;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class LineRasterizer extends Rasterizer{

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
        Optional<Vertex> newA = a.dehomog();
        Optional<Vertex> newB = b.dehomog();

        if (newA.isEmpty() || newB.isEmpty()) return;

        Vertex nA = newA.get();
        Vertex nB = newB.get();

        // Transformation to window
        Vec3D vec3D1 = transformToWindow(nA.getPosition());
        a = new Vertex(new Point3D(vec3D1), nA.getColor());
        Vec3D vec3D2 = transformToWindow(nB.getPosition());
        b = new Vertex(new Point3D(vec3D2), nB.getColor());

        // Sorting y
        if (a.getPosition().getY() > b.getPosition().getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        double dxAB = a.getPosition().getX() - b.getPosition().getX();
        double dyAB = a.getPosition().getY() - b.getPosition().getY();
        double dzAB = a.getPosition().getZ() - b.getPosition().getZ();

        // Určíme, která osa je hlavní (ta s největším rozdílem)
        double steps = Math.max(Math.abs(dxAB), Math.abs(dyAB));
        steps = Math.max(steps, Math.abs(dzAB));

        // Vypočítáme přírůstky pro každou osu
        double xIncrement = dxAB / steps;
        double yIncrement = dyAB / steps;
        double zIncrement = dzAB / steps;

        // Inicializujeme proměnné pro průchod přímkou
        double x = a.getPosition().getX();
        double y = a.getPosition().getY();
        double z = a.getPosition().getZ();

        // Projdeme přímkou a vykreslíme pixely, pokud jsou blíže než stávající pixel v z-bufferu
        for (int i = 0; i <= steps; i++) {
            int xi = (int) Math.round(x);
            int yi = (int) Math.round(y);

            // Zkontrolujeme z-buffer
            zBuffer.setPixelWithZTest(xi, yi, z, shader.getColor(a));

            // Posuneme se na další bod na přímce
            x -= xIncrement;
            y -= yIncrement;
            z -= zIncrement;
        }
    }
}
