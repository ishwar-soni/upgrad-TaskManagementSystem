package com.upgrad.tms.repository;

import com.upgrad.tms.util.AssigneeList;

import java.io.*;

public class AssigneeRepository {
    private AssigneeList assigneeList;
    private static AssigneeRepository assigneeRepository;

    private AssigneeRepository() throws IOException, ClassNotFoundException {
        initAssignee();
    }

    private void initAssignee() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File("assignee.txt"));
        if (fi.available() > 0) {
            ObjectInputStream oi = new ObjectInputStream(fi);
            assigneeList = (AssigneeList) oi.readObject();
            oi.close();
        } else {
            assigneeList = new AssigneeList();
        }
        fi.close();
    }
}
