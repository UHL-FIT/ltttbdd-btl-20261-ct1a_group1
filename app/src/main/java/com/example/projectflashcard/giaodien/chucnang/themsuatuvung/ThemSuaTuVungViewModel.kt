package com.example.projectflashcard.giaodien.chucnang.themsuatuvung

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemSuaTuVungViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard

    private val _uiState = MutableStateFlow(ThemSuaTuVungUiState())
    val uiState: StateFlow<ThemSuaTuVungUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())
    }

    fun taiBoThe(boTheId: Int) {
        _uiState.update { it.copy(boTheId = boTheId, daLuu = false, thongBaoLoi = null) }
    }

    fun doiTu(tu: String) {
        _uiState.update { it.copy(tu = tu, thongBaoLoi = null) }
    }

    fun doiNghia(nghia: String) {
        _uiState.update { it.copy(nghia = nghia, thongBaoLoi = null) }
    }

    fun doiPhienAm(phienAm: String) {
        _uiState.update { it.copy(phienAm = phienAm) }
    }

    fun doiViDu(viDu: String) {
        _uiState.update { it.copy(viDu = viDu) }
    }

    fun luu() {
        val state = _uiState.value
        val tu = state.tu.trim()
        val nghia = state.nghia.trim()

        if (tu.isEmpty()) {
            _uiState.update { it.copy(thongBaoLoi = "Tu vung khong duoc de trong") }
            return
        }
        if (nghia.isEmpty()) {
            _uiState.update { it.copy(thongBaoLoi = "Nghia khong duoc de trong") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(dangLuu = true, thongBaoLoi = null) }
            runCatching {
                kho.themTuVung(
                    TuVung(
                        boTheId = state.boTheId.toLong(),
                        tu = tu,
                        nghia = nghia,
                        phienAm = state.phienAm.trim(),
                        viDu = state.viDu.trim()
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(dangLuu = false, daLuu = true) }
            }.onFailure { loi ->
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoLoi = loi.message ?: "Khong luu duoc tu vung"
                    )
                }
            }
        }
    }
}
