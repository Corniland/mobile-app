package com.corniland.mobile.view.project

import androidx.lifecycle.*
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class ProjectBrowserViewState {
    object Loading : ProjectBrowserViewState()
    object Error : ProjectBrowserViewState()
    class Success(val projects: List<Project>) : ProjectBrowserViewState()
}

class ProjectBrowserViewModel(
    var repository: ProjectRepository
) : ViewModel() {

    var projectRequest: MutableLiveData<ProjectBrowserViewState> =
        MutableLiveData(ProjectBrowserViewState.Loading)

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.getProjects()
                .catch { projectRequest.postValue(ProjectBrowserViewState.Error) }
                .collect {
                    projectRequest.postValue(
                        when (it) {
                            null -> ProjectBrowserViewState.Error
                            else -> ProjectBrowserViewState.Success(it)
                        }
                    )
                    isLoading.postValue(false)
                }
        }
    }

}