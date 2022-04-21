package lightengine;

import java.io.BufferedReader;
import java.io.FileReader;

import lightengine.algebra.Vector3;

/**
 * Class that describes a simple 3D Scene:
 * This description is meant to be read form a scene description file (.scene
 * extension)
 *
 * @author: cdehais based on smondet, gmorin
 */

public class Scene {

    private static String nextLine(BufferedReader in) throws Exception {
        String r = in.readLine();

        while (r.matches("(\\s*#.*)|(\\s*$)")) {
            r = in.readLine();
        }
        return r;
    }

    String meshFilename;
    Vector3 cameraPosition = new Vector3("cam_pos");
    Vector3 cameraLookAt = new Vector3("cam_lookat");
    Vector3 cameraUp = new Vector3("cam_up");
    double cameraFocal;
    int screenW;
    int screenH;
    double ambientI;
    double sourceI;
    double[] sourceCoord = new double[3];
    double[] material = new double[4];

    public Scene(String filename) throws Exception {

        BufferedReader in = new BufferedReader(new FileReader(filename));
        // get directory of the scene file
        String dir = filename.substring(0,
                filename.lastIndexOf("/") != -1 ? filename.lastIndexOf("/") : filename.lastIndexOf("\\") + 1);

        meshFilename = nextLine(in);
        // concatenate the scene file directory and the mesh file name
        meshFilename = dir + meshFilename;

        String r = nextLine(in);
        String[] sar = r.split("\\s+");
        cameraPosition.set(Double.valueOf(sar[0]).doubleValue(),
                Double.valueOf(sar[1]).doubleValue(),
                Double.valueOf(sar[2]).doubleValue());

        r = nextLine(in);
        sar = r.split("\\s+");
        cameraLookAt.set(Double.valueOf(sar[0]).doubleValue(),
                Double.valueOf(sar[1]).doubleValue(),
                Double.valueOf(sar[2]).doubleValue());

        r = nextLine(in);
        sar = r.split("\\s+");
        cameraUp.set(Double.valueOf(sar[0]).doubleValue(),
                Double.valueOf(sar[1]).doubleValue(),
                Double.valueOf(sar[2]).doubleValue());

        r = nextLine(in);
        cameraFocal = Double.valueOf(r).doubleValue();

        r = nextLine(in);
        sar = r.split("\\s+");
        screenW = Integer.valueOf(sar[0]).intValue();
        screenH = Integer.valueOf(sar[1]).intValue();

        r = nextLine(in);
        ambientI = Double.valueOf(r).doubleValue();

        r = nextLine(in);
        sar = r.split("\\s+");
        for (int i = 0; i < sourceCoord.length; i++) {
            sourceCoord[i] = Double.valueOf(sar[i]).doubleValue();
        }
        sourceI = Double.valueOf(sar[3]).doubleValue();

        r = nextLine(in);
        sar = r.split("\\s+");
        for (int i = 0; i < material.length; i++) {
            material[i] = Double.valueOf(sar[i]).doubleValue();
        }
    }

    public String getMeshFileName() {
        return meshFilename;
    }

    public Vector3 getCameraPosition() {
        return cameraPosition;
    }

    public Vector3 getCameraLookAt() {
        return cameraLookAt;
    }

    public Vector3 getCameraUp() {
        return cameraUp;
    }

    public double getCameraFocal() {
        return cameraFocal;
    }

    public int getScreenW() {
        return screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public double getAmbientI() {
        return ambientI;
    }

    public double getSourceI() {
        return sourceI;
    }

    public double[] getSourceCoord() {
        return sourceCoord;
    }

    public double[] getMaterial() {
        return material;
    }

}
