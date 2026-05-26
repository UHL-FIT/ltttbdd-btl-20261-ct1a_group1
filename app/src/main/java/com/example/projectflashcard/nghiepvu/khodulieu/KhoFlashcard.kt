package com.example.projectflashcard.nghiepvu.khodulieu

import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import kotlinx.coroutines.flow.Flow

interface KhoFlashcard {

    fun layTatCaBoThe(): Flow<List<BoThe>>

    fun timBoTheoTen(tuKhoa: String): Flow<List<BoThe>>

    suspend fun themBoThe(boThe: BoThe): Long

    suspend fun suaBoThe(boThe: BoThe)

    suspend fun xoaBoThe(boThe: BoThe)
}