package com.example.movies.di

import com.example.movies.data.MovieRepository
import com.example.movies.net.HttpClient
import com.example.movies.net.Webservice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Singleton
    @Provides
    fun provideWebservice(): Webservice {
        return HttpClient.getService()
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        webservice: Webservice,
    ): MovieRepository {
        return MovieRepository(webservice)
    }
}