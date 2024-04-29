package com.jude.advancedmovie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.jude.advancedmovie.databinding.MovieItemListBinding;
import com.jude.advancedmovie.model.Movie;

import kotlinx.coroutines.CoroutineDispatcher;

public class MoviesAdapter extends PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder> {

    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 1;

    RequestManager glide;

    public MoviesAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback, RequestManager glide) {
        super(diffCallback);
        this.glide = glide;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(MovieItemListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // getting item from paging Data Adapter
        Movie currentMovie = getItem(position);
        if(currentMovie != null){
            glide.load("https://image.tmdb.org/t/p/w500"+currentMovie.getPosterPath())
                                                     .into(holder.movieItemListBinding.imageViewMovie);
            holder.movieItemListBinding.textViewRating.setText(String.valueOf(currentMovie.getVoteAverage()));

        }

    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() ? MOVIE_ITEM:LOADING_ITEM;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        MovieItemListBinding movieItemListBinding;

        public MovieViewHolder(@NonNull MovieItemListBinding movieItemListBinding) {
            super(movieItemListBinding.getRoot());
            this.movieItemListBinding = movieItemListBinding;
        }

    }
}
