package shader;

import solid.Vertex;
import transforms.Col;

@FunctionalInterface
public interface Shader {
    Col getColor(Vertex v);
}
