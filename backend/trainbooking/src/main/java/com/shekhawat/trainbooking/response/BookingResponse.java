package com.shekhawat.trainbooking.response;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponse {

    private List<String> bookedSeats;
    private String message;
}
