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

                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexC));
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

        // Fast clip
        if (newA.getPosition().getX() > newA.getPosition().getW() && newB.getPosition().getX() > newB.getPosition().getW() && newC.getPosition().getX() > newC.getPosition().getW()) return;
        if (newA.getPosition().getX() < -newA.getPosition().getW() && newB.getPosition().getX() < -newB.getPosition().getW() && newC.getPosition().getX() < -newC.getPosition().getW()) return;
        if (newA.getPosition().getY() > newA.getPosition().getW() && newB.getPosition().getY() > newB.getPosition().getW() && newC.getPosition().getY() > newC.getPosition().getW()) return;
        if (newA.getPosition().getY() < -newA.getPosition().getW() && newB.getPosition().getY() < -newB.getPosition().getW() && newC.getPosition().getY() < -newC.getPosition().getW()) return;
        if (newA.getPosition().getZ() > newA.getPosition().getW() && newB.getPosition().getZ() > newB.getPosition().getW() && newC.getPosition().getZ() > newC.getPosition().getW()) return;
        if (newA.getPosition().getZ() < 0 && newB.getPosition().getZ() < 0 && newC.getPosition().getZ() <0) return;

        // Sorting Z
        if (newA.getPosition().getZ() < newB.getPosition().getZ()) {
            Vertex temp = newA;
            newA = newB;
            newB = temp;
        }
        if (newB.getPosition().getZ() < newC.getPosition().getZ()) {
            Vertex temp = newB;
            newB = newC;
            newC = temp;
        }
        if (newA.getPosition().getZ() < newB.getPosition().getZ()) {
            Vertex temp = newA;
            newA = newB;
            newB = temp;
        }

        double zMin = 0;

        if(newA.getPosition().getZ() < zMin)
            return;

        if(newB.getPosition().getZ() < zMin) {
            double tv1 = (zMin - newA.getPosition().getZ()) / (newB.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v1 = newA.mul(1 - tv1).add(newB.mul(tv1));

            double tv2 = (zMin - newA.getPosition().getZ()) / (newC.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v2 = newA.mul(1 - tv2).add(newC.mul(tv2));

            triangleRasterizer.rasterize(newA, v1, v2);
            return;
        }

        if(newC.getPosition().getZ() < zMin) {
            double tv1 = -newA.getPosition().getZ() / (newC.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v1 = newA.mul(1 - tv1).add(newC.mul(tv1));

            double tv2 = -newB.getPosition().getZ() / (newC.getPosition().getZ() - newB.getPosition().getZ());
            Vertex v2 = newB.mul(1 - tv2).add(newC.mul(tv1));

            triangleRasterizer.rasterize(newA, newB, v2);
            triangleRasterizer.rasterize(newA, v1, v2);
            return;
        }

        triangleRasterizer.rasterize(newA, newB, newC);
    }

    // TODO: metoda render pro seznam solidů


    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
