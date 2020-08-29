package com.moviebooking.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.backend.repository.PreBookingCheckRepository;
import com.moviebooking.backend.repository.TheatreRepository;
import com.moviebooking.backend.repository.UserRepository;

import dataModel.PreBookingDetails;
import dataModel.SelectedSeatDetails;
import dataModel.TheatreDetails;

@RestController
public class TheatreController {

	@Autowired
	TheatreRepository theatreRepository;
	
	@Autowired
	PreBookingCheckRepository preBookingCheckRepository;
	
	@GetMapping(value = "/getSeatDetails/{theatreName}")
	public List<TheatreDetails> getSeatDetails(@PathVariable("theatreName") String theatreName){
		return theatreRepository.getSeatDetails(theatreName);
	}
	
	@PostMapping(value = "/checkSeatAvailability")
	@ResponseBody
	public String checkSeatAvailablity(@RequestBody SelectedSeatDetails selectedSeatDetails) {
		Map<String,List<PreBookingDetails>> existingBookingMap = preBookingCheckRepository
				.selectExistingBookingsUnderProcess();
		if(existingBookingMap.isEmpty()) {
			selectedSeatDetails.getSelectedSeatList().forEach(selectedSeat -> {
				PreBookingDetails preBookingDetails = new PreBookingDetails();
				int bookingId = preBookingCheckRepository.selectMaxBookingId();
				preBookingDetails.setBookingId(bookingId);
				preBookingDetails.setSeatNumber(selectedSeat);
				preBookingDetails.setUserName(selectedSeatDetails.getUserName());
				preBookingDetails.setTheatreName(selectedSeatDetails.getTheatreName());
				preBookingCheckRepository.insertData(preBookingDetails);
			});
			return "success";
		}else {
			boolean isFailure = highestUserCheck(existingBookingMap,selectedSeatDetails);
			return isFailure ? "failure" : "success";
		}
	}
	
	private boolean highestUserCheck(Map<String, List<PreBookingDetails>> existingBookingMap, SelectedSeatDetails selectedSeatDetails) {
		
		List<List<PreBookingDetails>> bookingList = (List<List<PreBookingDetails>>) existingBookingMap.values();
		Map<Integer,List<PreBookingDetails>> checkMap = new HashMap<Integer,List<PreBookingDetails>>();
		for(int i =0; i< bookingList.size();i++) {
			List<PreBookingDetails> userList  = bookingList.get(i);
			for(int j=0;j<userList.size();j++) {
				PreBookingDetails details = userList.get(j);
				checkMap.put(details.getSeatNumber(), userList);
			}
		}
		List<PreBookingDetails> confirmList = new ArrayList<>();
		List<PreBookingDetails> deleteList = new ArrayList<>();
		List<Integer> seatList = selectedSeatDetails.getSelectedSeatList();
		for(int i =0;i<seatList.size();i++) {
			int seatNumber = seatList.get(i);
			List<PreBookingDetails> preBookingList = checkMap.get(seatNumber);
			if(preBookingList == null) {
				confirmList.add(formDetail(seatNumber,selectedSeatDetails));
			}
			else {
				if(preBookingList.size() >= confirmList.size())
					return true;
				else {
					deleteList.addAll(preBookingList);
					confirmList.add(formDetail(seatNumber,selectedSeatDetails));
				}
			}
		}
		deleteList.forEach(deleteDetail ->{
			preBookingCheckRepository.deleteSeatDetail(deleteDetail);
		});
		confirmList.forEach(insertDetail->{
			int bookingId = preBookingCheckRepository.selectMaxBookingId();
			insertDetail.setBookingId(bookingId);
			preBookingCheckRepository.insertData(insertDetail);
		});
		
		return false;
	}
	
	private PreBookingDetails formDetail(int seatNumber,SelectedSeatDetails selectedSeatDetails) {
		PreBookingDetails details= new PreBookingDetails();
		details.setSeatNumber(seatNumber);
		details.setTheatreName(selectedSeatDetails.getTheatreName());
		details.setUserName(selectedSeatDetails.getUserName());
		return details;
	}
}
