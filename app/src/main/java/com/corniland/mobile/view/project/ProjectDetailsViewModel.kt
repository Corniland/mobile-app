package com.corniland.mobile.view.project

import androidx.lifecycle.*
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class ProjectDetailsViewState {
    object Loading : ProjectDetailsViewState()
    object Error : ProjectDetailsViewState()
    class Success(val project: Project) : ProjectDetailsViewState()
}

class ProjectDetailsViewModel(
    var projectId: String,
    var repository: ProjectRepository
) : ViewModel() {

    var projectRequest: MutableLiveData<ProjectDetailsViewState> =
        MutableLiveData(ProjectDetailsViewState.Loading)

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.getProject(id = projectId)
                .catch { projectRequest.postValue(ProjectDetailsViewState.Error) }
                .collect {
                    projectRequest.postValue(
                        when (it) {
                            null -> ProjectDetailsViewState.Error
                            else -> ProjectDetailsViewState.Success(it)
                        }
                    )
                    isLoading.postValue(false)
                }
        }
    }

}