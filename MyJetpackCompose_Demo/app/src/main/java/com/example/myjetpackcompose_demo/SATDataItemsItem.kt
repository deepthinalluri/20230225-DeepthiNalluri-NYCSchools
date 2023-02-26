package com.example.myjetpackcompose_demo

data class SATDataItemsItem(
    val dbn: String,
    val num_of_sat_test_takers: String,
    val sat_critical_reading_avg_score: String,
    val sat_math_avg_score: String,
    val sat_writing_avg_score: String,
    val school_name: String
)
data class SchoolDetails(
    val school_name: String ? = null,
    val sat_critical_reading_avg_score: String? = null,
    val sat_math_avg_score: String ? = null,
    val sat_writing_avg_score: String ? = null,
)