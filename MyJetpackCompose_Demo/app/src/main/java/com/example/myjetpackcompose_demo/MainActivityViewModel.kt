package com.example.myjetpackcompose_demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MyNYCSChooldataViewState

object MyNYCSchooldataLoadingState : MyNYCSChooldataViewState()
object MyNYCSchooldataerrorState : MyNYCSChooldataViewState()
class MyNYCSchooldataSuccessState(val nycSchoolList: List<NYCHighSchoolDataItemsItem>?) :
    MyNYCSChooldataViewState()

class MainActivityViewModel : ViewModel() {
    var retroservice = MyRetroInstance.getMyretroinstance()

   private val _myNycShooldataitems = MutableLiveData<MyNYCSChooldataViewState>()
    val myNycShooldataitems: LiveData<MyNYCSChooldataViewState> = _myNycShooldataitems

    fun fetchdata() {
        viewModelScope.launch {
            try {
                _myNycShooldataitems.postValue(MyNYCSchooldataLoadingState)
                val myresonsedata = retroservice.getNYCSchooldata()
                withContext(Dispatchers.Main) {
                    _myNycShooldataitems.postValue(MyNYCSchooldataSuccessState(myresonsedata.body()))
                }
            } catch (e: java.lang.Exception) {

                _myNycShooldataitems.postValue(MyNYCSchooldataerrorState)

            }

        }
    }
}