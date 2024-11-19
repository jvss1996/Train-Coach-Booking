import { Component } from '@angular/core';
import { SeatService } from "../../services/seat.service";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.css']
})
export class BookingFormComponent {
  numberOfSeats: number = 1;
  seatsToBook: string = '';

  constructor(private seatService: SeatService, private matSnackBar: MatSnackBar) { }

  async clearAllBookings(): Promise<void> {
    this.seatService.clearAllBookings();
    this.matSnackBar.open("Cleared all bookings", "close", {
      duration: 3000,
    });
    await new Promise(resolve => setTimeout(resolve, 3000));
    window.location.reload();
  }

  bookSeats(): void {
    if (this.numberOfSeats < 1 && this.numberOfSeats > 7) {
      this.matSnackBar.open("Please enter number between 1 and 7", "close", {
        duration: 3000,
      });
      return;
    }
    this.seatService.bookSeats(this.numberOfSeats).subscribe(
      async response => {
        this.matSnackBar.open(`Seats booked successfully: ${response.bookedSeats.join(', ')}`, "close", {
          duration: 3000,
        });
        await new Promise(resolve => setTimeout(resolve, 3000));
        window.location.reload();
      },
      error => {
        this.matSnackBar.open(`Error: ${error.message}`, "close", {
          duration: 3000,
        });
      }
    );
  }

  async setInitialBookings() {
    // Convert input string to array
    const seats = this.seatsToBook
      .split(',')
      .map(seat => seat.trim())
      .filter(seat => seat.length > 0);
    // Check format of input
    const isValidFormat = seats.every(seat =>
      /^\d+[A-G]$/.test(seat) &&
      parseInt(seat.slice(0, -1)) > 0 &&
      parseInt(seat.slice(0, -1)) <= 12
    );
    if (!isValidFormat) {
      this.matSnackBar.open('Invalid seat format. Please use format like "1A,2B,3C"', 'Close', {
        duration: 3000
      });
      return;
    }
    this.seatService.setInitialBookings(seats).subscribe(
      response => {
        console.log(response);
      }
    );
    this.matSnackBar.open("Initial booking done", "close", {
      duration: 3000,
    });
    await new Promise(resolve => setTimeout(resolve, 3000));
    window.location.reload();
  }
}
