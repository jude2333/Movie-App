package com.jude.advancedmovie;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.RequestManager;
import com.jude.advancedmovie.adapter.MovieLoadStateAdapter;
import com.jude.advancedmovie.adapter.MoviesAdapter;
import com.jude.advancedmovie.databinding.ActivityMainBinding;
import com.jude.advancedmovie.utils.GridSpace;
import com.jude.advancedmovie.utils.MovieComparator;
import com.jude.advancedmovie.viewmodel.MovieViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    MovieViewModel mainActivityViewModel;
    ActivityMainBinding activityMainBinding;
    MoviesAdapter moviesAdapter;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and set the content View
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
       // "getRoot()" is the parent View that contains all other view inside it(ex linear layout)
        setContentView(activityMainBinding.getRoot());

        moviesAdapter = new MoviesAdapter(new MovieComparator(), requestManager);
        // setting view model
        mainActivityViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        // intialize recycler and adpter
        initRecyclerviewAndAdapter();

        mainActivityViewModel.MoviePagingDataFlowable.subscribe(
                moviePagingData -> {moviesAdapter.submitData(getLifecycle(), moviePagingData);
        });



    }

    private void initRecyclerviewAndAdapter() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        activityMainBinding.recyclerViewMovies.setLayoutManager(gridLayoutManager);

        activityMainBinding.recyclerViewMovies.addItemDecoration(new GridSpace(2,20,true));
        // Set adapter for RecyclerView with load state footer
        activityMainBinding.recyclerViewMovies.setAdapter(moviesAdapter.withLoadStateFooter(
                new MovieLoadStateAdapter(view -> {
                    moviesAdapter.retry();
                })
        ));

        // Set span size lookup for GridLayoutManager
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return moviesAdapter.getItemViewType(position) == MoviesAdapter.LOADING_ITEM ? 1 : 2;
            }
        });

    }
}