package raster;

import transforms.Col;

import java.util.Optional;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col col) {
        // Check if valid z value
        if (0<=z && z<=1) {
            // Load z value from DepthBuffer
            Optional<Double> zOptional = depthBuffer.getValue(x,y);
            if (zOptional.isPresent() && zOptional.get() > z) {
                depthBuffer.setValue(x,y,z);
                imageBuffer.setValue(x,y,col);
            }

            imageBuffer.setValue(x, y, col);
        }
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
