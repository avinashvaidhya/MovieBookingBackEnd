package com.moviebooking.backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import dataModel.PreBookingDetails;

@Repository
public class PreBookingCheckRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Map<String,List<PreBookingDetails>> selectExistingBookingsUnderProcess() {
		String SQL = "select * from movie.pre_booking_details"
				+ " where status = \'booked\'";
		
		PreparedStatementCreator psc = con -> {
				return con.prepareStatement(SQL);
		};
		
		Map<String,List<PreBookingDetails>> result = new HashMap<String,List<PreBookingDetails>>();
		ResultSetExtractor<Map<String,List<PreBookingDetails>>> rse = rs -> {
			PreBookingDetails details = new PreBookingDetails();
			details.setBookingId(rs.getInt("booking_id"));
			details.setUserName(rs.getString("user_name"));
			details.setTheatreName(rs.getString("theatre_name"));
			details.setSeatNumber(rs.getInt("seat_number"));
			details.setStatus(rs.getString("status"));
			if(result.get(details.getUserName()) == null)
				result.put(details.getUserName(), new ArrayList<>());
			result.get(details.getUserName()).add(details);
			return result; 
		};
		
		return jdbcTemplate.query(psc, rse);
	}
	
	public int selectMaxBookingId() {
		Integer result = jdbcTemplate.queryForObject("select MAX(booking_id) from movie.pre_booking_check", Integer.class);
		return result == null? 1: result+1;
	}
	
	public int insertData(PreBookingDetails preBookingDetails) {
		String SQL = "Insert into movie.pre_booking_check(booking_id,user_name,"  
				+ "theatre_name,seat_number,status) VALUES (?,?,?,?,'booked')";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setInt(++i, preBookingDetails.getBookingId());
				ps.setString(++i, preBookingDetails.getUserName());
				ps.setString(++i, preBookingDetails.getTheatreName());
				ps.setInt(++i, preBookingDetails.getSeatNumber());
				return ps;
			}
		});
		return 1;
	}
	
	public int deleteSeatDetail(PreBookingDetails detail) {
		String SQL = "Delete from movie.pre_booking_check where seat_number = ?";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setInt(++i, detail.getSeatNumber());
				return ps;
			}
		});
		return 1;
	}
	
	public int deleteForUser(String userName) {
		String SQL = "Delete from movie.pre_booking_check where user_name = ?";
		int count = jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setString(++i, userName);
				return ps;
			}
		});
		return count;
	}
	
	public List<PreBookingDetails> selectUserBookings(String username){

		String SQL = "select * from movie.pre_booking_details"
				+ " where user_name = ?";
		
		PreparedStatementCreator psc = con -> {
				int i =0; 
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setString(++i, username);
				return ps;
		};
		
		List<PreBookingDetails> result = new ArrayList<PreBookingDetails>();
		ResultSetExtractor<List<PreBookingDetails>> rse = rs -> {
			PreBookingDetails details = new PreBookingDetails();
			details.setBookingId(rs.getInt("booking_id"));
			details.setUserName(rs.getString("user_name"));
			details.setTheatreName(rs.getString("theatre_name"));
			details.setSeatNumber(rs.getInt("seat_number"));
			details.setStatus(rs.getString("status"));
			result.add(details);
			return result; 
		};
		
		return jdbcTemplate.query(psc, rse);
	
	}
	
}
