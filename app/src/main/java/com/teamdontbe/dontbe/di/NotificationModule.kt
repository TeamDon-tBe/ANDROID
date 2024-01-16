package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.NotificationDataSource
import com.teamdontbe.data.repositoryimpl.NotificationRepositoryImpl
import com.teamdontbe.data_remote.api.NotificationApiService
import com.teamdontbe.data_remote.datasourceimpl.NotificationDataSourceImpl
import com.teamdontbe.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationService(
        @DontbeRetrofit retrofit: Retrofit,
    ): NotificationApiService = retrofit.create(NotificationApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsNotificationRepository(RepositoryImpl: NotificationRepositoryImpl): NotificationRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesNotificationDataSource(DataSourceImpl: NotificationDataSourceImpl): NotificationDataSource
    }
}
