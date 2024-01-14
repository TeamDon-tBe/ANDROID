package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.AuthDataSource
import com.teamdontbe.data.repositoryimpl.AuthRepositoryImpl
import com.teamdontbe.data_remote.api.AuthApiService
import com.teamdontbe.data_remote.datasourceimpl.AuthDataSourceImpl
import com.teamdontbe.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideLoginService(
        @DontbeRetrofit retrofit: Retrofit,
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsLoginRepository(RepositoryImpl: AuthRepositoryImpl): AuthRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesLoginDataSource(DataSourceImpl: AuthDataSourceImpl): AuthDataSource
    }
}
