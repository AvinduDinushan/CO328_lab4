/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.pdn.co328.studentSystem.dbimplementation;

import java.sql.*;
import java.util.ArrayList;
import lk.ac.pdn.co328.studentSystem.Student;
import lk.ac.pdn.co328.studentSystem.StudentRegister;
import java.lang.*;

/**
 *
 * @author himesh
 */
public class DerbyStudentRegister extends StudentRegister {

    Connection connection = null;
    public DerbyStudentRegister() //throws SQLException
    {
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            String dbURL1 = "jdbc:derby:codejava/studentDB;create=true";
            connection = DriverManager.getConnection(dbURL1);
            if (connection != null) {
                String SQL_CreateTable = "CREATE TABLE StudentsNew(id INT , first_name VARCHAR(24),last_name VARCHAR(40))";
                System.out.println("Creating table addresses...");
                try {
                    Statement stat = connection.createStatement();
                    stat.execute(SQL_CreateTable);
                    stat.close();
                    System.out.println("Table created");
                } catch (SQLException e) {
                    System.out.println(e);
                }
                System.out.println("Connected to database");
            } else {
                throw new SQLException("Connection Failed");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void addStudent(Student st) throws Exception {
        if (connection != null)
        {
            Student tempStudent = null;
            Statement stat;
            String sql;

            tempStudent = findStudent(st.getId());

            if(tempStudent!=null){
                sql = "UPDATE StudentsNew SET first_name = '" + st.getFirstName() +"' last_name = '" + st.getLastName() + "' WHERE id = "+st.getId();
                stat = connection.createStatement();
                stat.execute(sql);
                stat.close();
            }else{
                sql = "INSERT INTO StudentsNew VALUES(" + st.getId() + ",'" + st.getFirstName() + "','" + st.getLastName() + "')";
                stat = connection.createStatement();
                stat.execute(sql);
                stat.close();
            }


        }
        else
        {
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public void removeStudent(int regNo) throws Exception{
        if(connection != null){
            String sql = "DELETE FROM StudentsNew WHERE id = " +regNo;
            Statement stat = connection.createStatement();
            stat.execute(sql);
            stat.close();
        }else{
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public Student findStudent(int regNo) throws Exception{
        if(connection != null){
            String sql = "SELECT * FROM StudentsNew WHERE id = " + regNo;
            Statement stat = connection.createStatement();
            ResultSet resultStudents = stat.executeQuery(sql);
            Student temp = null;
            if(resultStudents.next()){
                temp = new Student(resultStudents.getInt(1), resultStudents.getString(2), resultStudents.getString(3));
            }
            stat.close();
            return temp;
        }else{
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public void reset() throws Exception{
        if(connection != null){
            String sql = "DELETE FROM StudentsNew";
            Statement stat = connection.createStatement();
            stat.execute(sql);
            stat.close();
        }else{
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public ArrayList<Student> findStudentsByName(String name) throws Exception{
        ArrayList<Student> students = new ArrayList<Student>();
        if(connection != null){
            String sql = "SELECT * FROM StudentsNew WHERE first_name = " + name +"OR last_name =" + name;
            Statement stat = connection.createStatement();
            ResultSet resultStudents = stat.executeQuery(sql);
            while(resultStudents.next()){
                students.add(new Student(resultStudents.getInt(1),resultStudents.getString(2),resultStudents.getString(3)));
            }
            stat.close();
        }else{
            throw new Exception("Database Connection Error");
        }
        return students;
    }

    @Override
    public ArrayList<Integer> getAllRegistrationNumbers() throws Exception{
        ArrayList<Integer> students = new ArrayList<Integer>();
        if(connection != null){
            String sql = "SELECT id FROM StudentsNew";
            Statement stat = connection.createStatement();
            ResultSet resultStudent = stat.executeQuery(sql);
            while(resultStudent.next()) students.add(resultStudent.getInt(1));
            stat.close();
        }else{
            throw new Exception("Database Connection Error");
        }
        return students;
    }
    
}
