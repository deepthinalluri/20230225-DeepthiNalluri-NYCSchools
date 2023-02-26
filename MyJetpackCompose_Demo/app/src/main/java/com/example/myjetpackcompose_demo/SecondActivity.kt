package com.example.myjetpackcompose_demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjetpackcompose_demo.ui.theme.MyJetpackCompose_DemoTheme
import kotlinx.coroutines.launch

class SecondActivity  : ComponentActivity() {
    val viewmodel1 by viewModels<SecondActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyJetpackCompose_DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dbn = intent.getStringExtra("NYCdbn")
                    val schoolName = intent.getStringExtra("NYCSchoolName")
                    val context = LocalContext.current


                    val scope = rememberCoroutineScope()
                    LaunchedEffect(key1 = Unit, block =
                    {
                        scope.launch {
                            viewmodel1.FetchSatData()
                        }
                    })
                    when(val satState = viewmodel1.mySatShooldataitems.observeAsState().value){

                        is MySATSchooldataSuccessState ->{
                            var isSchoolFound : Boolean = false
                            var schoolDetails = SchoolDetails()
                            satState.satSchoolList?.forEach {
                                if(it.dbn == dbn || it.school_name == schoolName) {
                                    isSchoolFound = true
                                    schoolDetails =   SchoolDetails(it.school_name,it.sat_critical_reading_avg_score,it.sat_math_avg_score,it.sat_writing_avg_score)


                                }
                            }
                            if(isSchoolFound) {
                                MySATDataItem(schoolDetails)

                            } else {
                                NoSchoolDataFound()
                            }

                        }
                        is MySATSchooldataLoadingState ->{
                            Loadingstate()

                        }
                        is MySATSchooldataerrorState -> {
                            NycError()
                        }

                    }

                }
            }
        }
    }


}

@Composable
fun MySATDataItem(satSchoolResult: SchoolDetails) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.nyc_school_details),
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.dimen_20).value.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${satSchoolResult.school_name}",
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
                modifier = Modifier.padding(10.dp),
                maxLines = 3


            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${stringResource(id = R.string.math)} ${satSchoolResult.sat_math_avg_score}",
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${stringResource(id = R.string.writing)} ${satSchoolResult.sat_writing_avg_score}",
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${stringResource(id = R.string.reading)} ${satSchoolResult.sat_critical_reading_avg_score}",
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = R.dimen.dimen_16).value.sp,
                color = Color.Green
            )
        }
    }

@Composable
fun NoSchoolDataFound(){
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
