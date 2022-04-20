package rendu1.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import rendu1.Scene;

public class TaskMgr {
  private HashMap<Event, ArrayList<Consumer<Scene>>> tasks;
  private Scene scene;

  public TaskMgr(Scene scene) {
    tasks = new HashMap<Event, ArrayList<Consumer<Scene>>>();
    this.scene = scene;
  }

  public void addTask(Event event, Consumer<Scene> task) {
    if (!tasks.containsKey(event)) {
      tasks.put(event, new ArrayList<Consumer<Scene>>());
    }
    tasks.get(event).add(task);
  }

  public void removeTask(Event event, Consumer<Scene> task) throws TaskNotFoundException {
    if (tasks.containsKey(event)) {
      tasks.get(event).remove(task);
    } else {
      throw new TaskNotFoundException("Task not found");
    }
  }

  public void triggerTasks(Event event, Scene scene) {
    if (tasks.containsKey(event)) {
      for (Consumer<Scene> task : tasks.get(event)) {
        task.accept(scene);
      }
    }
  }
}
