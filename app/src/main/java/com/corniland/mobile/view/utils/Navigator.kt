package com.corniland.mobile.view.utils

import android.os.Parcelable
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.savedinstancestate.listSaver
import androidx.compose.runtime.toMutableStateList
import com.corniland.mobile.view.main.Destination

class Navigator<T> private constructor(
    initialBackStack: List<T>,
    backDispatcher: OnBackPressedDispatcher
) {
    constructor(
        initial: T,
        backDispatcher: OnBackPressedDispatcher
    ) : this(listOf(initial), backDispatcher)

    private val backStack = initialBackStack.toMutableStateList()
    private val backCallback = object : OnBackPressedCallback(canGoBack()) {
        override fun handleOnBackPressed() {
            back()
        }
    }.also { callback ->
        backDispatcher.addCallback(callback)
    }

    val current: T get() = backStack.last()

    private fun canGoBack(): Boolean = backStack.size > 1

    fun back() {
        backStack.removeAt(backStack.lastIndex)
        backCallback.isEnabled = canGoBack()
    }

    fun navigate(destination: T) {
        if (destination == current) { return }
        backStack += destination
        backCallback.isEnabled = canGoBack()
    }

    companion object {
        fun <T : Parcelable> saver(backDispatcher: OnBackPressedDispatcher) =
            listSaver<Navigator<T>, T>(
                save = { navigator -> navigator.backStack.toList() },
                restore = { backstack -> Navigator(backstack, backDispatcher) }
            )
    }
}

val NavigatorAmbient : ProvidableAmbient<Navigator<Destination>> = ambientOf()
