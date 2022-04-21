package lightengine.tasks;

import java.util.HashMap;

public class PayLoad {

  enum Category {
    KEY_CODE,
    MESSAGE,
    INT_ARRAY,
    FLOAT_ARRAY,
    DOUBLE_ARRAY,
    STRING_ARRAY,
    OBJECT_ARRAY,
  }

  private HashMap<Category, Object> data;

  public PayLoad() {
    data = new HashMap<Category, Object>();
  }

  public PayLoad(HashMap<Category, Object> data) {
    this.data = data;
  }

  public void setKeyCode(int keyCode) {
    this.data.put(Category.KEY_CODE, keyCode);
  }

  public void setMessage(String message) {
    this.data.put(Category.MESSAGE, message);
  }

  public void setIntArray(int[] intArray) {
    this.data.put(Category.INT_ARRAY, intArray);
  }

  public int getKeyCode() {
    return (int) this.data.get(Category.KEY_CODE);
  }

  public String getMessage() {
    return (String) this.data.get(Category.MESSAGE);
  }

  public int[] getIntArray() {
    return (int[]) this.data.get(Category.INT_ARRAY);
  }

}
