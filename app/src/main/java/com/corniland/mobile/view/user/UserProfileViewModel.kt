package com.corniland.mobile.view.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class UserProfileViewState {
    object Loading : UserProfileViewState()
    object Failed: UserProfileViewState()
    class Success(val user: User): UserProfileViewState()
}

class UserProfileViewModel(var repository: UserRepository, val userId: String) : ViewModel() {

    val state: MutableLiveData<UserProfileViewState> = MutableLiveData(UserProfileViewState.Loading)

    init {
        getUser(id = userId)
    }

    private fun getUser(id: String) {
        viewModelScope.launch {
            state.postValue(UserProfileViewState.Loading)
            repository.getUser(id)
                .catch { state.postValue(UserProfileViewState.Failed) }
                .collect {
                    it?.let {
                        state.postValue(UserProfileViewState.Success(it))
                    } ?: run {
                        state.postValue(UserProfileViewState.Failed)
                    }
                }
        }
    }

}