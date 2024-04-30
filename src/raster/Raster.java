package raster;

import java.util.Optional;

public interface Raster<T> {

    void clear();

    void setDefaultValue(T value);

    int getWidth();

    int getHeight();

    Optional<T> getValue(int x, int y);

    void setValue(int x, int y, T value);

}
