package render;

import raster.LineRasterizer;
import raster.Rasterizer;
import raster.TriangleRasterizer;
import shader.Shader;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Mat4;

import java.util.List;

public class SolidRenderer implements Renderer {
    private Rasterizer rasterizer;
    private final LineRasterizer lineRasterizer;
    private Mat4 view, proj, transformation;

    public SolidRenderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.rasterizer = triangleRasterizer;
        this.lineRasterizer = lineRasterizer;
    }

    public void render(Solid solid) {
        transformation = solid.getModel().mul(view).mul(proj);

        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case LINES:
                    int startLine = part.getStart();
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = startLine;
                        int indexB = startLine + 1;
                        startLine += 2;

                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));
                        clipLine(a, b, solid.getShader());
                    }
                    break;
                case TRIANGLES:
                    int start = part.getStart();
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = start;
                        int indexB = start + 1;
                        int indexC = start + 2;
                        start += 3;

                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexC));
                        clipTriangle(a, b, c, solid.getShader());
                    }
                    break;
            }
        }
    }

    private void clipLine(Vertex a, Vertex b, Shader shader) {
        a = new Vertex(a.getPosition().mul(transformation), a.getColor(), a.getUv());
        b = new Vertex(b.getPosition().mul(transformation), b.getColor(), b.getUv());

        // Fast clip
        if (a.getPosition().getX() > a.getPosition().getW() && b.getPosition().getX() > b.getPosition().getW())
            return;
        if (a.getPosition().getX() < -a.getPosition().getW() && b.getPosition().getX() < -b.getPosition().getW())
            return;
        if (a.getPosition().getY() > a.getPosition().getW() && b.getPosition().getY() > b.getPosition().getW())
            return;
        if (a.getPosition().getY() < -a.getPosition().getW() && b.getPosition().getY() < -b.getPosition().getW())
            return;
        if (a.getPosition().getZ() > a.getPosition().getW() && b.getPosition().getZ() > b.getPosition().getW())
            return;
        if (a.getPosition().getZ() < 0 && b.getPosition().getZ() < 0) return;

        // Sorting Z
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex tmp = a;
            a = b;
            b = tmp;
        }

        double zMin = 0;

        if (a.getPosition().getZ() < zMin)
            return;

        if (b.getPosition().getZ() < zMin) {
            double t = (zMin - b.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            Vertex v = a.mul(1 - t).add(b.mul(t));
            lineRasterizer.rasterize(a, v, shader);
            return;
        }
        lineRasterizer.rasterize(a, b, shader);
    }

    private void clipTriangle(Vertex a, Vertex b, Vertex c, Shader shader) {
        a = new Vertex(a.getPosition().mul(transformation), a.getColor(), a.getUv());
        b = new Vertex(b.getPosition().mul(transformation), b.getColor(), b.getUv());
        c = new Vertex(c.getPosition().mul(transformation), c.getColor(), c.getUv());

        // Fast clip
        if (a.getPosition().getX() > a.getPosition().getW() && b.getPosition().getX() > b.getPosition().getW() && c.getPosition().getX() > c.getPosition().getW())
            return;
        if (a.getPosition().getX() < -a.getPosition().getW() && b.getPosition().getX() < -b.getPosition().getW() && c.getPosition().getX() < -c.getPosition().getW())
            return;
        if (a.getPosition().getY() > a.getPosition().getW() && b.getPosition().getY() > b.getPosition().getW() && c.getPosition().getY() > c.getPosition().getW())
            return;
        if (a.getPosition().getY() < -a.getPosition().getW() && b.getPosition().getY() < -b.getPosition().getW() && c.getPosition().getY() < -c.getPosition().getW())
            return;
        if (a.getPosition().getZ() > a.getPosition().getW() && b.getPosition().getZ() > b.getPosition().getW() && c.getPosition().getZ() > c.getPosition().getW())
            return;
        if (a.getPosition().getZ() < 0 && b.getPosition().getZ() < 0 && c.getPosition().getZ() < 0) return;

        // Sorting Z
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getPosition().getZ() < c.getPosition().getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        double zMin = 0;

        if (a.getPosition().getZ() < zMin)
            return;

        if (b.getPosition().getZ() < zMin) {
            double t1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            Vertex v1 = a.mul(1 - t1).add(b.mul(t1));

            double t2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex v2 = a.mul(1 - t2).add(c.mul(t2));

            rasterizer.rasterize(a, v1, v2, shader);
            return;
        }

        if (c.getPosition().getZ() < zMin) {
            double t1 = -a.getPosition().getZ() / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex v1 = a.mul(1 - t1).add(c.mul(t1));

            double t2 = -b.getPosition().getZ() / (c.getPosition().getZ() - b.getPosition().getZ());
            Vertex v2 = b.mul(1 - t2).add(c.mul(t1));

            rasterizer.rasterize(a, b, v2, shader);
            rasterizer.rasterize(a, v1, v2, shader);
            return;
        }

        rasterizer.rasterize(a, b, c, shader);
    }

    public void render(List<Solid> scene) {
        for (Solid s : scene)
            render(s);
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setRasterizer(Rasterizer rasterizer) {
        this.rasterizer = rasterizer;
    }
}
