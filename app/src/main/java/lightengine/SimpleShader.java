package lightengine;

/**
 * Simple shader that copies the interpolated color to the screen.
 * 
 * @author: cdehais
 */
public class SimpleShader extends Shader {

    public SimpleShader(GraphicsWrapper screen) {
        super(screen);
    }

    synchronized public void shade(Fragment fragment) {
        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
    }

    public String getName() {
        return "SimpleShader";
    }
}
