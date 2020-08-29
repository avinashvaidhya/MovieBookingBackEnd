package com.moviebooking.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.moviebooking.backend.repository.BookingDetailsRepository;
import com.moviebooking.backend.repository.PreBookingCheckRepository;

import dataModel.BookingDetails;
import dataModel.PaymentDetails;
import dataModel.PreBookingDetails;

@Controller
public class PaymentController {

	@Autowired
	PreBookingCheckRepository preBookingCheckRepository;
	
	@Autowired
	BookingDetailsRepository bookingDetailsRepositoy;
	
	@PostMapping(value = "/proceedToPayment")
	@ResponseBody
	public String makePayment(@RequestBody PaymentDetails paymentDetails) {
		RestTemplate restTemplate = new RestTemplate();
		
		String output = restTemplate.postForEntity("URL for third party payment API",
				paymentDetails, String.class).getBody();
		
		List<PreBookingDetails> prebookingList = preBookingCheckRepository.
				selectUserBookings(paymentDetails.getUserName());
		if(output.equals("success")) {
			List<BookingDetails> bookingDetailsList = formBookingDetails(prebookingList);
			bookingDetailsList.forEach(bookingDetail->{
				int bookingId = bookingDetailsRepositoy.selectMaxBookingId();
				bookingDetail.setBookingId(bookingId);
				bookingDetailsRepositoy.insertData(bookingDetail);
			});
		}
		prebookingList.forEach(preBookingDetail->{
			preBookingCheckRepository.deleteSeatDetail(preBookingDetail);
		});
		return output;
	}
	
	@RequestMapping(value = "/duringPaymentTimeout")
	private int duringPaymentTimeout(@RequestBody String userName) {
		return preBookingCheckRepository.deleteForUser(userName);
	}

	private List<BookingDetails> formBookingDetails(List<PreBookingDetails> prebookingList) {
		List<BookingDetails> resultList = new ArrayList<BookingDetails>();
		prebookingList.forEach(preBookingDetail->{
			BookingDetails detail = new BookingDetails();
			detail.setSeatNumber(preBookingDetail.getSeatNumber());
			detail.setStatus("Booked");
			detail.setTheatreName(preBookingDetail.getTheatreName());
			detail.setUserName(preBookingDetail.getUserName());
		});
		return resultList;
	}
	
}
