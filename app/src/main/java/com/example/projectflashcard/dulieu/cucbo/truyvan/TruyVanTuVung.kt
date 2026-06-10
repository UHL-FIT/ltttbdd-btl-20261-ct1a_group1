package com.example.projectflashcard.dulieu.cucbo.truyvan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectflashcard.dulieu.cucbo.bang.BangTuVung
import kotlinx.coroutines.flow.Flow

@Dao
interface TruyVanTuVung {

    @Query("SELECT * FROM tu_vung ORDER BY ngayTao DESC")
    fun layTatCa(): Flow<List<BangTuVung>>

    @Query("SELECT * FROM tu_vung WHERE boTheId = :boTheId ORDER BY ngayTao DESC")
    fun layTheoBoThe(boTheId: Long): Flow<List<BangTuVung>>

    @Query("SELECT * FROM tu_vung WHERE id = :id LIMIT 1")
    fun layTheoId(id: Long): Flow<BangTuVung?>

    @Query(
        """
        SELECT * FROM tu_vung
        WHERE tu LIKE '%' || :tuKhoa || '%'
            OR nghia LIKE '%' || :tuKhoa || '%'
            OR phienAm LIKE '%' || :tuKhoa || '%'
        ORDER BY ngayTao DESC
        """
    )
    fun timTuVung(tuKhoa: String): Flow<List<BangTuVung>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun them(tuVung: BangTuVung): Long

    @Update
    suspend fun sua(tuVung: BangTuVung)

    @Query("DELETE FROM tu_vung WHERE id = :id")
    suspend fun xoaTheoId(id: Long)
}
