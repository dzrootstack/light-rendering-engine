package lightengine;

/**
 * Simple shader that copies the interpolated color to the screen,
 * taking the depth of the fragment into acount.
 * 
 * @author: cdehais
 */
public class DepthShader extends Shader {

    DepthBuffer depth;

    public DepthShader(GraphicsWrapper screen) {
        super(screen);
        depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }

    public void shade(Fragment fragment) {
        if (depth.testFragment(fragment)) {
            screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
            depth.writeFragment(fragment);
        }
    }

    public void reset() {
        depth.clear();
    }

    public void resize() {
        depth.resize(screen.getWidth(), screen.getHeight());
    }

    public String getName() {
        return "DepthShader";
    }
}
