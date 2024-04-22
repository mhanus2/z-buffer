package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

import java.util.Optional;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;
    private final Vec2D uv;

    // TODO: souřadnice do textury atd.

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
        this.uv = new Vec2D();
    }

    public Vertex(Point3D position, Col color, Vec2D uv) {
        this.position = position;
        this.color = color;
        this.uv = uv;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }
    public Vec2D getUv() {
        return uv;
    }

    @Override
    public Vertex mul(double k) {
        return new Vertex(position.mul(k), color.mul(k), uv.mul(k));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(
                position.add(v.getPosition()),
                color.add(v.getColor()),
                uv.add(v.getUv())
        );

    }

    public Optional<Vertex> dehomog() {
        Optional<Vec3D> optional = position.dehomog();
        return optional.map(vec3D -> new Vertex(new Point3D(vec3D), color, uv));
    }

}
