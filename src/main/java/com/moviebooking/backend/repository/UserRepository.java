package com.moviebooking.backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import dataModel.UserDetails;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int insertUser(UserDetails userDetails) {
		String SQL = "Insert into movie.user_details(user_id,user_name,"  
				+ "password,email_id,phone_number,role_id) VALUES (?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setInt(++i, userDetails.getUserId());
				ps.setString(++i, userDetails.getUserName());
				ps.setString(++i, userDetails.getPassword());
				ps.setString(++i, userDetails.getEmailId());
				ps.setString(++i, userDetails.getPhoneNumber());
				ps.setInt(++i, userDetails.getRoleId());
				return ps;
			}
		});
		return 1;
	}
	
	public int selectMaxUserId() {
		Integer result = jdbcTemplate.queryForObject("select MAX(user_id) from movie.user_details", Integer.class);
		return result == null? 1: result+1;
	}
	
	public UserDetails selectByUserName(String userName) {
		String SQL = "select * from movie.user_details where user_name = ?";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i = 0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setString(++i, userName);
				return ps;
			}
		};
		
		ResultSetExtractor<UserDetails> rse = new ResultSetExtractor<UserDetails>() {
			
			@Override
			public UserDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
				UserDetails details = new UserDetails();
				details.setUserId(rs.getInt("user_id"));
				details.setUserName(userName);
				details.setPassword(rs.getString("password"));
				details.setPhoneNumber(rs.getString("phone_number"));
				details.setEmailId(rs.getString("email_id"));
				return details;
			}
		};
		return jdbcTemplate.query(psc, rse);
	}

	public boolean selectByUserNameAndPassword(String userName, String password) {
		String SQL = "select * from movie.user_details where user_name = ? and password = ?";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i = 0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setString(++i, userName);
				ps.setString(++i, password);
				return ps;
			}
		};
		
		ResultSetExtractor<Boolean> rse = new ResultSetExtractor<Boolean>() {
			
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next();
			}
		};
		return jdbcTemplate.query(psc, rse);
	}
	
}
