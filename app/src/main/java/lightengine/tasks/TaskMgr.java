package lightengine.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import lightengine.Scene;

public class TaskMgr {
  private HashMap<Event, ArrayList<Consumer<PayLoad>>> tasks;

  public TaskMgr() {
    tasks = new HashMap<Event, ArrayList<Consumer<PayLoad>>>();
  }

  /**
   * Add a task to the task manager
   * 
   * @param event The event to listen for
   * @param task  The task to execute
   */
  public void addTask(Event event, Consumer<PayLoad> task) {
    if (!tasks.containsKey(event)) {
      tasks.put(event, new ArrayList<Consumer<PayLoad>>());
    }
    tasks.get(event).add(task);
  }

  /**
   * Remove a task from the task manager
   * 
   * @param event The event to listen for
   * @param task  The task to remove
   * @throws TaskNotFoundException
   */
  public void removeTask(Event event, Consumer<Scene> task) throws TaskNotFoundException {
    if (tasks.containsKey(event)) {
      tasks.get(event).remove(task);
    } else {
      throw new TaskNotFoundException("Task not found");
    }
  }

  /**
   * Execute all tasks for the given event
   * 
   * @param event   The event to execute
   * @param payload The payload to pass to the task
   */
  public void triggerTasks(Event event, PayLoad payload) {
    if (tasks.containsKey(event)) {
      for (Consumer<PayLoad> task : tasks.get(event)) {
        task.accept(payload);
      }
    }
  }

  /**
   * Execute all tasks for the given event
   * 
   * @param event The event to execute
   */
  public void triggerTasks(Event event) {
    triggerTasks(event, null);
  }
}
