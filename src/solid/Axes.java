package solid;

import transforms.Col;
import transforms.Point3D;

public class Axes extends Solid {
    public Axes() {
        // vb
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(255,0,0))); // v0
        //Zelená je x
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0, 0), new Col(255,0,0))); // v1
        //Modrá je Y
        vertexBuffer.add(new Vertex(new Point3D(0, 0.2, 0), new Col(0,255,0))); // v2
        //Žlutá je Z
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0.2), new Col(0,0,255))); // v3

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(3);

        partBuffer.add(new Part(TopologyType.LINES, 0, 3));
    }
}