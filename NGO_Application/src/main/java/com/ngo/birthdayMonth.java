package com.ngo;

import java.io.IOException;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class birthdayMonth  extends HttpServlet {
@Override
	 protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException{

        try {
        	
        	// Step 1 - eshtablish connection
        	Class.forName("com.mysql.jdbc.Driver");
        	 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306?user=root&password=root");
        	
        	 
            // Get the current month and year
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();

            // Step 2 - Prepare a statement using connection object
            // Create and execute the SQL query
            String query = "SELECT * FROM members WHERE MONTH(dob) = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, currentMonth);
                
                // Step 3 - Execute the query or update the query
                ResultSet rs = stmt.executeQuery();

                // Step 4 - Process the ResultSet object
             // Prepare member data for JSON response
                Gson gson = new Gson();
                while (rs.next()) {
                    String firstName = rs.getString("first_name"); // Assuming the member's name is stored in a column named "first_name"
                    String surName = rs.getString("last_name");
                    Date birthDate = rs.getDate("birth_date"); // Assuming the birth date is stored in a column named "birth_date"
                    MemberData memberData = new MemberData(firstName, surName, birthDate);
                    String json = gson.toJson(memberData);
                    out.println(json);
                }
             // Clean up resources
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private class MemberData {
            private String firstName;
            private String surName;
            private Date birthDate;
            

            public MemberData(String firstName, String surName, Date birthDate) {
                this.firstName = firstName;
                this.surName = surName;
                this.birthDate = birthDate;
            }
}
