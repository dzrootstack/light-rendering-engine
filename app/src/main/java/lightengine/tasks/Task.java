package lightengine.tasks;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Task implements Consumer<PayLoad> {

  private Consumer<PayLoad> action;
  private List<Task> parentList;
  private UUID id;
  private String name;

  public Task(Consumer<PayLoad> action, List<Task> parentList, String name) {
    this.id = UUID.randomUUID();
    this.action = action;
    this.parentList = parentList;
    this.name = name;
  }

  public Task(Consumer<PayLoad> action, List<Task> parentList) {
    this(action, parentList, "Task '" + action.getClass().getSimpleName() + "'");
  }

  public void accept(PayLoad payload) {
    action.accept(payload);
  }

  /**
   * Delete the {@link Task} object from the Task Manager's records.
   * 
   * @apiNote this method should not be used, as creating a lot of tasks and
   *          removing them using Task.remove() causes the TaskMgr to not empty
   *          properly, thus leading to performance issues. Use
   *          {@link TaskMgr#removeTask(Task)} instead
   * @deprecated
   */
  @Deprecated
  public void remove() {
    parentList.remove(this);
  }

  /**
   * Get the task's unique ID.
   * <p>
   * A task's ID matches the following format : {@code ^T\d+$}
   * </p>
   * 
   * @return the task's ID
   */
  public UUID getID() {
    return id;
  }

  /**
   * Get the task's name.
   * 
   * @return the task's name
   */
  public String getName() {
    return name;
  }
}