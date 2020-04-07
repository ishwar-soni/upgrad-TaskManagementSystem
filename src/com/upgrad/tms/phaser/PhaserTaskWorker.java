package com.upgrad.tms.phaser;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.Phaser;

public class PhaserTaskWorker extends AbstractWorker implements Runnable {

    private final Task task;
    private final Phaser phaser;

    public PhaserTaskWorker(AssigneeRepository assigneeRepository, Task task, Phaser phaser) {
        super(assigneeRepository);
        this.task = task;
        this.phaser = phaser;
    }

    @Override
    public void doWork() {
        processTask(task);
        updateInFile();
    }

    @Override
    public void run() {
        doWork();
    }
}
