import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { BookingResponse, Seat } from "../model/seat.model";

@Injectable({
  providedIn: 'root'
})
export class SeatService {

  private baseUrl = "http://localhost:8080/seat";

  constructor(private http: HttpClient) { }

  getAllSeats(): Observable<Seat[]> {
    return this.http.get<Seat[]>(`${this.baseUrl}/all`);
  }

  clearAllBookings(): void {
    let response = this.http.delete(`${this.baseUrl}/clear`);
    console.log(response.subscribe());
  }

  setInitialBookings(seats: string[]): Observable<any> {
    return this.http.put(`${this.baseUrl}/set`, seats);
  }

  bookSeats(numberOfSeats: number): Observable<BookingResponse> {
    return this.http.post<BookingResponse>(`${this.baseUrl}/book`, {
      "numOfSeats": numberOfSeats
    });
  }
}
