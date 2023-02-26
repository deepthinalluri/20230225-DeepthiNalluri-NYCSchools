package com.example.myjetpackcompose_demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class MainActivityViewModelTest {

    @Mock
    private lateinit var observer: Observer<MyNYCSChooldataViewState>

    @Mock
     //private lateinit var apiInterface: ApiInterface

    private lateinit var viewModel: MainActivityViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewModel()
        viewModel.retroservice = MockRetroService()

    }

    @Test
    fun `fetchdata() should return success state when data is fetched successfully`() =
        testDispatcher.runBlockingTest {
            // Given
            val nycSchoolList = listOf(NYCHighSchoolDataItemsItem("1", "School 1"), NYCHighSchoolDataItemsItem("2", "School 2"))
            val mockResponse = Response.success(nycSchoolList)
            (viewModel.retroservice as MockRetroService).response = mockResponse

            // When
            viewModel.myNycShooldataitems.observeForever(observer)
            viewModel.fetchdata()
            testDispatcher.advanceUntilIdle()

            // Then
            Mockito.verify(observer).onChanged(MyNYCSchooldataLoadingState)
            Mockito.verify(observer).onChanged(MyNYCSchooldataSuccessState(nycSchoolList))
        }

    @Test
    fun `fetchdata() should return error state when data fetch fails`() = testDispatcher.runBlockingTest {
        // Given
        (viewModel.retroservice as MockRetroService).throwException = true

        // When
        viewModel.myNycShooldataitems.observeForever(observer)
        viewModel.fetchdata()
        testDispatcher.advanceUntilIdle()

        // Then
        Mockito.verify(observer).onChanged(MyNYCSchooldataLoadingState)
        Mockito.verify(observer).onChanged(MyNYCSchooldataerrorState)
    }
}

class MockRetroService : ApiInterface {
    var response: Response<List<NYCHighSchoolDataItemsItem>>? = null
    var throwException: Boolean = false


    override suspend fun getNYCSchooldata(): Response<List<NYCHighSchoolDataItemsItem>> {
        return if (throwException) {
            throw Exception("Something went wrong")
        } else {
            response ?: throw Exception("Response not set")
        }
    }

    override suspend fun getSATdata(): Response<List<SATDataItemsItem>> {
        TODO("Not yet implemented")
    }
}

