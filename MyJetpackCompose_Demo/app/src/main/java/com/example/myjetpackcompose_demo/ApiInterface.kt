package com.example.myjetpackcompose_demo

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/resource/s3k6-pzi2.json")
    suspend fun getNYCSchooldata(): Response<List<NYCHighSchoolDataItemsItem>>

    @GET("resource/f9bf-2cp4.json")
    suspend fun getSATdata(): Response<List<SATDataItemsItem>>


}