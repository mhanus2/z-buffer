package solid;

import shader.Shader;
import shader.ShaderInterpolated;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;

public abstract class Solid {
    protected final ArrayList<Part> partBuffer = new ArrayList<>();
    protected final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected final ArrayList<Integer> indexBuffer = new ArrayList<>();
    protected Shader shader = new ShaderInterpolated();
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

    public Shader getShader() {
        return shader;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void setTransform(Mat4 transformMat) {
        if (this.model.inverse().isPresent()) {
            Mat4 inverseMat = this.model.inverse().get();
            model = model.mul(inverseMat).mul(transformMat).mul(model);
        } else {
            model = model.mul(transformMat);
        }
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
