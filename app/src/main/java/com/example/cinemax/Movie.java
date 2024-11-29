package com.example.cinemax;

//public class Movie {
//    private String director;
//    private String imageUrl;
//    private double rating;
//    private String title;
//
//    // Default constructor required for calls to DataSnapshot.getValue(Movie.class)
//    public Movie() {}
//
//    public Movie(String director, String imageUrl, double rating, String title) {
//        this.director = director;
//        this.imageUrl = imageUrl;
//        this.rating = rating;
//        this.title = title;
//    }
//
//    // Getters and setters
//    public String getDirector() { return director; }
//    public void setDirector(String director) { this.director = director; }
//
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//
//    public double getRating() { return rating; }
//    public void setRating(double rating) { this.rating = rating; }
//
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//}
public class Movie {
    private String title;
    private String director;
    private String imageUrl;
    private float rating;
    private String id; //12-10 add

    // Default constructor (needed for Firestore deserialization)
    public Movie() {
    }

    public Movie(String id,String title, String director, String imageUrl, float rating) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }
    // Getter for id
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getRating() {  // Ensure getRating returns a float
        return rating;
    }
}



