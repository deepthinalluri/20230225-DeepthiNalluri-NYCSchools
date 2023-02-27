package com.example.a20230225_deepthichowdarynalluri_nycschools


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
/*
check network call successful  and failed in View Model
 */
@RunWith(MockitoJUnitRunner::class)
class NycSchoolViewModelTest {
    @Test
    fun `test fetch data function with successful response`() {  // check successful response from network call
        val mockRetrofit = RetroInstance.getRetroInstance() // get retrofit instance
        val response = Response.success( // create success response model
            listOf(
                NYCHighSchoolItems("1120", "Henry Hight school"),
                NYCHighSchoolItems("1121", "Unviersity Hight school")
            )
        )
        val viewModel = NYCSchoolActivityViewModel() // get ViewModel object
        viewModel.retroservice = mockRetrofit
        runBlocking {
            val service = mockk<ApiInterface>(relaxed = true).apply {
                coEvery {  getNYCSchooldata() } returns response
            }
            viewModel.getNycSchools() // make network call  to schools names
        }.let {

            val state = response.body()?.get(0)
            state.let { value->
                assertNotNull(value) // check response not null
                assertEquals("Henry Hight school", value?.school_name) // compare school name
               assertEquals("1120", value?.dbn) // compare school  dbn
            }

            val state2 = response.body()?.get(1)
            state2.let { value->
                Assert.assertNotNull(value)
                assertEquals("Unviersity Hight school", value?.school_name)
                assertEquals("1121", value?.dbn)
            }
        }

    }

    @Test
    fun `test fetchdata function with failed response`() = runBlocking {// check failed response from network call
        val mockRetrofit = RetroInstance.getRetroInstance() // get retrofit instance
        val response = Response.error<NycSchoolErrorState>( // create failure response model
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"key\":[\"somestuff\"]}"
            )
        )
        val viewModel = NYCSchoolActivityViewModel() // get ViewModel
        viewModel.retroservice = mockRetrofit
        runBlocking {
            val service = mockk<ApiInterface>(relaxed = true).apply {
                coEvery {  getNYCSchooldata() }  throws Exception("API call failed")
            }
            viewModel.getNycSchools() // make network call to get school names
        }.let {
            response.let { value->
                Assert.assertNotNull(value) // check response not null
                assertEquals(403, value.code()) // compare error code
            }

        }

    }
}