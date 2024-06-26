package com.jude.advancedmovie.paging;

import static kotlinx.coroutines.flow.FlowKt.onErrorReturn;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.jude.advancedmovie.api.APIclient;
import com.jude.advancedmovie.model.Movie;
import com.jude.advancedmovie.model.MovieResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie> {
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            int page = loadParams.getKey() != null ? loadParams.getKey() : 1;

            return APIclient.getApiInterface()
                    .getMoviesByPage(page)
                    .subscribeOn(Schedulers.io())
                    .map(MovieResponse::getMovies)
                    .map(movies -> toLoadResult(movies, page))
                    .onErrorReturn(LoadResult.Error::new);
        }
        catch (Exception e) {
            return Single.just(new LoadResult.Error(e));
        }
    }

    public LoadResult<Integer,Movie> toLoadResult(List<Movie> movies, int page){
        return new LoadResult.Page(movies,page==1 ? null:page-1,page+1);
        }
    }

