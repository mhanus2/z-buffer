package control;

import raster.Raster;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import render.Renderer;
import solid.Pyramid;
import solid.Solid;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private ZBuffer zBuffer;
    private TriangleRasterizer triangleRasterizer;
    private Renderer renderer;

    private Camera camera;
    private Mat4 proj;
    private double azimuth = 90;
    private double zenith = 0;
    private Solid pyramid;
    int firstX;
    int firstY;
    private final double step = 0.1;

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
        renderer = new Renderer(triangleRasterizer);

        camera = new Camera(
                new Vec3D(0, -2, 0.3),
                Math.toRadians(azimuth),
                Math.toRadians(zenith),
                1,
                true
        );

        proj = new Mat4PerspRH(
                Math.PI / 4,
                raster.getHeight() / (double) raster.getWidth(),
                0.1,
                20
        );

        pyramid = new Pyramid();
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

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    firstX = e.getX();
                    firstY = e.getY();
                }
                redraw();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    panel.clear();

                    int dy = e.getY() - firstY;
                    zenith -= (double) (180 * dy) / panel.getHeight();

                    if (zenith > 90) zenith = 90;
                    if (zenith < -90) zenith = -90;

                    int dx = e.getX() - firstX;

                    azimuth -= (double) (180 * dx) / panel.getWidth();
                    azimuth = azimuth % 360;

                    firstX = e.getX();
                    firstY = e.getY();
                }
                redraw();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_O:
                        double zn = 0.1;
                        double zf = 20;
                        int w = panel.getWidth() / 150;
                        int h = panel.getHeight() / 150;

                        proj = new Mat4OrthoRH(w, h, zn, zf);
                        break;
                    case KeyEvent.VK_P:
                        proj = new Mat4PerspRH(
                                Math.PI / 4,
                                panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(),
                                0.1,
                                20
                        );
                        break;
                    case KeyEvent.VK_W:
                        camera = camera.forward(step);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(step);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(step);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(step);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera = camera.up(step);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera = camera.down(step);
                        break;
                }
                redraw();
            }
        });
    }

    private void redraw() {
        panel.clear();
        zBuffer.reset();

        camera = camera.withAzimuth(Math.toRadians(azimuth)).withZenith(Math.toRadians(zenith));

        renderer.setView(camera.getViewMatrix());
        renderer.setProj(proj);

//        Mat4 cubeModelMat = new Mat4RotY(-0.5).mul(new Mat4Scale(0.5)).mul(new Mat4Transl(0.75, 1, 0));
//        pyramid.setModel(cubeModelMat);

        renderer.render(pyramid);

        panel.repaint();
    }
}
