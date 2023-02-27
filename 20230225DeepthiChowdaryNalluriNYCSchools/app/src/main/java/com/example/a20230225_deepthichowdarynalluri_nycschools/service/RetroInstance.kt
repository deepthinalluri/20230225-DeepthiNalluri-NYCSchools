package com.example.a20230225_deepthichowdarynalluri_nycschools

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {

    companion object{ // creating retrofit instance to make network call
        val baseurl ="https://data.cityofnewyork.us"

        fun getRetroInstance():ApiInterface{
            val retroInstance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseurl)
                .build()
                .create(ApiInterface::class.java)

            return retroInstance
        }
    }
}
