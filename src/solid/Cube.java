package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Cube extends Solid {
    public Cube() {
        Vertex A = new Vertex(new Point3D(-0.5, 0, -0.5), new Col(255,0,0), new Vec2D(0,0));
        Vertex B = new Vertex(new Point3D(0.5, 0, -0.5), new Col(0,255,0), new Vec2D(0.5,.6));
        Vertex C = new Vertex(new Point3D(0.5, 1, -0.5), new Col(0,0,255), new Vec2D(.7,.4));
        Vertex D = new Vertex(new Point3D(-0.5, 1, -0.5), new Col(255,255,255), new Vec2D(.2,.3));
        Vertex E = new Vertex(new Point3D(-0.5, 0, 0.5), new Col(255,255,255), new Vec2D(.4,.7));
        Vertex F = new Vertex(new Point3D(0.5, 0, 0.5), new Col(255,255,255), new Vec2D(.7,.4));
        Vertex G = new Vertex(new Point3D(0.5, 1, 0.5), new Col(255,255,255), new Vec2D(.6,.3));
        Vertex H = new Vertex(new Point3D(-0.5, 1, 0.5), new Col(255,255,255), new Vec2D(.8,.5));

        vertexBuffer.add(A);
        vertexBuffer.add(B);
        vertexBuffer.add(C);
        vertexBuffer.add(D);
        vertexBuffer.add(E);
        vertexBuffer.add(F);
        vertexBuffer.add(G);
        vertexBuffer.add(H);

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(5);

        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(5);

        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(7);

        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(7);

        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(7);

        indexBuffer.add(2);
        indexBuffer.add(6);
        indexBuffer.add(7);

        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(5);

        indexBuffer.add(2);
        indexBuffer.add(5);
        indexBuffer.add(6);

        indexBuffer.add(4);
        indexBuffer.add(5);
        indexBuffer.add(6);

        indexBuffer.add(4);
        indexBuffer.add(6);
        indexBuffer.add(7);

        partBuffer.add(new Part(TopologyType.TRIANGLES,0,12));
    }
}
