package com.example.cinemax;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class seat_layout extends AppCompatActivity {

    private double totalPrice = 0.0;
    private final double seatPrice = 5.55;
    private TextView totalTextView, dateTextView;
    private String selectedDate = "20/09/2024";
    private String selectedTime = "10:00 AM"; // Default time
    private ArrayList<String> selectedSeats = new ArrayList<>();
    private GridLayout groupLayout;

    private DatabaseReference bookingsReference;
    private DatabaseReference seatsReference;

    private String movieName; // To hold the current movie name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_layout);

        // Get the movie name from the previous activity
        movieName = getIntent().getStringExtra("movieName");

        if (movieName == null) {
            Toast.makeText(this, "Movie name is missing!", Toast.LENGTH_SHORT).show();
            finish(); // Exit if no movie name is passed
            return;
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bookingsReference = database.getReference("Bookings").child(movieName);
        seatsReference = database.getReference("Seats").child(movieName);

        groupLayout = findViewById(R.id.groupGridLayout);
        totalTextView = findViewById(R.id.textViewTotal);
        dateTextView = findViewById(R.id.textViewDate);
        Button bookButton = findViewById(R.id.buttonBook);

        // Set up time options dynamically
        GridLayout timeGridLayout = findViewById(R.id.timeGridLayout);
        String[] timeOptions = {
                "10:00 AM", "12:00 PM", "2:00 PM",
                "4:00 PM", "6:00 PM", "8:00 PM"
        };

        timeGridLayout.setRowCount(2);
        timeGridLayout.setColumnCount(3);

        for (String time : timeOptions) {
            TextView timeBox = new TextView(this);
            timeBox.setText(time);
            timeBox.setPadding(16, 16, 16, 16);
            timeBox.setTextSize(16);
            timeBox.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            timeBox.setBackground(ContextCompat.getDrawable(this, R.drawable.box_selector_background));
            timeBox.setClickable(true);
            timeBox.setFocusable(true);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);

            timeGridLayout.addView(timeBox, params);

            timeBox.setOnClickListener(v -> selectTime(time));
        }

        // Book button click listener
        bookButton.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(seat_layout.this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }

            String bookingId = bookingsReference.push().getKey();
            if (bookingId != null) {
                Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("date", selectedDate);
                bookingData.put("time", selectedTime);
                bookingData.put("userId", "userId1"); // Replace with actual user ID
                bookingData.put("seats", new HashMap<String, Boolean>());

                // Add selected seats to booking data
                for (String seatId : selectedSeats) {
                    ((HashMap<String, Boolean>) bookingData.get("seats")).put(seatId, true);
                }

                bookingsReference.child(bookingId).setValue(bookingData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully saved booking, now update seats
                        updateSeatAvailability();
                        finish();
                    } else {
                        Toast.makeText(seat_layout.this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dateTextView.setOnClickListener(v -> showDatePicker());

        // Set seat layout dynamically
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        int seatSize = screenWidth / 13;
        int seatMargin = screenWidth / 380;
        int groupMargin = screenWidth / 30;

        int numberOfGroupRows = 2;
        int numberOfGroupColumns = 2;
        int numberOfRows = 5;
        int numberOfColumns = 5;

        for (int i = 0; i < numberOfGroupRows; i++) {
            for (int j = 0; j < numberOfGroupColumns; j++) {
                GridLayout seatGroup = new GridLayout(this);
                seatGroup.setRowCount(numberOfRows);
                seatGroup.setColumnCount(numberOfColumns);

                GridLayout.LayoutParams groupParams = new GridLayout.LayoutParams();
                groupParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
                groupParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                groupParams.setMargins(groupMargin, groupMargin, groupMargin, groupMargin);
                seatGroup.setLayoutParams(groupParams);

                for (int m = 0; m < numberOfRows; m++) {
                    for (int n = 0; n < numberOfColumns; n++) {
                        final Button button = new Button(this);
                        String seatId = i + "_" + j + "_" + m + "_" + n; // Generate unique seatId for each seat

                        button.setId(View.generateViewId());
                        button.setTag(R.id.seat_id, seatId); // Store seatId in the tag
                        button.setTag(R.id.is_selected, false); // Initially, mark seat as not selected

                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.seat_available));

                        GridLayout.LayoutParams seatParams = new GridLayout.LayoutParams();
                        seatParams.width = seatSize;
                        seatParams.height = seatSize;
                        seatParams.setMargins(seatMargin, seatMargin, seatMargin, seatMargin);
                        button.setLayoutParams(seatParams);

                        button.setOnClickListener(v -> {
                            boolean isSelected = (boolean) button.getTag(R.id.is_selected);
                            if (!isSelected) {
                                button.setBackground(ContextCompat.getDrawable(seat_layout.this, R.drawable.seat_booked));
                                totalPrice += seatPrice;
                                button.setTag(R.id.is_selected, true); // Mark as selected
                                selectedSeats.add(seatId);
                            } else {
                                button.setBackground(ContextCompat.getDrawable(seat_layout.this, R.drawable.seat_available));
                                totalPrice -= seatPrice;
                                button.setTag(R.id.is_selected, false); // Mark as unselected
                                selectedSeats.remove(seatId);
                            }
                            updateTotalPrice();
                        });

                        seatGroup.addView(button);
                    }
                }

                groupLayout.addView(seatGroup);
            }
        }

        loadSeatAvailability(); // Load the seat status from Firebase for this movie
    }

    private void selectTime(String time) {
        selectedTime = time;

        GridLayout timeGridLayout = findViewById(R.id.timeGridLayout);
        for (int i = 0; i < timeGridLayout.getChildCount(); i++) {
            TextView timeBox = (TextView) timeGridLayout.getChildAt(i);
            timeBox.setBackground(ContextCompat.getDrawable(this, R.drawable.box_selector_background));
        }

        for (int i = 0; i < timeGridLayout.getChildCount(); i++) {
            TextView timeBox = (TextView) timeGridLayout.getChildAt(i);
            if (time.equals(timeBox.getText().toString())) {
                timeBox.setBackground(ContextCompat.getDrawable(this, R.drawable.box_selected_background));
                break;
            }
        }
    }

    private void updateSeatAvailability() {
        for (String seatId : selectedSeats) {
            seatsReference.child(seatId).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firebase", "Seat " + seatId + " booked successfully.");

                    // Update the seat's appearance immediately after booking
                    Button seatButton = findSeatButton(seatId);
                    if (seatButton != null) {
                        seatButton.setBackground(ContextCompat.getDrawable(seat_layout.this, R.drawable.seat_booked_green));
                        seatButton.setClickable(false); // Disable further clicks
                        seatButton.setTag(R.id.is_selected, true); // Mark it as booked
                    }
                } else {
                    Log.e("Firebase", "Failed to book seat " + seatId, task.getException());
                }
            });
        }

        // Reset total price and selected seats
        selectedSeats.clear();
        totalPrice = 0.0;
        updateTotalPrice(); // Reset total price to 0
        Toast.makeText(seat_layout.this, "Booking successful!", Toast.LENGTH_SHORT).show();
    }

    private void loadSeatAvailability() {
        seatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                    String seatId = seatSnapshot.getKey();
                    boolean isBooked = seatSnapshot.getValue(Boolean.class);

                    if (isBooked) {
                        Button seatButton = findSeatButton(seatId);
                        if (seatButton != null) {
                            seatButton.setBackground(ContextCompat.getDrawable(seat_layout.this, R.drawable.seat_booked_green));
                            seatButton.setClickable(false); // Disable further clicks
                            seatButton.setTag(R.id.is_selected, true); // Mark it as booked
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to load seat availability", databaseError.toException());
            }
        });
    }

    private Button findSeatButton(String seatId) {
        // Find the seat button by seatId within the seat layout
        for (int i = 0; i < groupLayout.getChildCount(); i++) {
            GridLayout seatGroup = (GridLayout) groupLayout.getChildAt(i);
            for (int j = 0; j < seatGroup.getChildCount(); j++) {
                Button seatButton = (Button) seatGroup.getChildAt(j);
                if (seatId.equals(seatButton.getTag(R.id.seat_id))) {
                    return seatButton;
                }
            }
        }
        return null; // Seat not found
    }

    private void updateTotalPrice() {
        totalTextView.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                seat_layout.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth++; // DatePicker month starts from 0
                    selectedDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    dateTextView.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }
}
