package solid;

import transforms.Col;
import transforms.Point3D;

public class Vertex {
    private final Point3D position;
    private final Col color;

    // TODO: souřadnice do textury, normála atd.

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }
}
