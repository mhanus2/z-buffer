package shader;

import solid.Vertex;
import transforms.Col;

public class ShaderConstant implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return new Col(0xffffff);
    }
}
