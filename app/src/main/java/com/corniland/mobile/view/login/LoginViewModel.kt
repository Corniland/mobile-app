package com.corniland.mobile.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class LoginViewState {
    object Idle : LoginViewState()
    object Loading : LoginViewState()
    object FailedLogin : LoginViewState()
    object SuccessLogin : LoginViewState()
}

class LoginViewModel(var repository: UserRepository) : ViewModel() {

    val state: MutableLiveData<LoginViewState> = MutableLiveData(LoginViewState.Idle)

    fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            state.postValue(LoginViewState.Loading)
            repository.login(email, password)
                .catch { state.postValue(LoginViewState.FailedLogin) }
                .collect { state.postValue(if (it) LoginViewState.SuccessLogin else LoginViewState.FailedLogin) }
        }
    }

}