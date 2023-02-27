package com.example.a20230225_deepthichowdarynalluri_nycschools

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/resource/s3k6-pzi2.json") // get  school names
    suspend fun getNYCSchooldata(): Response<List<NYCHighSchoolItems>>

    @GET("resource/f9bf-2cp4.json") // get schools SAT scores
    suspend fun getSATdata(): Response<List<SATDataItemsItem>>

}