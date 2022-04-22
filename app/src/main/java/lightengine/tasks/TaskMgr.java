package lightengine.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import lightengine.Scene;

public class TaskMgr {
  private HashMap<Event, ArrayList<Task>> tasks;

  public TaskMgr() {
    tasks = new HashMap<Event, ArrayList<Task>>();
  }

  void initRecord(Event event) {
    if (!tasks.containsKey(event)) {
      tasks.put(event, new ArrayList<Task>());
    }
  }

  /**
   * Add a task to the task manager
   * 
   * @param event  The event to listen for
   * @param action The action to perform
   */
  public void addTask(Event event, Consumer<PayLoad> action) {
    initRecord(event);
    tasks.get(event).add(new Task(action, tasks.get(event)));
  }

  /**
   * Add a task to the task manager
   * 
   * @param event  The event to listen for
   * @param action The action to perform
   * @param name   The name of the task
   */
  public void addTask(Event event, Consumer<PayLoad> action, String name) {
    initRecord(event);
    tasks.get(event).add(new Task(action, tasks.get(event), name));
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
      for (Task task : tasks.get(event)) {
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

  /**
   * Print the task manager's tasks to the console
   */
  public void printTasks() {
    for (Event event : tasks.keySet()) {
      System.out.println("Event " + event.toString());
      for (Task task : tasks.get(event)) {
        System.out.println("\t" + task.getName() + " : " + task.getID());
      }
    }
  }
}
