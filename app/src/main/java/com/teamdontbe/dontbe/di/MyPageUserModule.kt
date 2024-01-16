package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.MyPageUserProfileDataSource
import com.teamdontbe.data.repositoryimpl.MyPageUserProfileRepositoryImpl
import com.teamdontbe.data_remote.api.MyPageUserProfileApiService
import com.teamdontbe.data_remote.datasourceimpl.MyPageUserProfileDataSourceImpl
import com.teamdontbe.domain.repository.MyPageUserProfileDomainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyPageUserModule {
    @Provides
    @Singleton
    fun provideMyPageUserProfileService(
        @DontbeRetrofit retrofit: Retrofit,
    ): MyPageUserProfileApiService = retrofit.create(MyPageUserProfileApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsMyPageUserProfileRepository(RepositoryImpl: MyPageUserProfileRepositoryImpl): MyPageUserProfileDomainRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesExampleDataSource(DataSourceImpl: MyPageUserProfileDataSourceImpl): MyPageUserProfileDataSource
    }
}
