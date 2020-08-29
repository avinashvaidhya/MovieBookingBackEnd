package com.moviebooking.backend.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.backend.repository.UserRepository;
import com.moviebooking.backend.repository.ValidationRepository;

import dataModel.UserDetails;
import lombok.val;

@RestController
public class UserDetailsController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ValidationRepository validationRepository;
	
	@GetMapping("/index")
	@ResponseBody
	public String index() {
		return "success";
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String authCheck() {
		return "success";
	}
	
	@PostMapping("/registerUser")
	@ResponseBody
	public Map<String ,ArrayList<String>> registerUser(@RequestBody UserDetails userDetails) {
		Map<String ,ArrayList<String>> errorMap = validationRepository.validateUser(userDetails);
		Integer userId = userRepository.selectMaxUserId();
		userDetails.setUserId(userId);
		userRepository.insertUser(userDetails);
		return errorMap;
	}
	
	@PostMapping("/registerUser")
	@ResponseBody
	public String checkUserDetails(@RequestBody UserDetails userDetails) {
		Map<String ,ArrayList<String>> errorMap = validationRepository.validateUserDetails(userDetails);
		boolean isUserPresent = userRepository.selectByUserNameAndPassword(userDetails.getUserName(),
				userDetails.getPassword());
		return isUserPresent ? "success" : "error";
	}
	
}
