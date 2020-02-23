package com.upgrad.tms.repository;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.exception.EntityListFullException;

import com.upgrad.tms.util.Constants;
import com.upgrad.tms.util.DateUtils;

import java.io.*;
import java.util.*;

public class AssigneeRepository {
    private List<Assignee> assigneeList;
    private static AssigneeRepository assigneeRepository;

    private Map <String, Assignee> assigneeMap;

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
        File file = new File(Constants.ASSIGNEE_FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (
                FileInputStream fi = new FileInputStream(new File(Constants.ASSIGNEE_FILE_NAME));
        ) {
            if (fi.available() > 0) {
                ObjectInputStream oi = new ObjectInputStream(fi);
                assigneeList = (List<Assignee>) oi.readObject();
                oi.close();
                assigneeMap = new HashMap<>();
                for (Assignee assignee: assigneeList) {
                    assigneeMap.put(assignee.getUsername(), assignee);
                }
            } else {
                assigneeList = new ArrayList<Assignee>();
                assigneeMap = new HashMap<>();
            }
        }
    }

    public Assignee saveAssignee(Assignee assignee) throws  IOException {
        assigneeList.add(assignee);
        assigneeMap.put(assignee.getUsername(), assignee);
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

    public List<Assignee> getAssigneeList() {
        return assigneeList;
    }

    public boolean isValidCredentials(String username, String passwd) {
        for (Assignee assignee: assigneeList) {
            if (assignee != null && assignee.getUsername().equals(username) &&
                assignee.getPassword().equals(passwd)) {
                return true;
            }
        }
        return false;
    }

    public Assignee getAssignee(String username) {
        return assigneeMap.get(username);
    }

    public Collection<Assignee> getUniqueAssigneesForSpecificDate(Date specificDate) {
        Set<Assignee> filteredAssignees = new HashSet<>();
        for (Assignee assignee: getAssigneeList()) {
            for (Task task: assignee.getTaskCalendar().getTaskList()) {
                if (DateUtils.isSameDate(task.getDueDate(), specificDate)) {
                    filteredAssignees.add(assignee);
                }
            }
        }
        return filteredAssignees;
    }
}
