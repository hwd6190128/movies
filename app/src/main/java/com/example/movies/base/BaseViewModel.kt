package com.example.movies.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    internal var isLoading = MutableLiveData<LiveDataEvent<Boolean>>()

    init {
        isLoading.value = null
    }

    fun showLoading(isShow: Boolean = true) {
        isLoading.postValue(LiveDataEvent(isShow))
    }
}