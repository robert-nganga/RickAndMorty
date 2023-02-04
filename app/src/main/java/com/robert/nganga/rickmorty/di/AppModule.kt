package com.robert.nganga.rickmorty.di

import android.content.Context
import androidx.room.Room
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.repository.RickMortyRepository
import com.robert.nganga.rickmorty.repository.RickMortyRepositoryImpl
import com.robert.nganga.rickmorty.utils.Constants.BASE_URL
import com.robert.nganga.rickmorty.utils.Constants.CHARACTER_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesApi(): RickMortyAPI {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RickMortyAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCharacterDatabase(
        @ApplicationContext context: Context
        ) = Room.databaseBuilder(context, CharacterDatabase::class.java, CHARACTER_DB).build()


    @Provides
    @Singleton
    fun provideRickMortyRepository(api: RickMortyAPI): RickMortyRepository{
        return RickMortyRepositoryImpl(api)
    }
}