package com.corniland.mobile.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.repository.UserRepository
import com.corniland.mobile.view.utils.ViewStateAction
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterViewModel(var repository: UserRepository) : ViewModel() {

    val state: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)

    fun performRegister(email: String, username: String, password: String) {
        viewModelScope.launch {
            state.postValue(ViewStateAction.Loading)
            repository.register(email, username, password)
                .catch { state.postValue(ViewStateAction.Failed) }
                .collect { state.postValue(if (it) ViewStateAction.Success else ViewStateAction.Failed) }
        }
    }

}