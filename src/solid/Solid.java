package solid;

import java.util.ArrayList;

public abstract class Solid {
    protected  final ArrayList<Part> partBuffer = new ArrayList<>();
    protected  final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected  final ArrayList<Integer> indexBuffer = new ArrayList<>();

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }
}
