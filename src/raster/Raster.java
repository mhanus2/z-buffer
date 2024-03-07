package raster;

public interface Raster<T> {

    void clear();

    void setDefaultValue(T value);

    int getWidth();

    int getHeight();

    T getValue(int x, int y);

    void setValue(int x, int y, T value);

    default boolean isInside(int x, int y) {
        // TODO: implementovat
        return true;
    }

}
