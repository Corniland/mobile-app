package com.corniland.mobile.view.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.view.utils.ViewStateAction
import com.corniland.mobile.view.utils.ViewStateResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddRemoveUserToProjectOwnedViewModel(var repository: ProjectRepository) : ViewModel() {

    var projectRequest: MutableLiveData<ViewStateResource<List<Project>>> =
        MutableLiveData(ViewStateResource.Loading())

    init {
        loadProject()
    }

    private fun loadProject() {
        viewModelScope.launch {
            projectRequest.postValue(ViewStateResource.Loading())
            repository.getProjects()
                .catch { projectRequest.postValue(ViewStateResource.Error()) }
                .collect {
                    projectRequest.postValue(
                        when (it) {
                            null -> ViewStateResource.Error()
                            else -> ViewStateResource.Success(it)
                        }
                    )
                }
        }
    }

}

class AddRemoveUserToProjectOwnedItemViewModel(
    val repository: ProjectRepository,
    val projectId: String
) : ViewModel() {
    val addMemberState: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)
    val removeMemberState: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)
    val projectState: MutableLiveData<ViewStateResource<Project>> =
        MutableLiveData(ViewStateResource.Loading())

    init {
        loadProject()
    }

    fun addUserToProject(userId: String, projectId: String) {
        viewModelScope.launch {
            addMemberState.postValue(ViewStateAction.Loading)
            repository.addUserToProject(userId = userId, projectId = projectId)
                .catch { addMemberState.postValue(ViewStateAction.Failed) }
                .collect { success ->
                    if (success) {
                        addMemberState.postValue(ViewStateAction.Success)
                    } else {
                        addMemberState.postValue(ViewStateAction.Failed)
                    }
                }
        }
    }

    fun removeUserToProject(userId: String, projectId: String) {
        viewModelScope.launch {
            removeMemberState.postValue(ViewStateAction.Loading)
            repository.removeUserToProject(userId = userId, projectId = projectId)
                .catch { removeMemberState.postValue(ViewStateAction.Failed) }
                .collect { success ->
                    if (success) {
                        removeMemberState.postValue(ViewStateAction.Success)
                    } else {
                        removeMemberState.postValue(ViewStateAction.Failed)
                    }
                }
        }
    }


    private fun loadProject() {
        viewModelScope.launch {
            projectState.postValue(ViewStateResource.Loading())
            repository.getProject(id = projectId)
                .catch { projectState.postValue(ViewStateResource.Error()) }
                .collect {
                    projectState.postValue(
                        when (it) {
                            null -> ViewStateResource.Error()
                            else -> ViewStateResource.Success(it)
                        }
                    )
                }
        }
    }

    fun refresh() {
        addMemberState.postValue(ViewStateAction.Idle)
        removeMemberState.postValue(ViewStateAction.Idle)
        loadProject()
    }
}
