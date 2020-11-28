package com.corniland.mobile.view.utils

sealed class ViewStateResource<T> {
    class Loading<T> : ViewStateResource<T>()
    class Error<T> : ViewStateResource<T>()
    class Success<T>(val item: T) : ViewStateResource<T>()
}

sealed class ViewStateAction {
    object Idle : ViewStateAction()
    object Loading : ViewStateAction()
    object Failed : ViewStateAction()
    object Success : ViewStateAction()
}

sealed class ViewStateActionResponse<T> {
    class Idle<T> : ViewStateActionResponse<T>()
    class Loading<T> : ViewStateActionResponse<T>()
    class Failed<T> : ViewStateActionResponse<T>()
    class Success<T>(val item: T) : ViewStateActionResponse<T>()
}