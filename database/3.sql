CREATE PROCEDURE populate_seats()
BEGIN
  DECLARE row_num INT DEFAULT 1;
  DECLARE max_rows INT DEFAULT 12;  -- 11 rows of 7 seats + 1 row of 3 seats
  DECLARE seat_char CHAR(1);
  DECLARE seat_count INT;
  populate_rows: LOOP
    IF row_num > max_rows THEN
      LEAVE populate_rows;
    END IF;

    -- Set number of seats in current row
    IF row_num = max_rows THEN
      SET seat_count = 3;  -- Last row has 3 seats
    ELSE
      SET seat_count = 7;  -- All other rows have 7 seats
    END IF;

    -- Insert seats for current row
    SET seat_char = 'A';
    seats_in_row: LOOP
      IF seat_char > CHAR(ASCII('A') + seat_count - 1) THEN
        LEAVE seats_in_row;
      END IF;
      INSERT INTO seats (row_numb, seat_letter, is_booked)
      VALUES (row_num, seat_char, FALSE);
      SET seat_char = CHAR(ASCII(seat_char) + 1);
    END LOOP seats_in_row;
    SET row_num = row_num + 1;
  END LOOP populate_rows; 
END

-- Execute the procedure to populate seats
CALL populate_seats()
