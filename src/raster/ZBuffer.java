package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col col) {
        // TODO: načtu hodnotu z depth bufferu na souřadnici x, y
        // TODO: kontrola, jestli jsem dostal validní hodnotu
        // TODO: kontrola, jestli nové z  < staré z
        // TODO: pokud platí: 1) Obarvit 2) zapsat nové z do depth bufferu

        imageBuffer.setValue(x, y, col);
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }

    // TODO: odebrat, jen pro debug
    public Raster<Col> getImageBuffer() {
        return imageBuffer;
    }
}
