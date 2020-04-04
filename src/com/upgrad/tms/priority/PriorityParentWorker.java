package com.upgrad.tms.priority;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.util.TaskStatus;

import java.util.List;

/**
 * This class would be responsible for controlling the priority level globally
 */
public class PriorityParentWorker implements Runnable {

    private List<Task> taskList;

    public PriorityParentWorker(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public void run() {
        while (taskList.stream().anyMatch(task -> !task.getTaskStatus().equals(TaskStatus.DONE))) {

        }
    }
}
