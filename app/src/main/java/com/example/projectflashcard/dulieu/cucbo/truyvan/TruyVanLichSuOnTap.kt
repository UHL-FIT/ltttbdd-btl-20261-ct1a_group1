package com.example.projectflashcard.dulieu.cucbo.truyvan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectflashcard.dulieu.cucbo.bang.BangLichSuOnTap
import kotlinx.coroutines.flow.Flow

@Dao
interface TruyVanLichSuOnTap {

    @Query("SELECT * FROM lich_su_on_tap ORDER BY ngayOn DESC")
    fun layTatCa(): Flow<List<BangLichSuOnTap>>

    @Query("SELECT * FROM lich_su_on_tap WHERE tuVungId = :tuVungId ORDER BY ngayOn DESC")
    fun layTheoTuVung(tuVungId: Long): Flow<List<BangLichSuOnTap>>

    @Query("SELECT * FROM lich_su_on_tap WHERE boTheId = :boTheId ORDER BY ngayOn DESC")
    fun layTheoBoThe(boTheId: Long): Flow<List<BangLichSuOnTap>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun them(lichSuOnTap: BangLichSuOnTap): Long
}
