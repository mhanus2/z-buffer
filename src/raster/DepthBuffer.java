package raster;

public class DepthBuffer implements Raster<Double> {
    private final double[][] buffer;
    private final int width, height;
    private double defaultValue;

    public DepthBuffer(int width, int height) {
        this.buffer = new double[width][height];
        this.width = width;
        this.height = height;
        this.defaultValue = 1.d;
    }

    @Override
    public void clear() {
        // TODO: implementovat
    }

    @Override
    public void setDefaultValue(Double value) {
        this.defaultValue = value;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Double getValue(int x, int y) {
        // TODO: implementovat
        return null;
    }

    @Override
    public void setValue(int x, int y, Double value) {
        // TODO: implementovat
    }
}
