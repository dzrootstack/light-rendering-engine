package lightengine;

public enum RenderingMode {
  /* wireframe rendering */
  WIREFRAME {
    public String toString() {
      return "Wireframe";
    }
  },
  /* solid rendering, no lighting */
  SOLID {
    public String toString() {
      return "Solid";
    }
  },
  /* solid rendering, with texture */
  SOLID_TEXTURE {
    public String toString() {
      return "Solid with texture";
    }
  },
}
