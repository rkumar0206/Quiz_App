package com.rohitthebest.quizzed_aquizapp.module

import android.content.Context
import androidx.room.Room
import com.rohitthebest.quizzed_aquizapp.Constants.BASE_URL
import com.rohitthebest.quizzed_aquizapp.dataStorage.roomDatabase.database.QuestionDatabase
import com.rohitthebest.quizzed_aquizapp.others.Constants.QUESTION_DATABASE_NAME
import com.rohitthebest.quizzed_aquizapp.remote.apiServices.ApiHelper
import com.rohitthebest.quizzed_aquizapp.remote.apiServices.ApiHelperImplementation
import com.rohitthebest.quizzed_aquizapp.remote.apiServices.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object Module {

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesApiHelper(
        apiHelper: ApiHelperImplementation
    ): ApiHelper = apiHelper


    @Provides
    @Singleton
    fun providesQuestionDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        QuestionDatabase::class.java,
        QUESTION_DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesQuestionDao(
        db: QuestionDatabase
    ) = db.getQuestionDao()

}