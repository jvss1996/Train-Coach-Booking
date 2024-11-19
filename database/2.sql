CREATE TABLE seats (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  row_numb INT NOT NULL,
  seat_letter CHAR(1) NOT NULL,
  is_booked BOOLEAN DEFAULT FALSE,
  UNIQUE KEY unique_seat_position (row_numb, seat_letter)
)
