package com.example.projectflashcard.nghiepvu.khodulieu

import com.example.projectflashcard.nghiepvu.kieudulieu.CaiDatNguoiDung
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien
import kotlinx.coroutines.flow.Flow

interface KhoCaiDat {
    fun layCaiDatNguoiDung(): Flow<CaiDatNguoiDung>

    suspend fun luuMucTieuOnTapMoiNgay(mucTieu: Int)

    suspend fun luuCheDoGiaoDien(cheDoGiaoDien: CheDoGiaoDien)

    suspend fun luuCaiDatNguoiDung(caiDatNguoiDung: CaiDatNguoiDung)
}
