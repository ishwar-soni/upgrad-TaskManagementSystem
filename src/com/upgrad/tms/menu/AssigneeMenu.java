package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Meeting;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.entities.Todo;
import com.upgrad.tms.exception.NotFoundException;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.DateUtils;

import java.io.IOException;
import java.util.*;

public class AssigneeMenu implements OptionsMenu {
    private AssigneeRepository assigneeRepository;

    public AssigneeMenu () {
        try {
            assigneeRepository = AssigneeRepository.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showTopOptions() throws InputMismatchException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. See all tasks");
        System.out.println("2. See Today's Task");
        System.out.println("3. See Task sorted on priority");
        System.out.println("4. Tasks by task category");
        System.out.println("5. Change task status");
        System.out.println("6. Change multiple task status together");
        System.out.println("7. Exit");
        int choice = 0;

            choice = sc.nextInt();


        switch (choice) {
            case 1:
                seeAllTasks(); //done
                break;
            case 2:
                seeTodayTasks(); //done
                break;
            case 3:
                seeTaskSortedOnPriority();//done
                break;
            case 4:
                seeTaskByCategory(); //done
                break;
            case 5:
                changeTaskStatus();
                break;
            case 6:
                changeMultipleTaskStatus();
                break;
            case 7:
                MainMenu.exit();
                break;
            default:
                wrongInput();
        }
//        showTopOptions();
    }

    private void changeMultipleTaskStatus() {
        long taskId = 0;
        Scanner sc = new Scanner(System.in);
        List<Task> taskList = new ArrayList<>();
        Task task = null;
        do {
            System.out.println("Enter the task id to complete or -1 to process the already added: ");
            taskId = sc.nextLong();
            if (taskId != -1) {
                task = assigneeRepository.getTaskById(taskId);
                if (task == null) {
                    System.out.println("Task not found for id: "+taskId);
                } else {
                    taskList.add(task);
                }
            }
        } while (taskId != -1);
        List<Thread> threadList = new ArrayList<>(taskList.size());
        for (Task taskItem: taskList) {
            Thread thread = new Thread(new TaskWorker(taskItem, assigneeRepository));
            thread.setPriority(Thread.MAX_PRIORITY - taskItem.getPriority());
            threadList.add(thread);
        }
        threadList.forEach(Thread::start);
    }

    private void changeTaskStatus() {
        Scanner sc = new Scanner(System.in);
        Task task = null;
        long taskId = 0;
        do {
            System.out.println("Enter the task id to complete: ");
            taskId = sc.nextLong();
            task = assigneeRepository.getTaskById(taskId);
        } while (task == null);
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread: "+currentThread.getName());
        Thread thread = new Thread(new TaskWorker(task, assigneeRepository));
        System.out.println("User Thread: "+thread.getName());
        thread.setDaemon(true);
        thread.start();
        System.out.println("isDaemon: "+thread.isDaemon());
        System.out.println("Main thread isDaemon: "+currentThread.isDaemon());
    }

    @Override
    public void showAgain() {
        System.out.println("This is under implementation");
        showTopOptions();
    }

    private void seeTaskByCategory() {
        Map<String, List<Task>> listMap = new TreeMap<>();
        List<Class<? extends Task>> classTypes = List.of(Todo.class, Meeting.class);
        //init list for all classes there in classTypes
        for (Class<? extends Task> classType : classTypes) {
            listMap.put(classType.getSimpleName(), new ArrayList<>());
        }

        Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
        List<Task> taskList = assignee.getTaskCalendar().getTaskList();
        for (Task task : taskList) {
            List<Task> taskTypeList = listMap.get(task.getClass().getSimpleName());
            if (taskTypeList != null) {
                taskTypeList.add(task);
            } else {
                throw new NotFoundException("Task type not found");
            }
        }

        for (Map.Entry<String, List<Task>> listEntry : listMap.entrySet()) {
            System.out.println("======= Category: " + listEntry.getKey() + " =======");
            if (listEntry.getValue().isEmpty()) {
                System.out.println("No task in this category");
            }
            for (Task task : listEntry.getValue()) {
                task.printTaskOnConsole();
            }
            System.out.println("=======================");
        }
    }

    private void seeTaskSortedOnPriority() {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            PriorityQueue<Task> taskPriorityQueue = new PriorityQueue<>(new Comparator<Task>() {
                @Override
                public int compare(Task t1, Task t2) {
                    return t1.getPriority() - t2.getPriority();
                }
            });
            for (Task task : taskList) {
                if (DateUtils.isSameDate(task.getDueDate(), Calendar.getInstance().getTime())) {
                    taskPriorityQueue.add(task);
                }
            }
            while (!taskPriorityQueue.isEmpty()) {
                taskPriorityQueue.poll().printTaskOnConsole();
            }
        }
    }

    private void seeTodayTasks() {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            List<Task> todayTaskList = new ArrayList<>();
            for (Task task : taskList) {
                if (DateUtils.isSameDate(task.getDueDate(), Calendar.getInstance().getTime())) {
                    todayTaskList.add(task);
                }
            }
            if (todayTaskList.isEmpty()) {
                System.out.println("Hurray! No task for today");
            } else {
                printTaskList(todayTaskList);
            }
        }
    }

    private void seeAllTasks() {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            printTaskList(taskList);
        } else {
            System.out.println("User is not loggedin. Please login first");
        }
    }

    private void printTaskList (List<Task> taskList) {
        for (Task task: taskList) {
            task.printTaskOnConsole();
            System.out.println("\n");
        }
    }
}
