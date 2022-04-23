package lightengine;

public enum RenderingMode {
  /* wireframe rendering */
  WIREFRAME {
    public String toString() {
      return "Wireframe";
    }
  },
  /* wireframe rendering, multithreaded */
  WIREFRAME_MT {
    public String toString() {
      return "Wireframe MT";
    }
  },
  /* solid rendering */
  SOLID {
    public String toString() {
      return "Solid";
    }
  },
  /* solid rendering, multithreaded */
  SOLID_MT {
    public String toString() {
      return "Solid Multithreaded";
    }
  },
}
