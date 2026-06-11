package com.example.projectflashcard.dulieu.cucbo.cosodulieu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.projectflashcard.dulieu.cucbo.bang.BangBoThe
import com.example.projectflashcard.dulieu.cucbo.bang.BangLichSuOnTap
import com.example.projectflashcard.dulieu.cucbo.bang.BangTuVung
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanBoThe
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanLichSuOnTap
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanTuVung
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [BangBoThe::class, BangTuVung::class, BangLichSuOnTap::class],
    version = 3,
    exportSchema = false
)
abstract class CoSoDuLieuLearnFlash : RoomDatabase() {

    abstract fun truyVanBoThe(): TruyVanBoThe
    abstract fun truyVanTuVung(): TruyVanTuVung
    abstract fun truyVanLichSuOnTap(): TruyVanLichSuOnTap

    companion object {
        @Volatile
        private var INSTANCE: CoSoDuLieuLearnFlash? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `tu_vung` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `boTheId` INTEGER NOT NULL,
                        `tu` TEXT NOT NULL,
                        `nghia` TEXT NOT NULL,
                        `phienAm` TEXT NOT NULL,
                        `viDu` TEXT NOT NULL,
                        `trangThai` TEXT NOT NULL,
                        `canOnHomNay` INTEGER NOT NULL,
                        `ngayTao` INTEGER NOT NULL,
                        FOREIGN KEY(`boTheId`) REFERENCES `bo_the`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_tu_vung_boTheId` ON `tu_vung` (`boTheId`)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `lich_su_on_tap` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `tuVungId` INTEGER NOT NULL,
                        `boTheId` INTEGER NOT NULL,
                        `mucDoOnTap` TEXT NOT NULL,
                        `ngayOn` INTEGER NOT NULL,
                        FOREIGN KEY(`tuVungId`) REFERENCES `tu_vung`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`boTheId`) REFERENCES `bo_the`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_lich_su_on_tap_tuVungId` ON `lich_su_on_tap` (`tuVungId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_lich_su_on_tap_boTheId` ON `lich_su_on_tap` (`boTheId`)")
            }
        }

        fun layInstance(context: Context): CoSoDuLieuLearnFlash =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CoSoDuLieuLearnFlash::class.java,
                    "learnflash.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
