package raster;

import transforms.Col;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ImageBuffer implements Raster<Col> {
    private final BufferedImage img;
    private Col color;

    public ImageBuffer(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw(ImageBuffer raster) {
        Graphics graphics = img.getGraphics();
        graphics.setColor(new Color(color.getRGB()));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
    }

    @Override
    public Optional<Col> getValue(int x, int y) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            return Optional.of(new Col(img.getRGB(x, y)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(int x, int y, Col color) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            img.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(color.getRGB()));
        g.clearRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void setDefaultValue(Col color) {
        this.color = color;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    // FIXME: smazat - debug účely
    public BufferedImage getImg() {
        return img;
    }
}

