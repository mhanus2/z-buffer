package render;

import raster.TriangleRasterizer;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Mat4;

//todo - interface
public class Renderer {
    private TriangleRasterizer triangleRasterizer;
    private Mat4 view, proj;
    public Renderer(TriangleRasterizer triangleRasterizer) {
        this.triangleRasterizer = triangleRasterizer;
    }

    public void render(Solid solid) {
        Mat4 transformationMat = solid.getModel().mul(view).mul(proj);

        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case LINES:
                    // TODO: implementovat
                    break;
                case TRIANGLES:
                    int start = part.getStart();
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = start;
                        int indexB = start + 1;
                        int indexC = start + 2;
                        start += 3;

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);
                        clipTriangle(a, b, c, transformationMat);
                    }
                    break;
            }
        }
    }

    private void clipTriangle(Vertex a, Vertex b, Vertex c, Mat4 transformationMat) {
        Vertex newA = new Vertex(a.getPosition().mul(transformationMat), a.getColor(), a.getUv());
        Vertex newB = new Vertex(b.getPosition().mul(transformationMat), b.getColor(), b.getUv());
        Vertex newC = new Vertex(c.getPosition().mul(transformationMat), c.getColor(), c.getUv());

        if (newA.getPosition().getX() > newA.getPosition().getW() && newB.getPosition().getX() > newB.getPosition().getW() && newC.getPosition().getX() > newC.getPosition().getW()) return;
        if (newA.getPosition().getX() < -newA.getPosition().getW() && newB.getPosition().getX() < -newB.getPosition().getW() && newC.getPosition().getX() < -newC.getPosition().getW()) return;
        if (newA.getPosition().getY() > newA.getPosition().getW() && newB.getPosition().getY() > newB.getPosition().getW() && newC.getPosition().getY() > newC.getPosition().getW()) return;
        if (newA.getPosition().getY() < -newA.getPosition().getW() && newB.getPosition().getY() < -newB.getPosition().getW() && newC.getPosition().getY() < -newC.getPosition().getW()) return;
        if (newA.getPosition().getZ() > newA.getPosition().getW() && newB.getPosition().getZ() > newB.getPosition().getW() && newC.getPosition().getZ() > newC.getPosition().getW()) return;
        if (newA.getPosition().getZ() < 0 && newB.getPosition().getZ() < 0 && newC.getPosition().getZ() <0) return;

        // TODO: sežadit vrcholy podle z, aby aZ = max

        double zMin = 0;

        if(a.getPosition().getZ() < zMin)
            return;

        if(b.getPosition().getZ() < zMin) {
            double tv1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            Vertex v1 = a.mul(1 - tv1).add(b.mul(tv1));

            double tv2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex v2 = a.mul(1 - tv2).add(c.mul(tv2));

            triangleRasterizer.rasterize(a, v1, v2);
            return;
        }

        if(c.getPosition().getZ() < zMin) {
            // TODO: implementovat
        }

        triangleRasterizer.rasterize(a, b, c);
    }

    // TODO: metoda render pro seznam solidů


    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
