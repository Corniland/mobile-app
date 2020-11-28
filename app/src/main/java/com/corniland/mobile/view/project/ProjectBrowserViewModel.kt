package com.corniland.mobile.view.project

import androidx.lifecycle.*
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.view.utils.ViewStateResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProjectBrowserViewModel(var repository: ProjectRepository) : ViewModel() {

    var projectRequest: MutableLiveData<ViewStateResource<List<Project>>> =
        MutableLiveData(ViewStateResource.Loading())

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.getProjects()
                .catch { projectRequest.postValue(ViewStateResource.Error()) }
                .collect {
                    projectRequest.postValue(
                        when (it) {
                            null -> ViewStateResource.Error()
                            else -> ViewStateResource.Success(it)
                        }
                    )
                    isLoading.postValue(false)
                }
        }
    }

}