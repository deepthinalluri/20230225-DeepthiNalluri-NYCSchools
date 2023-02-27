package com.example.a20230225_deepthichowdarynalluri_nycschools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NycSChoolDetailsViewState() // nyc schools details view state
object NycSchoolDetailsLoadingState : NycSChoolDetailsViewState() // nyc schools details loading state
object NycSchoolDetailsErrorState : NycSChoolDetailsViewState() // nyc schools details error state
class NycSchoolDetailsSuccessState(val satSchoolList: List<SATDataItemsItem>?) : NycSChoolDetailsViewState() // // nyc schools details success state

class NycSchoolDetailsActivityViewModel() : ViewModel() {
    var retroService = RetroInstance.getRetroInstance() // get retrofit instance to make network call
    val _nycSchoolDetails = MutableLiveData<NycSChoolDetailsViewState>() //  nyc school details mutable live data to post different view states to UI
    val nycSchoolDetails: LiveData<NycSChoolDetailsViewState> = _nycSchoolDetails
   // get school details
    fun getSchoolDetails() {
        viewModelScope.launch(Dispatchers.IO) {// using View Model scope to make network post view state to UI
            try {
                _nycSchoolDetails.postValue(NycSchoolDetailsLoadingState) // post loading state to UI
                val mySatResponseData = retroService.getSATdata() // making network call to get school details
                _nycSchoolDetails.postValue(NycSchoolDetailsSuccessState(mySatResponseData.body())) // post nyc schools details to UI
            } catch (e: Exception) {
                _nycSchoolDetails.postValue(NycSchoolDetailsErrorState) // post error state to UI
            }
        }


    }
}