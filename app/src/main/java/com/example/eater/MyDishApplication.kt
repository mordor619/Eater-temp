package com.example.eater

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


class MyDishApplication: Application() {
    public lateinit var httpApiService: DishesHttpApiService

    override fun onCreate() {
        super.onCreate()

        httpApiService = initHttpApiService()
    }

    private fun initHttpApiService(): DishesHttpApiService {
        val token = "842e637d-fb7c-4a04-a481-d6bed194af85"

        val loggingInterceptor = HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(loggingInterceptor)          //gives details about connection
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://android-kanini-course.cloud/")
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
            .build()

        return retrofit.create(DishesHttpApiService::class.java)

    }
}