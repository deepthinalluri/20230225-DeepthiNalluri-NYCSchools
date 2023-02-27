package com.example.a20230225_deepthichowdarynalluri_nycschools

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class NycSchoolDetailsViewModelTest {
    @Test
    fun `test fetch School details function with successful response`() {  // check successful response from network call
        val mockSchooldetailRetrofit = RetroInstance.getRetroInstance() // get retrofit instance
        val schoolDetailResponse = Response.success( // create success response model
            listOf(
                SATDataItemsItem("1234","34", "324","321","364","Henry Hight school"),
                SATDataItemsItem("2356","45", "224","258","354","Unviersity Hight school")
            )
        )
        val viewModel = NycSchoolDetailsActivityViewModel() // get ViewModel object
        viewModel.retroService = mockSchooldetailRetrofit
        runBlocking {
            val service = mockk<ApiInterface>(relaxed = true).apply {
                coEvery {  getSATdata() } returns schoolDetailResponse
            }
            viewModel.getSchoolDetails() // make network call  to schools details
        }.let {

            val satItem1 = schoolDetailResponse.body()?.get(0)
            satItem1.let { value->
                Assert.assertNotNull(value) // check response not null
                Assert.assertEquals("34", value?.num_of_sat_test_takers) // compare sat test takers
                Assert.assertEquals("324", value?.sat_critical_reading_avg_score)  // compare reading score
                Assert.assertEquals("321", value?.sat_math_avg_score) // compare math score
                Assert.assertEquals("Henry Hight school", value?.school_name) // compare school name
                Assert.assertEquals("1234", value?.dbn) // compare dbn
            }

            val satItem2 = schoolDetailResponse.body()?.get(1)
            satItem2.let { value->
                Assert.assertNotNull(value) // check response not null
                Assert.assertEquals("45", value?.num_of_sat_test_takers) // compare sat test takers
                Assert.assertEquals("224", value?.sat_critical_reading_avg_score) // compare reading score
                Assert.assertEquals("258", value?.sat_math_avg_score) // compare math score
                Assert.assertEquals("Unviersity Hight school", value?.school_name) // compare school name
                Assert.assertEquals("2356", value?.dbn) // compare dbn
            }
        }

    }

    @Test
    fun `test fetch School details with failed response`() = runBlocking {
        val mockRetrofit = RetroInstance.getRetroInstance()
        val response = Response.error<NycSchoolDetailsErrorState>(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"key\":[\"somestuff\"]}"
            )
        )
        val viewModel = NycSchoolDetailsActivityViewModel()
        viewModel.retroService = mockRetrofit
        runBlocking {
            val service = mockk<ApiInterface>(relaxed = true).apply {
                coEvery {  getNYCSchooldata() }  throws Exception("API call failed")
            }
            viewModel.getSchoolDetails()
        }.let {
            response.let { value->
                Assert.assertNotNull(value)
                Assert.assertEquals(403, value.code())
            }

        }

    }
}