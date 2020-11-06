package com.corniland.mobile.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class RegisterViewState {
    object Idle : RegisterViewState()
    object Loading : RegisterViewState()
    object FailedRegister : RegisterViewState()
    object SuccessRegister : RegisterViewState()
}

class RegisterViewModel(var repository: UserRepository) : ViewModel() {

    val state: MutableLiveData<RegisterViewState> = MutableLiveData(RegisterViewState.Idle)

    fun performRegister(email: String, username: String, password: String) {
        viewModelScope.launch {
            state.postValue(RegisterViewState.Loading)
            repository.register(email, username, password)
                .catch { state.postValue(RegisterViewState.FailedRegister) }
                .collect { state.postValue(if (it) RegisterViewState.SuccessRegister else RegisterViewState.FailedRegister) }
        }
    }

}