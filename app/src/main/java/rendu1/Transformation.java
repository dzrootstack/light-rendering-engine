package rendu1;

import rendu1.algebra.Matrix;
import rendu1.algebra.SizeMismatchException;
import rendu1.algebra.Vector;
import rendu1.algebra.Vector3;

/**
 * author: cdehais
 */
public class Transformation {

    boolean verbose = false;
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

            // new API
            // Vector3 z = (Vector3) cam.readOnly.sub(lookAt).readOnly.normalize();
            // Vector3 x = (Vector3) up.cross(z).readOnly.normalize();
            // Vector3 y = z.cross(x);

            // old API
            Vector3 z = new Vector3(lookAt);
            z.sub(cam);
            z.normalize();
            Vector3 x = new Vector3(up).cross(z);
            x.normalize();
            Vector3 y = new Vector3(z).cross(x);
            y.normalize();

            for (int j = 0; j < 3; j++) {
                worldToCamera.set(0, j, x.get(j));
                worldToCamera.set(1, j, y.get(j));
                worldToCamera.set(2, j, z.get(j));
            }

            Matrix A = worldToCamera.getSubMatrix(0, 0, 3, 3);

            // compute translation
            Vector c = A.multiply(cam);
            for (int j = 0; j < 3; j++) {
                worldToCamera.set(j, 3, -c.get(j));
            }
            worldToCamera.set(3, 3, 1.0);

        } catch (SizeMismatchException | InstantiationException e) {
            /* should not reach */
        }

        if (verbose) {
            System.out.println("Modelview matrix:\n" + worldToCamera);
        }
    }

    public void setProjection(double fov, double aspect, double near, double far) {

        projection.set(0, 0, 1.0);
        projection.set(1, 1, 1.0);
        projection.set(2, 2, 1.0);

        // projection.set(0, 0, 1.0 / Math.tan(fov / 2.0));
        // projection.set(1, 1, projection.get(0, 0) / aspect);
        // projection.set(2, 2, (far + near) / (near - far));
        // projection.set(2, 3, 2.0 * far * near / (near - far));

        if (verbose) {
            System.out.println("Projection matrix:\n" + projection);
        }
    }

    public void setCalibration(double focal, double width, double height) {

        calibration.set(0, 0, focal);
        calibration.set(1, 1, focal);
        calibration.set(0, 2, width / 2.0);
        calibration.set(1, 2, height / 2.0);

        if (verbose) {
            System.out.println("Calibration matrix:\n" + calibration);
        }
    }

    /**
     * Projects the given homogeneous, 4 dimensional point onto the screen.
     * The resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
     * is the depth of the point in the camera coordinate system.
     */
    public Vector3 projectPoint(Vector p)
            throws SizeMismatchException, InstantiationException {
        Vector pOut = new Vector(3);
        Vector px = worldToCamera.multiply(p);
        px = projection.multiply(px);
        px = calibration.multiply(px);

        pOut.set(0, px.get(0) / px.get(2));
        pOut.set(1, px.get(1) / px.get(2));
        pOut.set(2, px.get(2));

        return new Vector3(pOut);
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
