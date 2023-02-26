package com.example.myjetpackcompose_demo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MySATSChooldataViewState()

object MySATSchooldataLoadingState : MySATSChooldataViewState()
object MySATSchooldataerrorState : MySATSChooldataViewState()
class MySATSchooldataSuccessState(val satSchoolList: List<SATDataItemsItem>?) : MySATSChooldataViewState()

class SecondActivityViewModel: ViewModel() {

    val retroservice1 = MyRetroInstance.getMyretroinstance()

    val _mySatShooldataitems = MutableLiveData<MySATSChooldataViewState>()
    val mySatShooldataitems: LiveData<MySATSChooldataViewState> = _mySatShooldataitems

    fun FetchSatData(){

        viewModelScope.launch {
            try {

                _mySatShooldataitems.postValue(MySATSchooldataLoadingState)

                val mySatResponseData = retroservice1.getSATdata()

                Log.i("Madhu", "viewModel " + mySatResponseData.body())
                withContext(Dispatchers.Main) {
                    _mySatShooldataitems.postValue(MySATSchooldataSuccessState(mySatResponseData.body()))
                }
            }catch (e:Exception){
                Log.i("Madhu","error "+e.stackTrace)
                _mySatShooldataitems.postValue(MySATSchooldataerrorState)
            }
        }


    }

}