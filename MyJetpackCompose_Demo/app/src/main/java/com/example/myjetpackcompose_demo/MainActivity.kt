package com.example.myjetpackcompose_demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material.Card
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.myjetpackcompose_demo.ui.theme.MyJetpackCompose_DemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewmodel by viewModels<MainActivityViewModel>()
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            val status by connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.Status.Unavailable
            )
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "NYC Schools",
                            fontSize =dimensionResource(id = R.dimen.dimen_20).value.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                if (status.equals(ConnectivityObserver.Status.Available)) {
                    MyviewModel(viewmodel = viewmodel)
                } else {
                    ConnectivityNotAvailable()

                }
            }
        }
    }
}


@Composable
fun MyviewModel(viewmodel: MainActivityViewModel) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block =
    {
        scope.launch {
            Log.i("Madhu", "viewModel " + viewmodel.fetchdata())
            viewmodel.fetchdata()
        }
    })
    when (val state = viewmodel.myNycShooldataitems.observeAsState().value) {

        is MyNYCSchooldataSuccessState -> {
            state.nycSchoolList?.let { MyRecyclerView(it) }
        }
        is MyNYCSchooldataLoadingState -> {
            Loadingstate()
        }
        is MyNYCSchooldataerrorState -> {
            NycError()
        }
    }

}

@Composable
fun Loadingstate() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.purple_200)
        )
    }
}

@Composable
fun MyRecyclerView(result: List<NYCHighSchoolDataItemsItem>) {

    LazyColumn {
        itemsIndexed(
            items = result
        ) { index, item ->
            MyDataItem(item)
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDataItem(nycSchoolResult: NYCHighSchoolDataItemsItem) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                context.startActivity(Intent(context, SecondActivity::class.java).apply {
                    this.putExtra("NYCdbn", nycSchoolResult.dbn)
                    this.putExtra("NYCSchoolName", nycSchoolResult.school_name)
                })
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 10.dp

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
                text = nycSchoolResult.school_name,
                fontWeight = FontWeight.Bold,
                fontSize =dimensionResource(id = R.dimen.dimen_16).value.sp,


                )
        }

    }

}

@Composable
fun ConnectivityNotAvailable() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = stringResource(id = R.string.netwrok_unavialable),
            fontWeight = FontWeight.Normal,
            fontSize =dimensionResource(id = R.dimen.dimen_16).value.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = Color.Red

        )
    }

}

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
