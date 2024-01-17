package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.data.repositoryimpl.MyPageRepositoryImpl
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.data_remote.datasourceimpl.MyPageDataSourceImpl
import com.teamdontbe.domain.repository.MyPageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyPageModule {
    @Provides
    @Singleton
    fun provideMyPageUserProfileService(
        @DontbeRetrofit retrofit: Retrofit,
    ): MyPageApiService = retrofit.create(MyPageApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsMyPageRepository(RepositoryImpl: MyPageRepositoryImpl): MyPageRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesMyPageDataSource(DataSourceImpl: MyPageDataSourceImpl): MyPageDataSource
    }
}
