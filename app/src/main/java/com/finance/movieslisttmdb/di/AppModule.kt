package com.finance.movieslisttmdb.di

import android.content.Context
import androidx.room.Room
import com.custom.networksdk.RequestRepository
import com.custom.networksdk.RequestViewModel
import com.finance.movieslisttmdb.AppDatabase
import com.finance.movieslisttmdb.repository.GenreRepository
import com.finance.movieslisttmdb.repository.MovieRepository
import com.finance.movieslisttmdb.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieRepository(requestViewModel: RequestViewModel): MovieRepository {
        return MovieRepositoryImpl(requestViewModel)
    }

    @Singleton
    @Provides
    fun provideRequestViewModel(requestRepository: RequestRepository): RequestViewModel {
        return RequestViewModel(requestRepository)
    }

    @Singleton
    @Provides
    fun provideHelper(movieRepository: MovieRepository): Helper {
        return Helper(movieRepository)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "genres").build()
    }

    @Singleton
    @Provides
    fun provideGenreRepository(
        movieRepository: MovieRepository,
        appDatabase: AppDatabase
    ): GenreRepository {
        return GenreRepository(movieRepository, appDatabase)
    }
}