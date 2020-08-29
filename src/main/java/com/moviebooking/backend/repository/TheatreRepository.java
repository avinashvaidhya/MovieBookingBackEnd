package com.moviebooking.backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import dataModel.TheatreDetails;

@Repository
public class TheatreRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<TheatreDetails> getSeatDetails(String theatreName){
		String SQL = "select * from movie.theatre_details where theatre_name = ?";
		PreparedStatementCreator psc = con -> {
				int i=0;
				PreparedStatement ps = con.prepareStatement(SQL);
				ps.setString(++i, theatreName);
				return ps;
		};

		ResultSetExtractor<List<TheatreDetails>> rse = rs -> {
				List<TheatreDetails> list = new ArrayList<>();
				while(rs.next()) {
					TheatreDetails detail = new TheatreDetails();
					detail.setTheatreName(theatreName);
					detail.setSeatNumber(rs.getInt("seat_number"));
					detail.setStatus(rs.getString("status"));
					list.add(detail);
				}
				return list;
		};

		List<TheatreDetails> list = jdbcTemplate.query(psc, rse);
		return list;
	}

}
