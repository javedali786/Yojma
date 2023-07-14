package com.breadgangtvnetwork.utils.helpers.downloads.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Singleton


@Database(entities = [DownloadedVideo::class, DownloadedEpisodes::class], version = 8, exportSchema = false)
@Singleton
abstract class DownloadDatabase : RoomDatabase() {
    abstract fun downloadVideoDao(): DownloadVideoDao
    abstract fun downloadEpisodeDao(): DownloadEpisodeDao

    companion object {
        @Volatile
        private var instance: DownloadDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
                ?: synchronized(LOCK) {
            instance
                    ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                DownloadDatabase::class.java, "enveu.db")
                .fallbackToDestructiveMigration()
                .build()
        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {

//                database.execSQL("ALTER TABLE 'downloadedVideos' ADD COLUMN 'parentalRating' TEXT")
//                database.execSQL("ALTER TABLE 'downloadedEpisodes' ADD COLUMN 'parentalRating' TEXT")
//
            }
        }
    }

}