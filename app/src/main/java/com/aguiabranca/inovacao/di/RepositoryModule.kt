package com.aguiabranca.inovacao.di

import com.aguiabranca.inovacao.data.repository.DashboardRepositoryImpl
import com.aguiabranca.inovacao.data.repository.GuidelineRepositoryImpl
import com.aguiabranca.inovacao.data.repository.IdeaRepositoryImpl
import com.aguiabranca.inovacao.data.repository.ProjectRepositoryImpl
import com.aguiabranca.inovacao.data.repository.UserRepositoryImpl
import com.aguiabranca.inovacao.domain.repository.DashboardRepository
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import com.aguiabranca.inovacao.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindGuidelineRepository(impl: GuidelineRepositoryImpl): GuidelineRepository

    @Binds @Singleton
    abstract fun bindIdeaRepository(impl: IdeaRepositoryImpl): IdeaRepository

    @Binds @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository

    @Binds @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}

