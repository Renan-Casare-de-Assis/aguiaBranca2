package com.aguiabranca.inovacao.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aguiabranca.inovacao.data.local.room.dao.GuidelineDao
import com.aguiabranca.inovacao.data.local.room.dao.IdeaDao
import com.aguiabranca.inovacao.data.local.room.dao.ProjectDao
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.local.room.entity.GuidelineEntity
import com.aguiabranca.inovacao.data.local.room.entity.IdeaEntity
import com.aguiabranca.inovacao.data.local.room.entity.ProjectEntity
import com.aguiabranca.inovacao.data.local.room.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GuidelineEntity::class,
        IdeaEntity::class,
        ProjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun guidelineDao(): GuidelineDao
    abstract fun ideaDao(): IdeaDao
    abstract fun projectDao(): ProjectDao
}

