package rendu1;

import rendu1.algebra.Matrix;
import rendu1.algebra.SizeMismatchException;
import rendu1.algebra.Vector;
import rendu1.algebra.Vector3;

/**
 * author: cdehais
 */
public class Transformation {

    Matrix worldToCamera;
    Matrix projection;
    Matrix calibration;

    public Transformation() {
        try {
            worldToCamera = new Matrix("W2C", 4, 4);
            projection = new Matrix("P", 3, 4);
            calibration = Matrix.createIdentity(3);
            calibration.setName("K");
        } catch (InstantiationException e) {
            /* should not reach */
        }
    }

    public void setLookAt(Vector3 cam, Vector3 lookAt, Vector3 up) {
        try {
            // compute rotation

            // compute translation

            // TODO

        } catch (Exception e) {
            /* unreached */
        }

        System.out.println("Modelview matrix:\n" + worldToCamera);
    }

    public void setProjection() {

        // TODO

        System.out.println("Projection matrix:\n" + projection);
    }

    public void setCalibration(double focal, double width, double height) {

        /* à compléter */

        System.out.println("Calibration matrix:\n" + calibration);
    }

    /**
     * Projects the given homogeneous, 4 dimensional point onto the screen.
     * The resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
     * is the depth of the point in the camera coordinate system.
     */
    public Vector3 projectPoint(Vector p)
            throws SizeMismatchException, InstantiationException {
        Vector ps = new Vector(3);

        /* à compléter */

        return new Vector3(ps);
    }

    /**
     * Transform a vector from world to camera coordinates.
     */
    public Vector3 transformVector(Vector3 v)
            throws SizeMismatchException, InstantiationException {
        /* Doing nothing special here because there is no scaling */
        Matrix R = worldToCamera.getSubMatrix(0, 0, 3, 3);
        Vector tv = R.multiply(v);
        return new Vector3(tv);
    }

}
