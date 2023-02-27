package com.example.a20230225_deepthichowdarynalluri_nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class SchoolDetailsActivity : ComponentActivity() {

    val satSchoolActivityViewModel by viewModels<NycSchoolDetailsActivityViewModel>() // View Model to get schools details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                val dbn = intent.getStringExtra("NYCdbn") // extract data coming from NYCSchoolAcitvity
                val schoolName = intent.getStringExtra("NYCSchoolName")
                val scope = rememberCoroutineScope()
                LaunchedEffect(key1 = Unit, block =
                {
                    scope.launch {
                        satSchoolActivityViewModel.getSchoolDetails() // make network call using View Model
                    }
                })
                when (val satState = satSchoolActivityViewModel.nycSchoolDetails.observeAsState().value) {
                    is NycSchoolDetailsSuccessState -> { // if network call successful  compare dbn and school names to scores
                        var isSchoolFound = false
                        var schoolDetails = SchoolDetails()
                        satState.satSchoolList?.forEach {
                            if (it.dbn == dbn || it.school_name == schoolName) {
                                isSchoolFound = true
                                schoolDetails = SchoolDetails(
                                    it.school_name,
                                    it.sat_critical_reading_avg_score,
                                    it.sat_math_avg_score,
                                    it.sat_writing_avg_score
                                )
                            }
                        }
                        if (isSchoolFound) {
                            NycSchoolDetails(schoolDetails) // show school details
                        } else {
                            NoSchoolDataFound() // show no school data found message
                        }
                    }
                    is NycSchoolDetailsLoadingState -> { // loading state will be received when while network call is in progress
                        LoadingState() // show loading state

                    }
                    is NycSchoolDetailsErrorState -> { // error state will be received  when network  call failed
                        NycError() // show error message
                    }
                }
            }
        }
    }


}
// show school details
@Composable
fun NycSchoolDetails(schoolDetails: SchoolDetails) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.semantics { "NYC School Details" },
            text = stringResource(id = R.string.nyc_school_details),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.dimen_20).value.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${schoolDetails.school_name}",
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            modifier = Modifier.padding(10.dp).semantics { "school_name" },
            maxLines = 3


        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${stringResource(id = R.string.math)} ${schoolDetails.sat_math_avg_score}",
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            color = Color.Green,
            modifier = Modifier.semantics { "Math Score" }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${stringResource(id = R.string.writing)} ${schoolDetails.sat_writing_avg_score}",
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            color = Color.Green,
            modifier = Modifier.semantics { "Sat Score" }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${stringResource(id = R.string.reading)} ${schoolDetails.sat_critical_reading_avg_score}",
            fontWeight = FontWeight.Normal,
            fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
            color = Color.Green,
            modifier = Modifier.semantics { "Writing Score" }

        )
    }
}

// show no school data found message
@Composable
fun NoSchoolDataFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.no_school_data),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.dimen_20).value.sp
        )
    }

}
