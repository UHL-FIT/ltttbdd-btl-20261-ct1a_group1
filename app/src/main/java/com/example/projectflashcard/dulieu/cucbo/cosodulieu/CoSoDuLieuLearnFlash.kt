package com.example.projectflashcard.dulieu.cucbo.cosodulieu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectflashcard.dulieu.cucbo.bang.BangBoThe
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanBoThe

@Database(
    entities = [BangBoThe::class],
    version = 1,
    exportSchema = false
)
abstract class CoSoDuLieuLearnFlash : RoomDatabase() {

    abstract fun truyVanBoThe(): TruyVanBoThe

    companion object {
        @Volatile
        private var INSTANCE: CoSoDuLieuLearnFlash? = null

        fun layInstance(context: Context): CoSoDuLieuLearnFlash =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CoSoDuLieuLearnFlash::class.java,
                    "learnflash.db"
                ).build().also { INSTANCE = it }
            }
    }
}