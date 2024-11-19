package com.shekhawat.trainbooking.repository;

import com.shekhawat.trainbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.isBooked=false ORDER BY s.rowNumber, s.seatLetter")
    List<Seat> findAvailableSeats();

    @Modifying
    @Query("UPDATE Seat s SET s.isBooked=false")
    void clearAllBookings();

    @Query("SELECT s FROM Seat s WHERE s.rowNumber=?1 AND s.seatLetter=?2")
    Optional<Seat> findByRowNumAndSeatLetter(Integer rowNumber, String seatLetter);
}
