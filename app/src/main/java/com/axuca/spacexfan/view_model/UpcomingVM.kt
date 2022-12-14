package com.axuca.spacexfan.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axuca.spacexfan.model.UpcomingLaunch
import com.axuca.spacexfan.retrofit.SpaceXApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingVM : ViewModel() {

    private var _upcomingLaunch = MutableLiveData<List<UpcomingLaunch>>()
    val upcomingLaunch: LiveData<List<UpcomingLaunch>>
        get() = _upcomingLaunch

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = SpaceXApi.retrofitService.getUpcomingLaunches()
            withContext(Dispatchers.Main) {
                _upcomingLaunch.value = result
            }
        }
    }

    fun getUpcomingLaunch(launchId: String): UpcomingLaunch {
        return _upcomingLaunch.value?.find {
            it.id == launchId
        }!!
    }
}