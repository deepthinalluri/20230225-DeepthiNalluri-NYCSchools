package com.example.a20230225_deepthichowdarynalluri_nycschools

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


class NYCSchoolActivity: ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        val nycSchoolActivityViewModel by viewModels<NYCSchoolActivityViewModel>() // viewModel to get NYC school names
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext) //creating object for network connectivity
        setContent {
            val status by connectivityObserver.observe().collectAsState( //Checking internet connection
                initial = ConnectivityObserver.Status.Unavailable
            )
            Column {
                TopAppBar( // adding top app bar to app
                    title = {
                        Text(
                            text = stringResource(id = R.string.nyc_school),
                            fontSize = dimensionResource(id = R.dimen.dimen_20).value.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                if (status.equals(ConnectivityObserver.Status.Available)) { // if connection is  available , get school names list
                    NycSchoolList(nycSchoolActivityViewModel = nycSchoolActivityViewModel)
                } else {
                    ConnectivityNotAvailable()  // add internet connection not available message

                }
            }
        }
    }
}

//Composable function for network call to fetch NYCSchoolsnames list through View Model and live data
@Composable
fun NycSchoolList(nycSchoolActivityViewModel: NYCSchoolActivityViewModel) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block =
    {
        scope.launch {
            nycSchoolActivityViewModel.getNycSchools() //network call to fetch NYCSchoolsNames list
        }
    })
    when (val state = nycSchoolActivityViewModel.nycSchoolData.observeAsState().value) {

        is NycSchoolSuccessState -> { // if network call is successful , call school names compose function
            state.nycSchoolList?.let { NycSchoolNames(it) }
        }
        is NycSchoolLoadingState -> { // add loading state before receiving the data from network
            LoadingState()
        }
        is NycSchoolErrorState -> {
            NycError() // if network call is un successful  , add error message
        }
    }

}

//show loading spinner
@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize().semantics
        {
            contentDescription = "Circular progress indicator"
        }.testTag("ProgressBar"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.purple_200)
        )
    }
}

// displaying list of schools names in scrollable list
@Composable
fun NycSchoolNames(nycHighSchoolItems: List<NYCHighSchoolItems>) {

    LazyColumn {
        itemsIndexed(
            items = nycHighSchoolItems
        ) { index, item ->
            NycSchoolNameItem(item)
        }
    }
}

//display school name
@Composable
fun NycSchoolNameItem(nycHighSchoolItems: NYCHighSchoolItems) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp).semantics { contentDescription = "NYC School Name Item Card" }
            .clickable {
                context.startActivity(Intent(context, SchoolDetailsActivity::class.java).apply {
                    this.putExtra("NYCdbn", nycHighSchoolItems.dbn)
                    this.putExtra("NYCSchoolName", nycHighSchoolItems.school_name)
                })
            },
        shape = RoundedCornerShape(8.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = nycHighSchoolItems.school_name,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp
                )
        }
    }
}

//Displaying text when internet is not available
@Composable
fun ConnectivityNotAvailable() {

    Box(
        modifier = Modifier.fillMaxSize().testTag("Internet Connection Not Available"),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = stringResource(id = R.string.netwrok_unavialable),
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = Color.Red

        )
    }

}

// display error message when network call failed.
@Composable
fun NycError() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = stringResource(id = R.string.api_error),
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = Color.Red

        )
    }

}

