export interface Seat {
  id: number;
  rowNumber: number;
  seatLetter: string;
  booked: boolean;
}

export interface BookingResponse {
  bookedSeats: string[];
  messages: string;
}
