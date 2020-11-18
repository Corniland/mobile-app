package com.corniland.mobile.view.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class UserSettingsViewState {
    object Idle : UserSettingsViewState()
    object Loading : UserSettingsViewState()
    object Failed : UserSettingsViewState()
    object Success : UserSettingsViewState()
}

class UserSettingsViewModel(var repository: UserRepository) : ViewModel() {

    val state: MutableLiveData<UserSettingsViewState> = MutableLiveData(UserSettingsViewState.Idle)

    fun updateSettings(username: String, password: String, privateProfile: Boolean) {
        viewModelScope.launch {
            state.postValue(UserSettingsViewState.Loading)
            repository.updateSettings(username, password, privateProfile)
                .catch { state.postValue(UserSettingsViewState.Failed) }
                .collect { state.postValue(if (it) UserSettingsViewState.Success else UserSettingsViewState.Failed) }
        }
    }

}