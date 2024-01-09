package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.ExampleDataSource
import com.teamdontbe.data.repositoryimpl.ExampleRepositoryImpl
import com.teamdontbe.data_remote.api.ExampleApiService
import com.teamdontbe.data_remote.datasourceimpl.ExampleDataSourceImpl
import com.teamdontbe.data_remote.datasourceimpl.ExamplePagingRepositoryImpl
import com.teamdontbe.domain.repository.ExamplePagingRepository
import com.teamdontbe.domain.repository.ExampleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExampleModule {
    @Provides
    @Singleton
    fun provideSignService(
        @DontbeRetrofit retrofit: Retrofit,
    ): ExampleApiService = retrofit.create(ExampleApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsExampleRepository(RepositoryImpl: ExampleRepositoryImpl): ExampleRepository

        @Binds
        @Singleton
        fun bindsExamplePagingRepository(RepositoryImpl: ExamplePagingRepositoryImpl): ExamplePagingRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesExampleDataSource(DataSourceImpl: ExampleDataSourceImpl): ExampleDataSource
    }
}
