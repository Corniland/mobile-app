package com.corniland.mobile.view.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.view.utils.ViewStateActionResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CreateProjectViewModel(var repository: ProjectRepository) : ViewModel() {

    val state: MutableLiveData<ViewStateActionResponse<Project>> =
        MutableLiveData(ViewStateActionResponse.Idle())

    fun performCreate(title: String) {
        viewModelScope.launch {
            state.postValue(ViewStateActionResponse.Loading())
            repository.createProject(title)
                .catch { state.postValue(ViewStateActionResponse.Failed()) }
                .collect { project ->
                    project?.let {
                        state.postValue(ViewStateActionResponse.Success(it))
                    } ?: run {
                        state.postValue(ViewStateActionResponse.Failed())
                    }
                }
        }
    }

}