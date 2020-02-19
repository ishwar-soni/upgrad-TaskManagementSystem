package com.upgrad.tms.menu;

import java.util.Scanner;

public class MainMenu {

    private void getLoginDetails() {
        Scanner sc = new Scanner (System.in);
        System.out.println("Enter Username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String passwd = sc.nextLine();
        processInput(username, passwd);
    }

    private void processInput ( String username, String passwd) {
        
    }
}
