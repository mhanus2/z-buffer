package render;


import solid.Solid;

import java.util.List;

public interface Renderer {
    void render(Solid solid);

    void render(List<Solid> scene);
}
