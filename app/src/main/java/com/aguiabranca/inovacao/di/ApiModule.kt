package com.aguiabranca.inovacao.di

import com.aguiabranca.inovacao.BuildConfig
import com.aguiabranca.inovacao.data.remote.api.DashboardApiService
import com.aguiabranca.inovacao.data.remote.api.GuidelineApiService
import com.aguiabranca.inovacao.data.remote.api.IdeaApiService
import com.aguiabranca.inovacao.data.remote.api.ProjectApiService
import com.aguiabranca.inovacao.data.remote.api.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)

    @Provides
    fun provideProjectApi(retrofit: Retrofit): ProjectApiService = retrofit.create(ProjectApiService::class.java)

    @Provides
    fun provideIdeaApi(retrofit: Retrofit): IdeaApiService = retrofit.create(IdeaApiService::class.java)

    @Provides
    fun provideGuidelineApi(retrofit: Retrofit): GuidelineApiService = retrofit.create(GuidelineApiService::class.java)

    @Provides
    fun provideDashboardApi(retrofit: Retrofit): DashboardApiService = retrofit.create(DashboardApiService::class.java)
}

