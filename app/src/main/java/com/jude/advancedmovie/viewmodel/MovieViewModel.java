package com.jude.advancedmovie.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.jude.advancedmovie.model.Movie;
import com.jude.advancedmovie.paging.MoviePagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieViewModel extends ViewModel {


    public Flowable<PagingData<Movie>> MoviePagingDataFlowable;

    public MovieViewModel(){
        init();
    }

    public void init(){
        MoviePagingSource moviePagingSource = new MoviePagingSource();

        Pager<Integer,Movie> pager = new Pager(
                new PagingConfig(
                        20,
                        20,
                        false,
                        20,
                        20*499
                ),
                () -> moviePagingSource);

        MoviePagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(MoviePagingDataFlowable, coroutineScope);

    }
}
