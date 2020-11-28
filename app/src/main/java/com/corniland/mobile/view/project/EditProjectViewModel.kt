package com.corniland.mobile.view.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.model.UpdateProjectRequest
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.view.utils.ViewStateAction
import com.corniland.mobile.view.utils.ViewStateResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EditProjectViewModel(var repository: ProjectRepository, var projectId: String) : ViewModel() {

    val projectRequest: MutableLiveData<ViewStateResource<Project>> =
        MutableLiveData(ViewStateResource.Loading())
    val actionRequest: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)

    init {
        loadProject()
    }

    private fun loadProject() {
        viewModelScope.launch {
            projectRequest.postValue(ViewStateResource.Loading())
            repository.getProject(id = projectId)
                .catch { projectRequest.postValue(ViewStateResource.Error()) }
                .collect { project ->
                    project
                        ?.let { projectRequest.postValue(ViewStateResource.Success(it)) }
                        ?: run { projectRequest.postValue(ViewStateResource.Error()) }
                }
        }
    }

    fun update(projectId: String, projectRequest: UpdateProjectRequest) {
        viewModelScope.launch {
            actionRequest.postValue(ViewStateAction.Loading)
            repository.updateProject(projectId = projectId, projectRequest = projectRequest)
                .catch { actionRequest.postValue(ViewStateAction.Failed) }
                .collect { actionRequest.postValue(if (it != null) ViewStateAction.Success else ViewStateAction.Failed) }
        }
    }

}