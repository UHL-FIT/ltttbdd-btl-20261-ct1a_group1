package com.example.projectflashcard.giaodien.chucnang.trangchu

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TrangChuViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard

    var uiState by mutableStateOf(TrangChuUiState())
        private set

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())

        viewModelScope.launch {
            combine(
                kho.layTatCaBoThe(),
                kho.layTatCaTuVung()
            ) { danhSachBoThe, danhSachTuVung ->
                taoUiState(danhSachBoThe, danhSachTuVung)
            }.collect { stateMoi ->
                uiState = stateMoi
            }
        }
    }

    private fun taoUiState(
        danhSachBoThe: List<BoThe>,
        danhSachTuVung: List<TuVung>
    ): TrangChuUiState {
        return TrangChuUiState(
            tongSoBoThe = danhSachBoThe.size,
            tongSoTuVung = danhSachTuVung.size,
            soTuDaThuoc = danhSachTuVung.count { it.trangThai == TrangThaiTuVung.DA_THUOC },
            soTuCanOnHomNay = danhSachTuVung.count {
                it.trangThai != TrangThaiTuVung.DA_THUOC && it.canOnHomNay
            }
        )
    }
}

