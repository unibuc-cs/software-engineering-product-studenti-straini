package com.taskmaster.taskmaster_backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3307/taskmaster_db";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Conexiune la baza de date reusita!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
