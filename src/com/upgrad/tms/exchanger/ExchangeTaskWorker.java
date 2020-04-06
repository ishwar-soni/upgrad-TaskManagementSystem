package com.upgrad.tms.exchanger;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.Exchanger;

public class ExchangeTaskWorker extends AbstractWorker implements Runnable {

    private final Task task;
    private final Exchanger<String> stringExchanger;

    public ExchangeTaskWorker(AssigneeRepository assigneeRepository, Task task, Exchanger<String> stringExchanger) {
        super(assigneeRepository);
        this.task = task;
        this.stringExchanger = stringExchanger;
    }

    @Override
    public void doWork() {

    }

    @Override
    public void run() {

    }
}
