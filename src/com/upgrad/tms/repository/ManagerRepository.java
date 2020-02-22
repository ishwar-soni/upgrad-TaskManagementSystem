package com.upgrad.tms.repository;

import com.upgrad.tms.util.ManagerMap;

public class ManagerRepository {
    private static ManagerRepository managerRepository;
    private ManagerMap managerCredentials;

    public static ManagerRepository getInstance() {
        if (managerRepository == null) {
            managerRepository = new ManagerRepository();
        }
        return managerRepository;
    }

    private ManagerRepository() {
        initManagerCredentials();
    }

    private void initManagerCredentials() {
        
    }
}
