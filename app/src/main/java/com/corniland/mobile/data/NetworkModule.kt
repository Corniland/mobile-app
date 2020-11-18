package com.corniland.mobile.data

import com.corniland.mobile.data.repository.Repositories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRepositories(): Repositories {
        return Repositories()
    }

}