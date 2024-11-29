//package com.example.cinemax;
//
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ImageView;
////import android.widget.TextView;
////import androidx.annotation.NonNull;
////import androidx.recyclerview.widget.RecyclerView;
////import com.bumptech.glide.Glide;
////import java.util.List;
////
////public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
////
////    private List<Movie> movieList;
////
////    public MovieAdapter(List<Movie> movieList) {
////        this.movieList = movieList;
////    }
////
////    @NonNull
////    @Override
////    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_item, parent, false);
////        return new MovieViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
////        Movie movie = movieList.get(position);
////        holder.titleTextView.setText(movie.getTitle());
////        holder.directorTextView.setText(movie.getDirector());
////        holder.ratingTextView.setText("Rating: " + movie.getRating());
////
////        // Load image using Glide
////        Glide.with(holder.itemView.getContext())
////                .load(movie.getImageUrl())
////                .into(holder.imageView);
////    }
////
////    @Override
////    public int getItemCount() {
////        return movieList.size();
////    }
////
////    public static class MovieViewHolder extends RecyclerView.ViewHolder {
////        ImageView imageView;
////        TextView titleTextView;
////        TextView directorTextView;
////        TextView ratingTextView;
////
////        public MovieViewHolder(@NonNull View itemView) {
////            super(itemView);
////            imageView = itemView.findViewById(R.id.movieImage);
////            titleTextView = itemView.findViewById(R.id.movieTitle);
////            directorTextView = itemView.findViewById(R.id.movieDirector);
////            ratingTextView = itemView.findViewById(R.id.movieRating);
////        }
////    }
////}
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.LayerDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import java.util.List;
//
//public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
//
//    private List<Movie> movieList;
//
//    public MovieAdapter(List<Movie> movieList) {
//        this.movieList = movieList;
//    }
//
//    @NonNull
//    @Override
//    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_item, parent, false);
//        return new MovieViewHolder(view);
//    }
//
////    @Override
////    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
////        Movie movie = movieList.get(position);
////        holder.titleTextView.setText(movie.getTitle());
////        holder.directorTextView.setText(movie.getDirector());
////
////        // Set movie rating on the RatingBar
////        holder.ratingBar.setRating(movie.getRating());  // Assuming getRating() returns a float between 0 and 5
////        // Load image using Glide
////        Glide.with(holder.itemView.getContext())
////                .load(movie.getImageUrl())
////                .into(holder.imageView);
////    }
//@Override
//public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
//    Movie movie = movieList.get(position);
//    holder.titleTextView.setText(movie.getTitle());
//    holder.directorTextView.setText(movie.getDirector());
//
//    // Set movie rating on the RatingBar
//    holder.ratingBar.setRating(movie.getRating());
//
//    // Load image using Glide
//    Glide.with(holder.itemView.getContext())
//            .load(movie.getImageUrl())
//            .into(holder.imageView);
//
//    // Set OnClickListener for the "Book" button
//    holder.bookButton.setOnClickListener(view -> {
//        Context context = view.getContext();
//        Intent intent = new Intent(context, seat_layout.class);
//        intent.putExtra("movieId", movie.getId());  // Passing movie ID to next activity
//        context.startActivity(intent);
//    });
//}
//
//
//    @Override
//    public int getItemCount() {
//        return movieList.size();
//    }
//
//    public static class MovieViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView titleTextView;
//        TextView directorTextView;
//        RatingBar ratingBar;
//
//        public MovieViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.movieImage);
//            titleTextView = itemView.findViewById(R.id.movieTitle);
//            directorTextView = itemView.findViewById(R.id.movieDirector);
//            ratingBar = itemView.findViewById(R.id.ratingBar);
//
//        }
//    }
//}
package com.example.cinemax;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.directorTextView.setText(movie.getDirector());

        // Set movie rating on the RatingBar
        holder.ratingBar.setRating(movie.getRating());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(movie.getImageUrl())
                .into(holder.imageView);

        // Set OnClickListener for the "Book" button
        holder.bookButton.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, seat_layout.class);
            intent.putExtra("movieName", movie.getTitle());

//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Pass movie ID to next activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView directorTextView;
        RatingBar ratingBar;
        Button bookButton;  // Add button here

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieImage);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            directorTextView = itemView.findViewById(R.id.movieDirector);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            bookButton = itemView.findViewById(R.id.bookButton);  // Initialize button
        }
    }
}
