package raster;

import java.util.Arrays;
import java.util.Optional;

public class DepthBuffer implements Raster<Double> {
    private final double[][] buffer;
    private final int width, height;
    private double defaultValue;

    public DepthBuffer(int width, int height) {
        this.buffer = new double[width][height];
        this.width = width;
        this.height = height;
        this.defaultValue = 1.d;
        clear();
    }

    @Override
    public void clear() {
        for (double[] d : buffer) {
            Arrays.fill(d, defaultValue);
        }
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
    public Optional<Double> getValue(int x, int y) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            return Optional.of(buffer[x][y]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            buffer[x][y] = value;
        }
    }
}
