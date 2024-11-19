package com.shekhawat.trainbooking.controller;

import com.shekhawat.trainbooking.entity.Seat;
import com.shekhawat.trainbooking.request.BookingRequest;
import com.shekhawat.trainbooking.response.BookingResponse;
import com.shekhawat.trainbooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seat")
@CrossOrigin(origins = "http://localhost:4200")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/all")
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllBookings() {
        seatService.clearAllBookings();
        return ResponseEntity.ok("Seats cleared");
    }

    @PutMapping("/set")
    public ResponseEntity<String> setInitialBookings(@RequestBody List<String> seatNumbers) {
        try {
            seatService.setInitialBookings(seatNumbers);
            return ResponseEntity.ok("Initial booking done");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error while initial booking: " + ex.getMessage());
        }
    }

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookSeats(@RequestBody BookingRequest bookingRequest) {
        try {
            List<Seat> bookedSeats = seatService.bookSeats(bookingRequest.getNumOfSeats());
            BookingResponse response = new BookingResponse();
            response.setBookedSeats(bookedSeats.stream()
                    .map(s -> s.getRowNumber() + s.getSeatLetter()).collect(Collectors.toList()));
            response.setMessage("Booking successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            BookingResponse response = new BookingResponse();
            response.setMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException ex) {
            BookingResponse response = new BookingResponse();
            response.setMessage("Unable to book seats: " + ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
