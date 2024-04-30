package solid;

import transforms.Col;
import transforms.Point3D;

public class Axes extends Solid {
    public Axes() {
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0.2, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0.2), new Col(0, 0, 255)));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(3);

        partBuffer.add(new Part(TopologyType.LINES, 0, 3));
    }
}