package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.repositoryimpl.HomeRepositoryImpl
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.data_remote.datasourceimpl.HomeDataSourceImpl
import com.teamdontbe.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideHomeService(
        @DontbeRetrofit retrofit: Retrofit,
    ): HomeApiService = retrofit.create(HomeApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsHomeRepository(RepositoryImpl: HomeRepositoryImpl): HomeRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesHomeDataSource(DataSourceImpl: HomeDataSourceImpl): HomeDataSource
    }
}
