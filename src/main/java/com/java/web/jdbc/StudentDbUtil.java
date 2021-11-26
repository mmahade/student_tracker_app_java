package com.java.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource=theDataSource;
	}
	
	public List<Student> getStudent() throws Exception{
		
		List<Student> students=new ArrayList<>();
		
		//DB Connection variable
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
			myConn=dataSource.getConnection();
			String sql="select * from student order by last_name";
			myStmt=myConn.createStatement();
			myRs=myStmt.executeQuery(sql);
			
			while(myRs.next()) {
				
				int id=myRs.getInt("id");
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				Student tempStudent=new Student(id,firstName,lastName,email);
				students.add(tempStudent);
				
			}
			
			
		}
		finally {
			
			close(myConn,myStmt,myRs);
			
		}
		
		
		return students;
		
	}
	

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if(myRs!=null) {
				myRs.close();
			}
			
			if(myStmt!=null) {
				myStmt.close();
			}
			
			if(myConn!=null) {
				myConn.close();
			}
			
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}

	public void addStudent(Student theStudent) throws Exception {
		// TODO Auto-generated method stub
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
			myConn=dataSource.getConnection();
			
			String sql="insert into student" +
			"(first_name,last_name,email)"+"value(?,?,?)";
			
			myStmt=myConn.prepareStatement(sql);
			
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			myStmt.execute();
			
		}finally {
			close(myConn,myStmt,null);
		}
		
		
	}

	//LOAD A STUDENT
	public Student getStudent(String theStudentId)throws Exception {
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		ResultSet myRs=null;
		int studentId;
		Student theStudent=null;
		
		try {
			
			studentId=Integer.parseInt(theStudentId);
			
			myConn=dataSource.getConnection();
			String sql="select * from student where id=?";
			myStmt=myConn.prepareStatement(sql);
			
			myStmt.setInt(1,studentId);
			
			myRs=myStmt.executeQuery();
			
			if(myRs.next()) {
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				theStudent=new Student(studentId,firstName,lastName,email);
			}
			
			else {
				throw new Exception("Could not find student id:"+studentId);
			}
			
		}finally {
			close(myConn,myStmt,myRs);
		}
		
		
		
		return theStudent;
	}
	
	

	public void updateStudent(Student theStudent)throws Exception {
		// TODO Auto-generated method stub
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
			myConn=dataSource.getConnection();
			
			String sql="UPDATE student "+"SET first_name=?,last_name=?,email=? "+" WHERE id=?";
			
			myStmt=myConn.prepareStatement(sql);
			
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			myStmt.execute();
			
		}finally {
			close(myConn,myStmt,null);
		}
		
	}

	public void deleteStudent(int id)throws Exception {
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		ResultSet myRs=null;
		
		try {
			
			myConn=dataSource.getConnection();
			
			String sql="DELETE from student where id=?";
			
			myStmt=myConn.prepareStatement(sql);
			
			myStmt.setInt(1,id);
			
			myStmt.execute();
			
		}finally {
			close(myConn,myStmt,myRs);
		}
		
	}

	
	

}
