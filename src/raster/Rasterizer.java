package raster;

import solid.Vertex;
import transforms.Point3D;
import transforms.Vec3D;

public abstract class Rasterizer {
    protected final ZBuffer zBuffer;

    public Rasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public abstract void rasterize(Vertex a, Vertex b, Vertex c);

    protected Vec3D transformToWindow(Point3D vec) {
        return new Vec3D(vec)
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((zBuffer.getWidth() - 1)  / 2., (zBuffer.getHeight() - 1) / 2., 1));
    }
}
