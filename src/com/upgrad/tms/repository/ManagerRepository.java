package com.upgrad.tms.repository;

import com.upgrad.tms.exception.ManagerMapFullException;
import com.upgrad.tms.util.Constants;
import com.upgrad.tms.util.ManagerMap;

import java.io.*;

public class ManagerRepository {
    private static ManagerRepository managerRepository;
    private ManagerMap managerCredentials;

    public static ManagerRepository getInstance() throws IOException {
        if (managerRepository == null) {
            managerRepository = new ManagerRepository();
        }
        return managerRepository;
    }

    private ManagerRepository() throws IOException {
        initManagerCredentials();
    }

    private void initManagerCredentials() throws IOException {
        File file = new File (Constants.MANAGER_FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        managerCredentials = new ManagerMap();
        try (BufferedReader br = new BufferedReader(new FileReader(Constants.MANAGER_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                managerCredentials.put(split[0], split[1]);
            }
        } catch (IOException e) {
            System.out.println("IO Exception handling");
            e.printStackTrace();
        } catch (ManagerMapFullException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveManager(String userName, String password) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(Constants.MANAGER_FILE_NAME, true));
            String line = userName+","+password+"\n";
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            System.out.println("IO Exception handling");
            e.printStackTrace();
        }
    }

    public boolean isValidCredentials(String username, String passwd) {
    }
}
