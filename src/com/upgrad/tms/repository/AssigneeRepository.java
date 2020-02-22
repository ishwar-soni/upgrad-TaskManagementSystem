package com.upgrad.tms.repository;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.AssigneeListFullException;
import com.upgrad.tms.util.AssigneeList;
import com.upgrad.tms.util.Constants;

import java.io.*;

public class AssigneeRepository {
    private AssigneeList assigneeList;
    private static AssigneeRepository assigneeRepository;

    private AssigneeRepository() throws IOException, ClassNotFoundException {
        initAssignee();
    }

    public static AssigneeRepository  getInstance() throws IOException, ClassNotFoundException{
        if (assigneeRepository == null) {
            assigneeRepository = new AssigneeRepository();
        }
        return assigneeRepository;
    }

    private void initAssignee() throws IOException, ClassNotFoundException {
        try (
                FileInputStream fi = new FileInputStream(new File(Constants.ASSIGNEE_FILE_NAME));
        ) {
            if (fi.available() > 0) {
                ObjectInputStream oi = new ObjectInputStream(fi);
                assigneeList = (AssigneeList) oi.readObject();
                oi.close();
            } else {
                File file = new File(Constants.ASSIGNEE_FILE_NAME);
                assigneeList = new AssigneeList();
            }
        }
    }

    public Assignee saveAssignee(Assignee assignee) throws AssigneeListFullException, IOException {
        assigneeList.add(assignee);
        updateListToFile();
        return assignee;
    }

    public void updateListToFile() throws IOException{
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Constants.ASSIGNEE_FILE_NAME));
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            outputStream.writeObject(assigneeList);
        }
    }

    public AssigneeList getAssigneeList() {
        return assigneeList;
    }

}
