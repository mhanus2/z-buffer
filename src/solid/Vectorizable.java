package solid;

public interface Vectorizable<T> {
    T mul(double k);
    T add(T v);
}
