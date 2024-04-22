package solid;

import transforms.Col;
import transforms.Point3D;


public class Pyramid extends Solid {
    public Pyramid() {
        Vertex A = new Vertex(new Point3D(0,0,0), new Col(255,0,0));
        Vertex B = new Vertex(new Point3D(1,0,0), new Col(0,255,0));
        Vertex C = new Vertex(new Point3D(0.5,Math.sqrt(3)/2,0), new Col(0,0,255));
        Vertex D = new Vertex(new Point3D(0.5,Math.sqrt(3)/6,Math.sqrt(2./3)), new Col(255,255,255));

        vertexBuffer.add(A);
        vertexBuffer.add(B);
        vertexBuffer.add(C);
        vertexBuffer.add(D);

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(3);

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);

        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);

        partBuffer.add(new Part(TopologyType.TRIANGLES,0,4));
    }
 }
