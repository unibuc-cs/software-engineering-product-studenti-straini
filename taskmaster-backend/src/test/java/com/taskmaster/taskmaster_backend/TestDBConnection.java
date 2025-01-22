package com.taskmaster.taskmaster_backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/taskmaster_db";
        String username = "root";
        String password = "1514071211";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Bağlantı başarılı!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
