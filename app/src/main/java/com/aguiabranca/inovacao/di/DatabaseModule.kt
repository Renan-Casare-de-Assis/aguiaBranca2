package com.aguiabranca.inovacao.di

import android.content.Context
import androidx.room.Room
import com.aguiabranca.inovacao.data.local.room.AppDatabase
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.local.room.dao.GuidelineDao
import com.aguiabranca.inovacao.data.local.room.dao.IdeaDao
import com.aguiabranca.inovacao.data.local.room.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aguia_branca_db"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideGuidelineDao(db: AppDatabase): GuidelineDao = db.guidelineDao()
    @Provides fun provideIdeaDao(db: AppDatabase): IdeaDao = db.ideaDao()
    @Provides fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()
}

