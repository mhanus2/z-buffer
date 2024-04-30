package control;

import raster.LineRasterizer;
import raster.Raster;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import render.SolidRenderer;
import shader.Shader;
import shader.ShaderTexture;
import solid.Axes;
import solid.Cube;
import solid.Pyramid;
import solid.Solid;
import transforms.*;
import view.Panel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller3D implements Controller {
    private final Panel panel;
    private ZBuffer zBuffer;
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;
    private SolidRenderer renderer;

    private Camera camera;
    private Mat4 proj;
    private double azimuth = 90;
    private double zenith = 0;
    int firstX;
    int firstY;
    private final double step = 0.1;
    ArrayList<Solid> solids = new ArrayList<>();
    int activeSolidIndex = 0;
    Solid activeSolid;
    private BufferedImage texture;
    Solid axes;
    Shader shaderTexture;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x101010));

        try {
            texture = ImageIO.read(new File("./res/texture-artwork.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        zBuffer = new ZBuffer(raster);
        triangleRasterizer = new TriangleRasterizer(zBuffer);
        lineRasterizer = new LineRasterizer(zBuffer);
        shaderTexture =  new ShaderTexture(texture);

        renderer = new SolidRenderer(lineRasterizer, triangleRasterizer);

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

        Solid pyramid = new Pyramid();
        pyramid.setTransform(new Mat4RotZ(45).mul(new Mat4Transl(0,-0.5,0)));
        Solid cube = new Cube();
        cube.setShader(shaderTexture);
        cube.setModel(new Mat4Transl(-1,0,0));
        axes = new Axes();
        solids.add(pyramid);
        solids.add(cube);
        activeSolid = solids.get(activeSolidIndex);
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
                    case KeyEvent.VK_N:
                        if (activeSolidIndex == solids.size() - 1) {
                            activeSolidIndex = 0;
                        } else {
                            activeSolidIndex++;
                        }
                        activeSolid = solids.get(activeSolidIndex);
                        break;
                    case KeyEvent.VK_E:
                        activeSolid.setTransform(new Mat4RotY(-0.5));
                        break;
                    case KeyEvent.VK_R:
                        activeSolid.setTransform(new Mat4RotY(0.5));
                        break;
                    case KeyEvent.VK_SHIFT:
                        activeSolid.setTransform(new Mat4Scale(1.5));
                        break;
                    case KeyEvent.VK_C:
                        activeSolid.setTransform(new Mat4Scale(0.5));
                        break;
                    case KeyEvent.VK_UP:
                        activeSolid.setModel(activeSolid.getModel().mul(new Mat4Transl(0, 0, 0.5)));
                        break;
                    case KeyEvent.VK_DOWN:
                        activeSolid.setModel(activeSolid.getModel().mul(new Mat4Transl(0, 0, -0.5)));
                        break;
                    case KeyEvent.VK_RIGHT:
                        activeSolid.setModel(activeSolid.getModel().mul(new Mat4Transl(0.5, 0, 0)));
                        break;
                    case KeyEvent.VK_LEFT:
                        activeSolid.setModel(activeSolid.getModel().mul(new Mat4Transl(-0.5, 0, 0)));
                        break;
                    case KeyEvent.VK_L:
                        renderer.setRasterizer(lineRasterizer);
                        break;
                    case KeyEvent.VK_T:
                        renderer.setRasterizer(triangleRasterizer);
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

        renderer.render(axes);
        renderer.render(solids);

        panel.repaint();
    }
}
