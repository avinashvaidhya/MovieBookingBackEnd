package com.moviebooking.backend.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dataModel.UserDetails;

@Repository
public class ValidationRepository {

	public Map<String,ArrayList<String>> validateUser(UserDetails userDetails) {
		Map<String ,ArrayList<String>> errorMap = new HashMap<>();
		errorMap.put("errors", new ArrayList<>());
		// TODO validation conditions for the user
		if(false) {
				errorMap.get("errors").add("errors");
		}
		return errorMap;
	}
	
	public Map<String,ArrayList<String>> validateUserDetails(UserDetails userDetails) {
		Map<String ,ArrayList<String>> errorMap = new HashMap<>();
		errorMap.put("errors", new ArrayList<>());
		// TODO validation conditions for the user
		if(false) {
				errorMap.get("errors").add("errors");
		}
		return errorMap;
	}
	
}

