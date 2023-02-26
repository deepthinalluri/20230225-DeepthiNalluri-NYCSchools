package com.example.myjetpackcompose_demo


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class SchoolTestActivity {
    @Test
    fun `test fetch data function with successful response`() = runBlocking {
        // Create a mock Retrofit instance and response

        val mockRetrofit = mockk<ApiInterface>()
        val response = Response.success(listOf(NYCHighSchoolDataItemsItem("1120","Henry Hight school"), NYCHighSchoolDataItemsItem("1121","Unviersity Hight school")))
        coEvery { mockRetrofit.getNYCSchooldata() } returns response

        // Create a ViewModel instance and call fetchdata()
        val viewModel = MainActivityViewModel()
        viewModel.retroservice = mockRetrofit
        viewModel.fetchdata()

        // Assert that the initial state is loading
        val expectedInitialState = MyNYCSchooldataLoadingState
        val actualInitialState = viewModel.myNycShooldataitems.value
        Assert.assertEquals(expectedInitialState, actualInitialState)

        // Wait for the API call to complete
        delay(1000)

        // Assert that the final state is success with the correct data
        val expectedFinalState = MyNYCSchooldataSuccessState(listOf(NYCHighSchoolDataItemsItem("1120","Henry Hight school"), NYCHighSchoolDataItemsItem("1121","Unviersity Hight school")))
        val actualFinalState = viewModel.myNycShooldataitems.value
        Assert.assertEquals(expectedFinalState, actualFinalState)
    }

}