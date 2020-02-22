package com.upgrad.tms.repository;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.AssigneeListFullException;
import com.upgrad.tms.util.AssigneeList;

import java.io.*;

public class AssigneeRepository {
    private AssigneeList assigneeList;
    private static AssigneeRepository assigneeRepository;

    private AssigneeRepository() throws IOException, ClassNotFoundException {
        initAssignee();
    }

    private void initAssignee() throws IOException, ClassNotFoundException {
        try (
            FileInputStream fi = new FileInputStream(new File("assignee.txt"))
        ) {
            if (fi.available() > 0) {
                ObjectInputStream oi = new ObjectInputStream(fi);
                assigneeList = (AssigneeList) oi.readObject();
                oi.close();
            } else {
                assigneeList = new AssigneeList();
            }
        }
    }

    public Assignee saveAssignee(Assignee assignee) throws AssigneeListFullException {
        assigneeList.add(assignee);
        updateListToFile();
        return assignee;
    }

    public void updateListToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("assignee.txt"));
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(assigneeList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
