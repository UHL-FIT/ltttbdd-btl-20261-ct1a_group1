package com.example.projectflashcard.dulieu.khodulieu

import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhBangBoThe
import com.example.projectflashcard.dulieu.cucbo.chuyendoi.thanhBoThe
import com.example.projectflashcard.dulieu.cucbo.truyvan.TruyVanBoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KhoDuLieuFlashcard(
    private val truyVanBoThe: TruyVanBoThe
) : KhoFlashcard {

    override fun layTatCaBoThe(): Flow<List<BoThe>> =
        truyVanBoThe.layTatCa().map { ds -> ds.map { it.thanhBoThe() } }

    override fun timBoTheoTen(tuKhoa: String): Flow<List<BoThe>> =
        truyVanBoThe.timTheoTen(tuKhoa).map { ds -> ds.map { it.thanhBoThe() } }

    override suspend fun themBoThe(boThe: BoThe): Long =
        truyVanBoThe.them(boThe.thanhBangBoThe())

    override suspend fun suaBoThe(boThe: BoThe) =
        truyVanBoThe.sua(boThe.thanhBangBoThe())

    override suspend fun xoaBoThe(boThe: BoThe) =
        truyVanBoThe.xoa(boThe.thanhBangBoThe())
}