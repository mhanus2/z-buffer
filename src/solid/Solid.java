package solid;

import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;

public abstract class Solid {
    protected  final ArrayList<Part> partBuffer = new ArrayList<>();
    protected  final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected  final ArrayList<Integer> indexBuffer = new ArrayList<>();
    private Mat4 model = new Mat4Identity();

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }
    public Mat4 getModel() {
        return model;
    }
    public void setModel(Mat4 model) {
        this.model = model;
    }
}
