package com.teamdontbe.dontbe.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import com.teamdontbe.data.interceptor.TokenInterceptor
import com.teamdontbe.data_local.SharedPreferenceDataSourceImpl
import com.teamdontbe.dontbe.BuildConfig.DONTBE_BASE_URL
import com.velogandroid.di.extension.isJsonArray
import com.velogandroid.di.extension.isJsonObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesJson(): Json =
        Json {
            isLenient = true
            prettyPrint = true
            explicitNulls = false
            ignoreUnknownKeys = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @AccessToken tokenInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideDataStore(DataStore: SharedPreferenceDataSourceImpl): SharedPreferenceDataSource = DataStore

    @Provides
    @Singleton
    @AccessToken
    fun provideAuthInterceptor(interceptor: TokenInterceptor): Interceptor = interceptor

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor { message ->
                when {
                    message.isJsonObject() -> Timber.d(JSONObject(message).toString(4))
                    message.isJsonArray() -> Timber.d(JSONArray(message).toString(4))
                    else -> Timber.d("CONNECTION INFO -> $message")
                }
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    @DontbeRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(DONTBE_BASE_URL)
            .client(okHttpClient)
            .build()
}
