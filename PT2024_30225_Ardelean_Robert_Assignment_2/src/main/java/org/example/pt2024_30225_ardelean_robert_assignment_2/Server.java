package org.example.pt2024_30225_ardelean_robert_assignment_2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;


    public Server() {
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public void addTask(Task newTask) {
        tasks.offer(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public int getTaskSize(){
        return tasks.size();
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Task currentTask;
            try {
                currentTask = tasks.take();

                Thread.sleep(currentTask.getServiceTime());

                waitingPeriod.addAndGet(-currentTask.getServiceTime());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
