package com.example.projectflashcard.dulieu.khodulieu

import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhBangBoThe
import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhBangTuVung
import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhBoThe
import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhTuVung
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanBoThe
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KhoDuLieuFlashcard(
    private val truyVanBoThe: TruyVanBoThe,
    private val truyVanTuVung: TruyVanTuVung
) : KhoFlashcard {

    override fun layTatCaBoThe(): Flow<List<BoThe>> =
        truyVanBoThe.layTatCa().map { ds -> ds.map { it.thanhBoThe() } }

    override fun layBoTheTheoId(boTheId: Long): Flow<BoThe?> =
        truyVanBoThe.layTheoId(boTheId).map { it?.thanhBoThe() }

    override fun timBoTheoTen(tuKhoa: String): Flow<List<BoThe>> =
        truyVanBoThe.timTheoTen(tuKhoa).map { ds -> ds.map { it.thanhBoThe() } }

    override suspend fun themBoThe(boThe: BoThe): Long =
        truyVanBoThe.them(boThe.thanhBangBoThe())

    override suspend fun suaBoThe(boThe: BoThe) =
        truyVanBoThe.sua(boThe.thanhBangBoThe())

    override suspend fun xoaBoThe(boThe: BoThe) =
        truyVanBoThe.xoa(boThe.thanhBangBoThe())

    override fun layTuVungTheoBoThe(boTheId: Long): Flow<List<TuVung>> =
        truyVanTuVung.layTheoBoThe(boTheId).map { ds -> ds.map { it.thanhTuVung() } }

    override fun layTuVungTheoId(id: Long): Flow<TuVung?> =
        truyVanTuVung.layTheoId(id).map { it?.thanhTuVung() }

    override fun timTuVung(tuKhoa: String): Flow<List<TuVung>> =
        truyVanTuVung.timTuVung(tuKhoa).map { ds -> ds.map { it.thanhTuVung() } }

    override suspend fun themTuVung(tuVung: TuVung): Long =
        truyVanTuVung.them(tuVung.thanhBangTuVung())

    override suspend fun suaTuVung(tuVung: TuVung) =
        truyVanTuVung.sua(tuVung.thanhBangTuVung())

    override suspend fun xoaTuVungTheoId(id: Long) =
        truyVanTuVung.xoaTheoId(id)
}
