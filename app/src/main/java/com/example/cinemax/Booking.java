package com.example.cinemax;

import java.util.List;

public class Booking {

    public String movie;
    public String date;
    public String time;
    public List<String> seats;
    public double price;

    // Default constructor required for Firebase calls to DataSnapshot.getValue(Booking.class)
    public Booking() {
    }

    public Booking(String movie, String date, String time, List<String> seats, double price) {
        this.movie = movie;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.price = price;
    }
}
