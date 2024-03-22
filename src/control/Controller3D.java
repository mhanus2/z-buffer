package control;

import raster.Raster;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import solid.Vertex;
import transforms.*;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private ZBuffer zBuffer;
    private TriangleRasterizer triangleRasterizer;
    private Camera camera;
    private Mat4 proj;
    private double azimuth = 90;
    private double zenith = -15;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x101010));

        zBuffer = new ZBuffer(raster);
        triangleRasterizer = new TriangleRasterizer(zBuffer);

        camera = new Camera(
                new Vec3D(1, -5, 2),
                Math.toRadians(azimuth),
                Math.toRadians(zenith),
                1,
                true
        );

        proj = new Mat4PerspRH(
                Math.PI / 3,
                raster.getHeight() / (double) raster.getWidth(),
                0.5,
                50
        );
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void redraw() {
        panel.clear();

//        zBuffer.setPixelWithZTest(10, 10, 0.5, new Col(0xff0000));
//        zBuffer.setPixelWithZTest(10, 10, 0.7, new Col(0x00ff00));

        triangleRasterizer.rasterize(
                new Vertex(new Point3D(400, 0, 0.5), new Col(0xff0000), new Vec2D(0)),
                new Vertex(new Point3D(0, 300, 0.5), new Col(0x00ff00), new Vec2D(0)),
                new Vertex(new Point3D(799, 599, 0.5), new Col(0x0000ff), new Vec2D(0))
        );

        panel.repaint();
    }
}
