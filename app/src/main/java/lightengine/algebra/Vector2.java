/**
 * @author: cdehais 
 */

package lightengine.algebra;

public class Vector2 extends Vector {

  public Vector2(double x, double y) {
    super();
    try {
      allocValues(2);
    } catch (java.lang.InstantiationException e) {
      /* unreached */
    }
    this.values[0] = x;
    this.values[1] = y;
  }

  public Vector2() {
    this(0.0, 0.0);
  }

  public Vector2(String name) {
    this(0.0, 0.0);
    this.setName(name);
  }

  /**
   * Creates a new named 3D vector with coordinates (x, y, z).
   */
  public Vector2(String name, double x, double y) {
    super();
    try {
      allocValues(3);
    } catch (java.lang.InstantiationException e) {
      /* unreached */
    }

    this.values[0] = x;
    this.values[1] = y;
  }

  /**
   * Copy constructor from a Vector of size 3 or 4.
   * For a vector of size 4, divide the 3 first coordinates by the fourth.
   */
  public Vector2(Vector v) throws InstantiationException {
    this();
    if ((v.size != 2) && (v.size != 3)) {
      throw new InstantiationException("Can only build 3D vector from vector of size 3 or 4");
    }

    if (v.size == 2) {
      set(v.get(0), v.get(1));
    } else {
      double w = v.get(3);
      set(v.get(0) / w, v.get(1) / w);
    }
  }

  /**
   * Makes the x, y, and z coordinates of the Vector2 cartesian, by dividing them
   * by
   * the homogeneous coordinate w.
   */
  public void makeCartesian() throws java.lang.ArithmeticException {
    this.values[0] /= this.values[2];
    this.values[1] /= this.values[2];
    this.values[2] = 1.0;
  }

  public void set(double x, double y) {
    this.values[0] = x;
    this.values[1] = y;
  }

  public double dot(Vector2 v) {
    return (values[0] * v.values[0] + values[1] * v.values[1]);
  }

  public double norm() {
    double r = (values[0] * values[0] + values[1] * values[1]);
    return Math.sqrt(r);
  }

  /**
   * Gets the x coordinates of the Vector2
   */
  public double getX() {
    return this.values[0];
  }

  /**
   * Gets the y coordinates of the Vector2
   */
  public double getY() {
    return this.values[1];
  }

}
