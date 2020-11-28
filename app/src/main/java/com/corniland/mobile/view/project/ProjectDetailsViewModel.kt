package com.corniland.mobile.view.project

import androidx.lifecycle.*
import com.corniland.mobile.data.SessionManager
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.view.utils.ViewStateAction
import com.corniland.mobile.view.utils.ViewStateResource
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProjectDetailsViewModel(
    var projectId: String,
    var repository: ProjectRepository,
    var session: SessionManager,
) : ViewModel() {
    var projectRequest: MutableLiveData<ViewStateResource<Project>> =
        MutableLiveData(ViewStateResource.Loading())

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var likeRequest: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)
    var deleteRequest: MutableLiveData<ViewStateAction> = MutableLiveData(ViewStateAction.Idle)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.getProject(id = projectId)
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

    fun likeProject(onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.likeProject(id = projectId)
                .catch { likeRequest.postValue(ViewStateAction.Failed) }
                .collect { success ->
                    if (success) {
                        session.updateCurrentUser()
                        likeRequest.postValue(ViewStateAction.Success)
                    } else {
                        likeRequest.postValue(ViewStateAction.Failed)
                    }
                }
                .also { onCompleted() }
        }
    }

    fun unlikeProject(onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.unlikeProject(id = projectId)
                .catch { likeRequest.postValue(ViewStateAction.Failed) }
                .collect { success ->
                    if (success) {
                        session.updateCurrentUser()
                        likeRequest.postValue(ViewStateAction.Success)
                    } else {
                        likeRequest.postValue(ViewStateAction.Failed)
                    }
                }
                .also { onCompleted() }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.deleteProject(id = projectId)
                .catch { deleteRequest.postValue(ViewStateAction.Failed) }
                .collect { deleteRequest.postValue(if (it) ViewStateAction.Success else ViewStateAction.Failed) }
        }
    }

}