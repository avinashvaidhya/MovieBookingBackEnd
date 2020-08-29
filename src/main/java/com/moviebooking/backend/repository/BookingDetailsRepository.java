package com.moviebooking.backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import dataModel.BookingDetails;
import dataModel.PreBookingDetails;

@Repository
public class BookingDetailsRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int selectMaxBookingId() {
		Integer result = jdbcTemplate.queryForObject("select MAX(booking_id) from movie.booking_details", Integer.class);
		return result == null? 1: result+1;
	}
	
	public int insertData(BookingDetails bookingDetails) {
		String SQL = "Insert into movie.booking_details(booking_id,user_name,"  
				+ "theatre_name,seat_number,status) VALUES (?,?,?,?,'booked')";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setInt(++i, bookingDetails.getBookingId());
				ps.setString(++i, bookingDetails.getUserName());
				ps.setString(++i, bookingDetails.getTheatreName());
				ps.setInt(++i, bookingDetails.getSeatNumber());
				return ps;
			}
		});
		return 1;
	}
	
}
