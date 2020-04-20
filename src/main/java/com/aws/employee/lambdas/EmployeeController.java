package com.aws.employee.lambdas;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.employee.data.RequestData;
import com.aws.employee.data.ResponseData;


public class EmployeeController implements RequestHandler<RequestData, ResponseData> {

	public ResponseData handleRequest(RequestData input, Context context) {
		ResponseData response = new ResponseData();
		try {
			insertEmployeeDetails(input, response);
		}catch(Exception e) {
			response.setResponseId(String.valueOf(input.getEmpID()));
			response.setStatus("Error");
			response.setResponseMsg("Error, unable to update"+e);
		}
		return response;
	}
	
	private void insertEmployeeDetails(RequestData request, ResponseData response) throws SQLException{
		Connection conn= getConnection();
		Statement statement = conn.createStatement();
		int status = 0;
		try {
			status = statement.executeUpdate(fetchQuery(request));
		}catch(SQLException e) {
			response.setResponseId(String.valueOf(request.getEmpID()));
			response.setStatus("Error");
			response.setResponseMsg("Unable to insert "+e);
		}
		
		if(1 == status) {
			response.setResponseId(String.valueOf(request.getEmpID()));
			response.setStatus("success");
			response.setResponseMsg("Successfully updated employee details");
		}
	}
	private String fetchQuery(RequestData request) {
		String query = "insert into raviezofficial.employee(empid,empname,salary,desg) values (";
		if(request!=null) {
			query = query.concat(request.getEmpID()+",'"+request.getEmployeeName()+"',"+request.getSalary()+",'"+request.getDesignation()+"')");
		}
		System.out.print(query);
		return query;
	}
	
	
	private Connection getConnection() throws SQLException{
		String url = "jdbc:mysql://raviezofficial-db.cphnytg2n7lj.ap-south-1.rds.amazonaws.com:3306";
		String userName = "admin";
		String password = "*******";
		Connection conn = DriverManager.getConnection(url, userName, password);
		return conn;
		
	}

}
