package shader;

import solid.Vertex;
import transforms.Col;

import java.awt.image.BufferedImage;

public class ShaderTexture implements Shader {
    private final BufferedImage texture;

    public ShaderTexture(BufferedImage texture) {
        this.texture = texture;
    }

    @Override
    public Col getColor(Vertex v) {
        int x = (int) (v.getUv().getX() * (texture.getWidth()));
        int y = (int) (v.getUv().getY() * (texture.getHeight()));
        return new Col(texture.getRGB(x, y));
    }
}
