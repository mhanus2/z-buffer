package render;


import solid.Solid;

import java.util.List;

public interface Renderer {
    public void render(Solid solid);

    public void render(List<Solid> scene);
}
