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

            worldToCamera = Matrix.createIdentity(4);
            worldToCamera.setName("W2C");

            Vector3 z = (Vector3) cam.readOnly.sub(lookAt).readOnly.normalize();
            Vector3 x = (Vector3) up.cross(z).readOnly.normalize();
            Vector3 y = z.cross(x);

            worldToCamera.set(0, 0, x.getX());
            worldToCamera.set(0, 1, x.getY());
            worldToCamera.set(0, 2, x.getZ());
            worldToCamera.set(1, 0, y.getX());
            worldToCamera.set(1, 1, y.getY());
            worldToCamera.set(1, 2, y.getZ());
            worldToCamera.set(2, 0, z.getX());
            worldToCamera.set(2, 1, z.getY());
            worldToCamera.set(2, 2, z.getZ());

            // compute translation

            worldToCamera.set(0, 3, cam.getX());
            worldToCamera.set(1, 3, cam.getY());
            worldToCamera.set(2, 3, cam.getZ());

            // compute projection

            projection = Matrix.createIdentity(3);
            projection.setName("P");
            projection.set(0, 0, 1.0 / Math.tan(Math.toRadians(45.0 / 2.0)));
            projection.set(1, 1, 1.0 / Math.tan(Math.toRadians(45.0 / 2.0)));
            projection.set(2, 2, -1.0);
            projection.set(2, 3, -1.0);
            projection.set(3, 2, -1.0);
            projection.set(3, 3, 0.0);
        } catch (SizeMismatchException | InstantiationException e) {
            /* should not reach */
        }

        // compute translation

        // TODO

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
