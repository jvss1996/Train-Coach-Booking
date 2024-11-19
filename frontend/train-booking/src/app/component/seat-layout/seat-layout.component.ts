import { Component, OnInit } from '@angular/core';
import {Seat} from "../../model/seat.model";
import {SeatService} from "../../services/seat.service";

@Component({
  selector: 'app-seat-layout',
  templateUrl: './seat-layout.component.html',
  styleUrls: ['./seat-layout.component.css']
})
export class SeatLayoutComponent implements OnInit{
  seats: Seat[] = [];
  seatRows: Seat[][] = [];

  constructor(private seatService: SeatService) { }

  ngOnInit() {
    this.loadSeats();
  }

  loadSeats() {
    this.seatService.getAllSeats().subscribe(
      seats => {
        this.seats = seats;
        this.organizeSeats();
      },
    );
  }

  private organizeSeats() {
    this.seatRows = [];
    let currentRow: Seat[] = [];
    let currentRowNumber = 1;
    this.seats.forEach(seat => {
      if (seat.rowNumber !== currentRowNumber) {
        if (currentRow.length > 0) {
          this.seatRows.push(currentRow);
        }
        currentRow = [];
        currentRowNumber = seat.rowNumber;
      }
      currentRow.push(seat);
    });
    if (currentRow.length > 0) {
      this.seatRows.push(currentRow);
    }
  }
}
