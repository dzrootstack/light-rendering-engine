package lightengine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 2D Texture class.
 */
public class Texture {
    int width;
    int height;
    BufferedImage image;

    /**
     * Constructs a new Texture with the content of the image at @path.
     */
    public Texture(String path) throws Exception {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * Samples the texture at texture coordinates (u,v), using nearest neighboor
     * interpolation
     * u and v and wrapped around to [0,1].
     */
    public Color sample(double u, double v) {

        int x = Math.min(Math.max((int) (u % 1 * width), 0), width - 1);
        int y = Math.min(Math.max((int) (v % 1 * height), 0), height - 1);

        return new Color(image.getRGB(x, y));
    }
}
