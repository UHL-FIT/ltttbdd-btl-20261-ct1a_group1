package com.example.projectflashcard.dulieu.cucbo.truyvan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectflashcard.dulieu.cucbo.bang.BangBoThe
import kotlinx.coroutines.flow.Flow

@Dao
interface TruyVanBoThe {

    @Query("SELECT * FROM bo_the ORDER BY ngayTao DESC")
    fun layTatCa(): Flow<List<BangBoThe>>

    @Query("SELECT * FROM bo_the WHERE id = :id LIMIT 1")
    fun layTheoId(id: Long): Flow<BangBoThe?>

    @Query("SELECT * FROM bo_the WHERE tenBoThe LIKE '%' || :tuKhoa || '%' ORDER BY ngayTao DESC")
    fun timTheoTen(tuKhoa: String): Flow<List<BangBoThe>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun them(boThe: BangBoThe): Long

    @Update
    suspend fun sua(boThe: BangBoThe)

    @Delete
    suspend fun xoa(boThe: BangBoThe)
}
