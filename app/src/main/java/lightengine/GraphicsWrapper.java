package lightengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import lightengine.tasks.Event;
import lightengine.tasks.PayLoad;
import lightengine.tasks.TaskMgr;

/**
 * A "virtual" screen, where only "setPixel" is available
 * (It is a JFrame, and JFrame.EXIT_ON_CLOSE is set)
 * 
 * @author smondet
 */
class ImageComponent extends JComponent {

  BufferedImage renderedImage = null;

  public ImageComponent(BufferedImage init) {
    renderedImage = init;
  }

  public void setImage(BufferedImage image) {
    renderedImage = image;
    repaint();
  }

  public BufferedImage swapImage(BufferedImage bi) {
    BufferedImage ret = renderedImage;
    renderedImage = bi;
    return ret;
  }

  public void paint(Graphics g) {

    if (renderedImage != null) {
      ((Graphics2D) g).drawImage(renderedImage, new AffineTransform(1f, 0f, 0f, 1f, 0, 0), null);
    }
  }

}

public class GraphicsWrapper {

  private int height = 0;
  private int width = 0;
  private int pixelSize = 0;

  private ImageComponent drawComp = null;
  private Label statusText;
  private JFrame frame;

  private KeyListener myKeyListener;
  private ComponentAdapter myComponentAdapter;
  private TaskMgr taskMgr;

  private BufferedImage backBuffer = null;
  private BufferedImage frontBuffer = null;

  private void init() {
    backBuffer = new BufferedImage(width * pixelSize, height * pixelSize, BufferedImage.TYPE_INT_ARGB);
    frontBuffer = new BufferedImage(width * pixelSize, height * pixelSize, BufferedImage.TYPE_3BYTE_BGR);

    drawComp = new ImageComponent(frontBuffer);
    drawComp.setBackground(Color.BLACK);
    drawComp.setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
    drawComp.setVisible(true);

    statusText = new Label("");
    statusText.setBackground(new Color(35, 33, 38));
    statusText.setForeground(new Color(180, 180, 180));
    statusText.setVisible(true);

    frame = new JFrame("Inverse rasterizer demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(drawComp, "Center");
    frame.add(statusText, "South");
    frame.pack();
    frame.setVisible(true);

    myComponentAdapter = new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        PayLoad pl = new PayLoad();
        Dimension d = e.getComponent().getSize();
        pl.setIntArray(new int[] { (int) d.getWidth(), (int) d.getHeight() });
        taskMgr.triggerTasks(Event.WINDOW_RESIZED, pl);
      }
    };
    myKeyListener = new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        PayLoad pl = new PayLoad();
        pl.setKeyCode(e.getKeyCode());
        taskMgr.triggerTasks(Event.KEY_PRESSED, pl);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        PayLoad pl = new PayLoad();
        pl.setKeyCode(e.getKeyCode());
        taskMgr.triggerTasks(Event.KEY_RELEASED, pl);
      }
    };
    frame.addComponentListener(myComponentAdapter);
    frame.addKeyListener(myKeyListener);
  }

  /**
   * Build a virtual screen of size width x height
   * And set its window visible.
   */
  public GraphicsWrapper(TaskMgr taskMgr, int width, int height) {
    this(taskMgr, width, height, 1);
  }

  /**
   * Build a virtual screen of size width x height, where one virtual pixel is
   * represented by
   * a pixelSize x pixelSize square.
   * And set its window visible.
   */
  public GraphicsWrapper(TaskMgr taskMgr, int width, int height, int pixelSize) {
    this.taskMgr = taskMgr;
    this.height = height;
    this.width = width;
    this.pixelSize = pixelSize;
    init();
  }

  /**
   * Lights the pixel (x,y) with color (r, g, b) (values clamped to [0,1])
   * on the current draw buffer.
   * Does nothing for pixels out of the screen.
   */
  public void setPixel(int x, int y, double r, double g, double b) {

    r = Math.min(1.0, Math.max(0.0, r));
    g = Math.min(1.0, Math.max(0.0, g));
    b = Math.min(1.0, Math.max(0.0, b));

    setPixel(x, y, (char) (r * 255), (char) (g * 255), (char) (b * 255));
  }

  /**
   * Lights the pixel (x,y) with color (r, g, b) (values clamped to [0, 255])
   * on the current draw buffer.
   * Does nothing for pixels out of the screen.
   */
  public void setPixel(int x, int y, char r, char g, char b) {

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      int argb = 0xFF000000;
      argb += ((int) r) << (8 * 2);
      argb += ((int) g) << (8 * 1);
      argb += ((int) b);

      for (int i = 0; i < pixelSize; i++) {
        for (int j = 0; j < pixelSize; j++) {
          backBuffer.setRGB(i + (x * pixelSize), j + (y * pixelSize), argb);
        }
      }
    }
  }

  /**
   * Lights the pixel (x,y) with the given color.
   * Does nothing for pixels out of the screen.
   */
  public void setPixel(int x, int y, Color color) {

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      int rgb = color.getRGB();
      for (int i = 0; i < pixelSize; i++) {
        for (int j = 0; j < pixelSize; j++) {
          backBuffer.setRGB(i + (x * pixelSize), j + (y * pixelSize), rgb);
        }
      }
    }
  }

  /**
   * Gets the pixel in the back buffer
   */
  public Color getPixel(int x, int y) {
    Color color;

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      color = new Color(backBuffer.getRGB(x, y), false);
    } else {
      color = Color.BLACK;
    }

    return color;
  }

  public Color getFrontPixel(int x, int y) {
    Color color;

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      color = new Color(frontBuffer.getRGB(x, y), false);
    } else {
      color = Color.BLACK;
    }

    return color;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }

  /**
   * Updates the size of the screen and recreates the buffers.
   */
  public void resize(int width, int height) {
    this.width = width;
    this.height = height;
    // recreate buffers
    backBuffer = new BufferedImage(width * pixelSize, height * pixelSize, BufferedImage.TYPE_INT_ARGB);
    frontBuffer = new BufferedImage(width * pixelSize, height * pixelSize, BufferedImage.TYPE_INT_ARGB);
    drawComp.setImage(frontBuffer);
  }

  /**
   * Sets the content displayed on the status bar.
   * 
   * @param text the text to display
   */
  public void setStatusText(String text) {
    statusText.setText(text);
  }

  /**
   * Clear current draw-buffer with the given color.
   *
   */
  public void clearBuffer(Color c) {
    Graphics2D gd = backBuffer.createGraphics();
    gd.setColor(c);
    gd.fillRect(0, 0, width * pixelSize, height * pixelSize);
    gd.dispose();
  }

  /**
   * Clear current draw-buffer (ie Paint it black)
   */
  public void clearBuffer() {
    clearBuffer(Color.BLACK);
  }

  /**
   * Draw current draw-buffer on the window.
   *
   */
  public void swapBuffers() {
    frontBuffer = drawComp.swapImage(backBuffer);
    drawComp.paintImmediately(0, 0, width * pixelSize, height * pixelSize);
  }

  /**
   * Destroy window.
   */
  public void destroy() {
    frame.dispose();
  }

}
