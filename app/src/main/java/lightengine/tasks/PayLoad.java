package lightengine.tasks;

public class PayLoad {
  private int keyCode;
  private String message;

  public PayLoad() {
  }

  public PayLoad(int keyCode, String message) {
    this.keyCode = keyCode;
    this.message = message;
  }

  public PayLoad(int keyCode) {
    this.keyCode = keyCode;
  }

  public void setKeyCode(int keyCode) {
    this.keyCode = keyCode;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getKeyCode() {
    return keyCode;
  }

  public String getMessage() {
    return message;
  }
}
