package com.shekhawat.trainbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "row_numb")
    private int rowNumber;

    @Column(name = "seat_letter")
    private String seatLetter;

    @Column(name = "is_booked")
    private boolean isBooked = false;
}
