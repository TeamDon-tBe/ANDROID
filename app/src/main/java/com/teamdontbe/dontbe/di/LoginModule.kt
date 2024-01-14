package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.AuthDataSource
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import com.teamdontbe.data.repositoryimpl.AuthRepositoryImpl
import com.teamdontbe.data.repositoryimpl.UserInfoRepositoryImpl
import com.teamdontbe.data_local.SharedPreferenceDataSourceImpl
import com.teamdontbe.data_remote.api.AuthApiService
import com.teamdontbe.data_remote.datasourceimpl.AuthDataSourceImpl
import com.teamdontbe.domain.repository.AuthRepository
import com.teamdontbe.domain.repository.UserInfoRepository
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

        @Binds
        @Singleton
        fun bindUserInfoRepository(RepositoryImpl: UserInfoRepositoryImpl): UserInfoRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesLoginDataSource(DataSourceImpl: AuthDataSourceImpl): AuthDataSource

        @Singleton
        @Binds
        fun providesUserInfoDataSource(DataSourceImpl: SharedPreferenceDataSourceImpl): SharedPreferenceDataSource
    }
}
