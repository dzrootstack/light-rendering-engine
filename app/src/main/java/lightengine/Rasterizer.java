package lightengine;

import lightengine.algebra.Matrix;
import lightengine.algebra.SizeMismatchException;
import lightengine.algebra.Vector;
import lightengine.algebra.Vector3;

/**
 * The Rasterizer class is responsible for the discretization of geometric
 * primitives
 * (edges and faces) over the screen pixel grid and generates Fragment (pixels
 * with
 * interpolated attributes). Those Fragment are then passed to a Shader object,
 * which will produce the final color of the fragment.
 *
 * @author morin, chambon, cdehais
 */
public class Rasterizer {

    Shader shader;

    double eps = 1e-3;

    public Rasterizer(Shader shader) {
        this.shader = shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Linear interpolation of a Fragment f on the edge defined by Fragment's v1 and
     * v2
     */
    private void interpolate2(Fragment v1, Fragment v2, Fragment f) {
        int x1 = v1.getX();
        int y1 = v1.getY();
        int x2 = v2.getX();
        int y2 = v2.getY();
        int x = f.getX();
        int y = f.getX();

        double alpha;
        if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
            alpha = (double) (x - x1) / (double) (x2 - x1);
        } else {
            if (y2 != y1) {
                alpha = (double) (y - y1) / (double) (y2 - y1);
            } else {
                alpha = 0.5;
            }
        }
        int numAttributes = f.getNumAttributes();
        for (int i = 0; i < numAttributes; i++) {
            f.setAttribute(i, (1.0 - alpha) * v1.getAttribute(i) + alpha * v2.getAttribute(i));
        }
    }

    /*
     * Swaps x and y coordinates of the fragment. Used by the Bresenham algorithm.
     */
    private static void swapXAndY(Fragment f) {
        f.setPosition(f.getY(), f.getX());
    }

    /**
     * Rasterizes the edge between the projected vectors v1 and v2.
     * Generates Fragment's and calls the Shader::shade() metho on each of them.
     */
    public void rasterizeEdge(Fragment v1, Fragment v2) {

        /* Coordinates of V1 and V2 */
        int x1 = v1.getX();
        int y1 = v1.getY();
        int x2 = v2.getX();
        int y2 = v2.getY();

        /* Display the vertices */
        Fragment f = new Fragment(0, 0);
        int size = 2;
        for (int i = 0; i < v1.getNumAttributes(); i++) {
            f.setAttribute(i, v1.getAttribute(i));
        }
        for (int i = -size; i <= size; i++) {
            for (int j = -size; j <= size; j++) {
                f.setPosition(x1 + i, y1 + j);
                if (!shader.isClipped(f)) {
                    shader.shade(f);
                }
            }
        }

        // tracé d'un segment avec l'algo de Bresenham
        Fragment fragment = new Fragment(0, 0);

        boolean sym = (Math.abs(y2 - y1) > Math.abs(x2 - x1));
        if (sym) {
            int temp;
            temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if (x1 > x2) {
            Fragment ftemp;
            int temp;
            temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
            ftemp = v1;
            v1 = v2;
            v2 = ftemp;
        }

        int ystep;
        if (y1 < y2) {
            ystep = 1;
        } else {
            ystep = -1;
        }

        int x = x1;
        float y_courant = y1;
        int y = y1;
        float delta_y = y2 - y1;
        float delta_x = x2 - x1;
        float m = delta_y / delta_x;

        for (int i = 1; i <= delta_x; i++) {
            x = x + 1;
            y_courant = y_courant + m;
            if (!((ystep == 1) && (y_courant < y + 0.5) || ((ystep == -1) && (y_courant > y
                    - 0.5)))) {
                y = y + ystep;
            }
            // envoi du fragment au shader
            fragment.setPosition(x, y);

            // remove this condition to solve the bug
            // if (!shader.isClipped(fragment)) {

            // interpolation des attributs
            interpolate2(v1, v2, fragment);
            if (sym) {
                swapXAndY(fragment);
            }
            shader.shade(fragment);
            // }
        }
    }

    static double triangleArea(Fragment v1, Fragment v2, Fragment v3) {
        return (double) v2.getX() * v3.getY() - v2.getY() * v3.getX()
                + v3.getX() * v1.getY() - v1.getX() * v3.getY()
                + v1.getX() * v2.getY() - v2.getX() * v1.getY();
    }

    static protected Matrix makeBarycentricCoordsMatrix(Fragment v1, Fragment v2, Fragment v3) {
        Matrix C = null;
        try {
            C = new Matrix(3, 3);
        } catch (InstantiationException e) {
            /* unreached */
        }

        double area = triangleArea(v1, v2, v3);
        int x1 = v1.getX();
        int y1 = v1.getY();
        int x2 = v2.getX();
        int y2 = v2.getY();
        int x3 = v3.getX();
        int y3 = v3.getY();
        C.set(0, 0, (x2 * y3 - x3 * y2) / area);
        C.set(0, 1, (y2 - y3) / area);
        C.set(0, 2, (x3 - x2) / area);
        C.set(1, 0, (x3 * y1 - x1 * y3) / area);
        C.set(1, 1, (y3 - y1) / area);
        C.set(1, 2, (x1 - x3) / area);
        C.set(2, 0, (x1 * y2 - x2 * y1) / area);
        C.set(2, 1, (y1 - y2) / area);
        C.set(2, 2, (x2 - x1) / area);

        return C;
    }

    private void interpolate3(Fragment v1, Fragment v2, Fragment v3, Fragment f, Vector barycentricCoords) {
        int numAttributes = v1.getNumAttributes();
        for (int i = 0; i < numAttributes; i++) {
            f.setAttribute(i, barycentricCoords.get(0) * v1.getAttribute(i)
                    + barycentricCoords.get(1) * v2.getAttribute(i) + barycentricCoords.get(2) * v3.getAttribute(i));
        }
    }

    /**
     * Rasterizes the triangular face made of the Fragment v1, v2 and v3
     */
    public void rasterizeFace(Fragment v1, Fragment v2, Fragment v3) {

        Matrix C = makeBarycentricCoordsMatrix(v1, v2, v3);

        /* iterate over the triangle's bounding box */
        int xmin = Math.min(Math.min(v1.getX(), v2.getX()), v3.getX());
        int xmax = Math.max(Math.max(v1.getX(), v2.getX()), v3.getX());
        int ymin = Math.min(Math.min(v1.getY(), v2.getY()), v3.getY());
        int ymax = Math.max(Math.max(v1.getY(), v2.getY()), v3.getY());

        try {
            Fragment f = new Fragment(0, 0);
            for (int x = xmin; x <= xmax; x++) {
                for (int y = ymin; y <= ymax; y++) {
                    f.setPosition(x, y);
                    Vector barycentricCoords = C.multiply(new Vector3(1, x, y));
                    if (!shader.isClipped(f)) {
                        if (barycentricCoords.get(0) >= -eps && barycentricCoords.get(1) >= -eps
                                && barycentricCoords.get(2) >= -eps) {
                            interpolate3(v1, v2, v3, f, barycentricCoords);
                            shader.shade(f);
                        }
                    }
                }
            }
        } catch (SizeMismatchException e) {
            throw new RuntimeException(e);
        }

    }
}
