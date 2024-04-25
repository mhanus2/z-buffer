package render;

import raster.LineRasterizer;
import raster.Rasterizer;
import raster.TriangleRasterizer;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Mat4;

import java.util.List;

//todo - interface
public class Renderer {
    private Rasterizer rasterizer;
    private final LineRasterizer lineRasterizer;
    private Mat4 view, proj;

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.rasterizer = triangleRasterizer;
        this.lineRasterizer = lineRasterizer;
    }

    public void render(Solid solid) {
        Mat4 transformationMat = solid.getModel().mul(view).mul(proj);

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
                        clipLine(a, b, transformationMat);
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
                        clipTriangle(a, b, c, transformationMat);
                    }
                    break;
            }
        }
    }

    private void clipLine(Vertex a, Vertex b, Mat4 transformationMat) {
        //Trasformace až do projekce
        Vertex nA = new Vertex(a.getPosition().mul(transformationMat), a.getColor(), a.getUv());
        Vertex nB = new Vertex(b.getPosition().mul(transformationMat), b.getColor(), b.getUv());

        //fast clip
        if (nA.getPosition().getX() > nA.getPosition().getW() && nB.getPosition().getX() > nB.getPosition().getW())
            return;
        if (nA.getPosition().getX() < -nA.getPosition().getW() && nB.getPosition().getX() < -nB.getPosition().getW())
            return;
        if (nA.getPosition().getY() > nA.getPosition().getW() && nB.getPosition().getY() > nB.getPosition().getW())
            return;
        if (nA.getPosition().getY() < -nA.getPosition().getW() && nB.getPosition().getY() < -nB.getPosition().getW())
            return;

        if (nA.getPosition().getZ() > nA.getPosition().getW() && nB.getPosition().getZ() > nB.getPosition().getW())
            return;
        if (nA.getPosition().getZ() < 0 && nB.getPosition().getZ() < 0) return;


        //seřazení vrcholy podle z, aby aZ = max
        if (nA.getPosition().getZ() < nB.getPosition().getZ()) {
            Vertex tmp = nA;
            nA = nB;
            nB = tmp;
        }

        double zMin = 0;

        if (nA.getPosition().getZ() < zMin)
            return;

        if (nB.getPosition().getZ() < zMin) {
            double tv1 = (zMin - nB.getPosition().getZ()) / (nB.getPosition().getZ() - nA.getPosition().getZ());
            Vertex v1 = nA.mul(1 - tv1).add(nB.mul(tv1));

            lineRasterizer.rasterize(nA, v1);
            return;
        }
        lineRasterizer.rasterize(nA, nB);
    }

    private void clipTriangle(Vertex a, Vertex b, Vertex c, Mat4 transformationMat) {
        Vertex newA = new Vertex(a.getPosition().mul(transformationMat), a.getColor(), a.getUv());
        Vertex newB = new Vertex(b.getPosition().mul(transformationMat), b.getColor(), b.getUv());
        Vertex newC = new Vertex(c.getPosition().mul(transformationMat), c.getColor(), c.getUv());

        // Fast clip
        if (newA.getPosition().getX() > newA.getPosition().getW() && newB.getPosition().getX() > newB.getPosition().getW() && newC.getPosition().getX() > newC.getPosition().getW())
            return;
        if (newA.getPosition().getX() < -newA.getPosition().getW() && newB.getPosition().getX() < -newB.getPosition().getW() && newC.getPosition().getX() < -newC.getPosition().getW())
            return;
        if (newA.getPosition().getY() > newA.getPosition().getW() && newB.getPosition().getY() > newB.getPosition().getW() && newC.getPosition().getY() > newC.getPosition().getW())
            return;
        if (newA.getPosition().getY() < -newA.getPosition().getW() && newB.getPosition().getY() < -newB.getPosition().getW() && newC.getPosition().getY() < -newC.getPosition().getW())
            return;
        if (newA.getPosition().getZ() > newA.getPosition().getW() && newB.getPosition().getZ() > newB.getPosition().getW() && newC.getPosition().getZ() > newC.getPosition().getW())
            return;
        if (newA.getPosition().getZ() < 0 && newB.getPosition().getZ() < 0 && newC.getPosition().getZ() < 0) return;

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

        if (newA.getPosition().getZ() < zMin)
            return;

        if (newB.getPosition().getZ() < zMin) {
            double tv1 = (zMin - newA.getPosition().getZ()) / (newB.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v1 = newA.mul(1 - tv1).add(newB.mul(tv1));

            double tv2 = (zMin - newA.getPosition().getZ()) / (newC.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v2 = newA.mul(1 - tv2).add(newC.mul(tv2));

            rasterizer.rasterize(newA, v1, v2);
            return;
        }

        if (newC.getPosition().getZ() < zMin) {
            double tv1 = -newA.getPosition().getZ() / (newC.getPosition().getZ() - newA.getPosition().getZ());
            Vertex v1 = newA.mul(1 - tv1).add(newC.mul(tv1));

            double tv2 = -newB.getPosition().getZ() / (newC.getPosition().getZ() - newB.getPosition().getZ());
            Vertex v2 = newB.mul(1 - tv2).add(newC.mul(tv1));

            rasterizer.rasterize(newA, newB, v2);
            rasterizer.rasterize(newA, v1, v2);
            return;
        }

        rasterizer.rasterize(newA, newB, newC);
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
