package com.example.a20230225_deepthichowdarynalluri_nycschools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NycSchoolViewState // nyc schools names view  state

object NycSchoolLoadingState : NycSchoolViewState() // nyc schools names loading state
object NycSchoolErrorState : NycSchoolViewState() // nyc schools names error state
class NycSchoolSuccessState(val nycSchoolList: List<NYCHighSchoolItems>?) : NycSchoolViewState() // nyc schools success  state

class NYCSchoolActivityViewModel() : ViewModel() {
    var retroservice = RetroInstance.getRetroInstance() // get retrofit instance to make network call

    private val _nycSchoolData = MutableLiveData<NycSchoolViewState>() //  nyc school mutable live data to post different view states to UI
    val nycSchoolData: LiveData<NycSchoolViewState> = _nycSchoolData

    fun getNycSchools() {
        viewModelScope.launch(Dispatchers.IO) {// using View Model scope to make network post view state to UI
            try {
                _nycSchoolData.postValue(NycSchoolLoadingState) // post loading state to UI
                val myresonsedata = retroservice.getNYCSchooldata() // making network call to get school names
                _nycSchoolData.postValue(NycSchoolSuccessState(myresonsedata.body())) // post nyc schools names to UI
            } catch (e: java.lang.Exception) {
                _nycSchoolData.postValue(NycSchoolErrorState) // post error state to UI

            }

        }
    }
}