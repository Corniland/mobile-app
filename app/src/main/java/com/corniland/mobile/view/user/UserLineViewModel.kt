package com.corniland.mobile.view.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.UserRepository
import com.corniland.mobile.view.utils.ViewStateResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserLineViewModel(
    var repository: UserRepository,
    userId: String
) : ViewModel() {

    val state: MutableLiveData<ViewStateResource<User>> =
        MutableLiveData(ViewStateResource.Loading())

    init {
        getUser(id = userId)
    }

    private fun getUser(id: String) {
        viewModelScope.launch {
            state.postValue(ViewStateResource.Loading())
            repository.getUser(id)
                .catch { state.postValue(ViewStateResource.Error()) }
                .collect {
                    it?.let {
                        state.postValue(ViewStateResource.Success(it))
                    } ?: run {
                        state.postValue(ViewStateResource.Error())
                    }
                }
        }
    }

}