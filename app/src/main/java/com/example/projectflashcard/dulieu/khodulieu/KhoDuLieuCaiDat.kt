package com.example.projectflashcard.dulieu.khodulieu

import com.example.projectflashcard.dulieu.caidat.LuuCaiDatNguoiDung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoCaiDat
import com.example.projectflashcard.nghiepvu.kieudulieu.CaiDatNguoiDung
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien
import kotlinx.coroutines.flow.Flow

class KhoDuLieuCaiDat(
    private val luuCaiDatNguoiDung: LuuCaiDatNguoiDung
) : KhoCaiDat {

    override fun layCaiDatNguoiDung(): Flow<CaiDatNguoiDung> =
        luuCaiDatNguoiDung.caiDatNguoiDung

    override suspend fun luuMucTieuOnTapMoiNgay(mucTieu: Int) {
        luuCaiDatNguoiDung.luuMucTieuOnTapMoiNgay(mucTieu)
    }

    override suspend fun luuCheDoGiaoDien(cheDoGiaoDien: CheDoGiaoDien) {
        luuCaiDatNguoiDung.luuCheDoGiaoDien(cheDoGiaoDien)
    }

    override suspend fun luuCaiDatNguoiDung(caiDatNguoiDung: CaiDatNguoiDung) {
        luuCaiDatNguoiDung.luuCaiDatNguoiDung(caiDatNguoiDung)
    }
}
