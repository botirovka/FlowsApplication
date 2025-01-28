package com.botirovka.flowsapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _liveData = MutableLiveData("Hello World")
    val liveData: LiveData<String> = _liveData
    private var liveDataJob: Job? = null

    private val _stateFlow = MutableStateFlow("Hello World")
    val stateFlow = _stateFlow.asStateFlow()
    private var stateFlowJob: Job? = null

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()
    private var sharedFlowJob: Job? = null

    val _channel = Channel<String>()
    private var channelJob: Job? = null



    //Холодний потік, почне виконання після підписки
    val flow = flow {
        Log.d("mydebug", "Flow started")
        repeat(100) {
            emit("Flow $it")
            delay(500L)
        }
    }

    fun triggerLiveData() {
        if (liveDataJob?.isActive == true){
            liveDataJob?.cancel()
            return
        }
        liveDataJob = viewModelScope.launch {
            repeat(100) {
                _liveData.postValue("LiveData $it")
                delay(500L)
            }
        }
    }

    fun triggerStateFlow() {
        if (stateFlowJob?.isActive == true){
            stateFlowJob?.cancel()
            return
        }
        stateFlowJob = viewModelScope.launch {
            repeat(100){
                _stateFlow.emit("StateFlow $it")
                delay(500L)
            }
        }
    }

    fun triggerSharedFlow() {
        if (sharedFlowJob?.isActive == true){
            sharedFlowJob?.cancel()
            return
        }
        sharedFlowJob = viewModelScope.launch {
            repeat(100){
                _sharedFlow.emit("SharedFlow $it")
                delay(500L)
            }
        }
    }

    fun triggerChannel() {
        if (channelJob?.isActive == true) {
            channelJob?.cancel()
            return
        }
        channelJob = viewModelScope.launch {
            repeat(2) {
                Log.d("mydebug", "triggerChannel: ")
                _channel.send("Channel $it")
                delay(500L)
            }
        }
    }


}
