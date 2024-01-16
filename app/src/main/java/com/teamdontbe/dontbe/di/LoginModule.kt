package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.repositoryimpl.LoginRepositoryImpl
import com.teamdontbe.data_remote.api.LoginApiService
import com.teamdontbe.data_remote.datasourceimpl.LoginDataSourceImpl
import com.teamdontbe.domain.repository.LoginRepository
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
    ): LoginApiService = retrofit.create(LoginApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsLoginRepository(RepositoryImpl: LoginRepositoryImpl): LoginRepository

        @Binds
        @Singleton
        fun bindUserInfoRepository(RepositoryImpl: UserInfoRepositoryImpl): UserInfoRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesLoginDataSource(DataSourceImpl: LoginDataSourceImpl): LoginDataSource

        @Singleton
        @Binds
        fun providesUserInfoDataSource(DataSourceImpl: SharedPreferenceDataSourceImpl): SharedPreferenceDataSource

    }
}
