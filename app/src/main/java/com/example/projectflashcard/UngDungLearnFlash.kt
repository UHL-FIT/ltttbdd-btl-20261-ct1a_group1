package com.example.projectflashcard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.projectflashcard.dulieu.caidat.LuuCaiDatNguoiDung
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuCaiDat
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.dieuhuong.SoDoDieuHuong
import com.example.projectflashcard.nghiepvu.kieudulieu.CaiDatNguoiDung
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien

@Composable
fun UngDungLearnFlash() {
    val context = LocalContext.current.applicationContext
    val khoCaiDat = remember(context) {
        KhoDuLieuCaiDat(LuuCaiDatNguoiDung(context))
    }
    val caiDat by khoCaiDat
        .layCaiDatNguoiDung()
        .collectAsState(initial = CaiDatNguoiDung())

    val heThongDangToi = isSystemInDarkTheme()
    val suDungNenToi = when (caiDat.cheDoGiaoDien) {
        CheDoGiaoDien.HE_THONG -> heThongDangToi
        CheDoGiaoDien.SANG -> false
        CheDoGiaoDien.TOI -> true
    }

    ChuDeLearnFlash(darkTheme = suDungNenToi) {
        SoDoDieuHuong()
    }
}
