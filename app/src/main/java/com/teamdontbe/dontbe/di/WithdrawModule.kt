package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.WithdrawDataSource
import com.teamdontbe.data.repositoryimpl.WithdrawRepositoryImpl
import com.teamdontbe.data_remote.api.WithdrawApiService
import com.teamdontbe.data_remote.datasourceimpl.WithdrawDataSourceImpl
import com.teamdontbe.domain.repository.WithdrawRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WithdrawModule {
    @Provides
    @Singleton
    fun provideWithdrawService(
        @DontbeRetrofit retrofit: Retrofit,
    ): WithdrawApiService = retrofit.create(WithdrawApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsWithdrawRepository(RepositoryImpl: WithdrawRepositoryImpl): WithdrawRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesWithdrawDataSource(DataSourceImpl: WithdrawDataSourceImpl): WithdrawDataSource
    }
}
