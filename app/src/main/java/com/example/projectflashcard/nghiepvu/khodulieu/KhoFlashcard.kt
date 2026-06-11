package com.example.projectflashcard.nghiepvu.khodulieu

import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.DuLieuNhapXuat
import com.example.projectflashcard.nghiepvu.kieudulieu.LichSuOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import kotlinx.coroutines.flow.Flow

interface KhoFlashcard {

    fun layTatCaBoThe(): Flow<List<BoThe>>

    fun layBoTheTheoId(boTheId: Long): Flow<BoThe?>

    fun timBoTheoTen(tuKhoa: String): Flow<List<BoThe>>

    suspend fun themBoThe(boThe: BoThe): Long

    suspend fun suaBoThe(boThe: BoThe)

    suspend fun xoaBoThe(boThe: BoThe)

    fun layTuVungTheoBoThe(boTheId: Long): Flow<List<TuVung>>

    fun layTatCaTuVung(): Flow<List<TuVung>>

    fun layTuVungTheoId(id: Long): Flow<TuVung?>

    fun timTuVung(tuKhoa: String): Flow<List<TuVung>>

    suspend fun themTuVung(tuVung: TuVung): Long

    suspend fun suaTuVung(tuVung: TuVung)

    suspend fun xoaTuVungTheoId(id: Long)

    fun layTatCaLichSuOnTap(): Flow<List<LichSuOnTap>>

    fun layLichSuOnTapTheoTuVung(tuVungId: Long): Flow<List<LichSuOnTap>>

    fun layLichSuOnTapTheoBoThe(boTheId: Long): Flow<List<LichSuOnTap>>

    suspend fun themLichSuOnTap(lichSuOnTap: LichSuOnTap): Long

    suspend fun nhapDuLieuNhapXuat(duLieuNhapXuat: DuLieuNhapXuat)
}
