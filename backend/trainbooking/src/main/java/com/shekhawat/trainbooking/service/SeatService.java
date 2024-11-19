package com.shekhawat.trainbooking.service;

import com.shekhawat.trainbooking.entity.Seat;
import com.shekhawat.trainbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Transactional
    public void clearAllBookings() {
        seatRepository.clearAllBookings();
    }

    @Transactional
    public void setInitialBookings(List<String> seatNumbers) {
        for (String seatNumber: seatNumbers) {
            int rowNumber = Integer.parseInt(seatNumber.substring(0, seatNumber.length()-1));
            String seatLetter = seatNumber.substring(seatNumber.length()-1);
            Seat seat = seatRepository.findByRowNumAndSeatLetter(rowNumber, seatLetter)
                    .orElseThrow(() -> new RuntimeException("Invalid seat: " + seatNumber));
            seat.setBooked(true);
            seatRepository.save(seat);
        }
    }

    @Transactional
    public List<Seat> bookSeats(int numOfSeats) {
        if (numOfSeats < 1 || numOfSeats > 7) {
            throw new IllegalArgumentException("Seats should be between 1 and 7");
        }
        List<Seat> seatsToBook = findAvailableSeatsInSameRow(numOfSeats);
        if (seatsToBook.isEmpty()) {
            seatsToBook = findNearestAvailableSeats(numOfSeats);
        }
        if (seatsToBook.isEmpty()) {
            throw new RuntimeException("Not enough seats available");
        }
        seatsToBook.forEach(s -> s.setBooked(true));
        return seatRepository.saveAll(seatsToBook);
    }

    private List<Seat> findNearestAvailableSeats(int numOfSeats) {
        List<Seat> availableSeats = seatRepository.findAvailableSeats();
        if (availableSeats.size() < numOfSeats) {
            return new ArrayList<>();
        }
        Map<Integer, List<Seat>> seatsByRow = new HashMap<>();
        for (Seat seat : availableSeats) {
            seatsByRow.computeIfAbsent(seat.getRowNumber(), k -> new ArrayList<>()).add(seat);
        }

        // Get rows sorted by number
        List<Integer> sortedRows = new ArrayList<>(seatsByRow.keySet());
        Collections.sort(sortedRows);

        List<Seat> result = new ArrayList<>();
        int seatsNeeded = numOfSeats;

        // Try to find seats in nearby rows
        for (int rowNum : sortedRows) {
            List<Seat> rowSeats = seatsByRow.get(rowNum);
            rowSeats.sort((a, b) -> a.getSeatLetter().compareTo(b.getSeatLetter()));

            // Calculate how many seats we can take from this row
            int seatsToTake = Math.min(seatsNeeded, rowSeats.size());

            // Take seats from the beginning of the row
            result.addAll(rowSeats.subList(0, seatsToTake));
            seatsNeeded -= seatsToTake;

            // If we have all needed seats, break
            if (seatsNeeded == 0) {
                break;
            }
        }

        // Return result only if we found all needed seats
        return seatsNeeded == 0 ? result : Collections.emptyList();
    }

    private List<Seat> findAvailableSeatsInSameRow (int numOfSeats) {
        List<Seat> availableSeats = seatRepository.findAvailableSeats();
        List<Seat> result = new ArrayList<>();
        Integer currRow = null;
        for (Seat s: availableSeats) {
            if (currRow == null || !currRow.equals(s.getRowNumber())) {
                currRow = s.getRowNumber();
                result.clear();
            }
            result.add(s);
            if (numOfSeats == result.size()) {
                return result;
            }
        }
        return new ArrayList<>();
    }
}
