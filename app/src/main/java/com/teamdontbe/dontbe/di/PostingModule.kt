package com.teamdontbe.dontbe.di

import com.teamdontbe.data.datasource.PostingDataSource
import com.teamdontbe.data.repositoryimpl.PostingRepositoryImpl
import com.teamdontbe.data_remote.api.PostingApiService
import com.teamdontbe.data_remote.datasourceimpl.PostingDataSourceImpl
import com.teamdontbe.domain.repository.PostingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostingModule {
    @Provides
    @Singleton
    fun providePostingService(
        @DontbeRetrofit retrofit: Retrofit,
    ): PostingApiService = retrofit.create(PostingApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsPostingRepository(RepositoryImpl: PostingRepositoryImpl): PostingRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataSourceModule {
        @Singleton
        @Binds
        fun providesPostingDataSource(DataSourceImpl: PostingDataSourceImpl): PostingDataSource
    }
}
