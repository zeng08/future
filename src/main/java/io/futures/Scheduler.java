package io.futures;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

  private static ThreadLocal<Scheduler> local = new ThreadLocal<Scheduler>() {
    @Override
    public Scheduler initialValue() {
      return new Scheduler();
    }
  };

  private List<Runnable> tasks = new ArrayList<>();

  private boolean running = false;

  public static void submit(final Runnable r) {
    local.get().addTask(r);
  }

  private void addTask(final Runnable r) {
    if (tasks == null)
      tasks = new ArrayList<>(1);
    tasks.add(r);
    if (!running)
      run();
  }

  private void run() {
    running = true;
    while (tasks != null && !tasks.isEmpty()) {
      final List<Runnable> pending = tasks;
      tasks = null;
      pending.forEach(r -> r.run());
    }
    running = false;
  }
}