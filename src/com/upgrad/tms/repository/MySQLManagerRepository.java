package com.upgrad.tms.repository;

import com.upgrad.tms.util.Constants;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLManagerRepository {
    private static MySQLManagerRepository managerRepository;
    private Map<String, String> managerCredentials;
    static Connection conn;

    public static MySQLManagerRepository getInstance() throws IOException, ClassNotFoundException, SQLException {
        if (managerRepository == null) {
            managerRepository = new MySQLManagerRepository();
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://alldbs.c9q0z7esjprb.us-east-1.rds.amazonaws.com:3306/upgrad", "upgrad", "upgrad");
        }
        return managerRepository;
    }

    private MySQLManagerRepository() throws IOException {
        initManagerCredentials();
    }

    private void initManagerCredentials() throws IOException {
        File file = new File (Constants.MANAGER_FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        managerCredentials = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Constants.MANAGER_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                managerCredentials.put(split[0], split[1]);
            }
        } catch (IOException e) {
            System.out.println("IO Exception handling");
            e.printStackTrace();
        }
    }

    public void saveManager(String userName, String password){
        String sql = "insert into manager (username, password) values ('"+userName+"', '"+password+"')";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidCredentials(String username, String passwd) {
        String sql = "select * from manager where username = '"+username+"', password = '"+passwd;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        //return managerCredentials.containsKey(username) && managerCredentials.get(username).equals(passwd);
    }

    public boolean isManager (String username) {
        return managerCredentials.containsKey(username);
    }
}
