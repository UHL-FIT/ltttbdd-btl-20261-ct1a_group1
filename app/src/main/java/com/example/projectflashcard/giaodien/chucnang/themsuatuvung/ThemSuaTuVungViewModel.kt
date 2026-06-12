package com.example.projectflashcard.giaodien.chucnang.themsuatuvung

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kiemtra.KiemTraTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemSuaTuVungViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard
    private var congViecTai: Job? = null

    private val _uiState = MutableStateFlow(ThemSuaTuVungUiState())
    val uiState: StateFlow<ThemSuaTuVungUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())
    }

    fun taiDuLieu(boTheId: Int, tuVungId: Long?) {
        congViecTai?.cancel()
        congViecTai = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    boTheId = boTheId,
                    tuVungId = tuVungId,
                    dangTai = true,
                    daLuu = false,
                    thongBaoLoi = null
                )
            }

            val boThe = kho.layBoTheTheoId(boTheId.toLong()).first()
            val tuVung = tuVungId?.let { kho.layTuVungTheoId(it).first() }

            _uiState.update { state ->
                if (tuVung == null) {
                    state.copy(
                        tenBoThe = boThe?.tenBoThe ?: "Bộ thẻ #$boTheId",
                        tuVungGoc = null,
                        tu = "",
                        nghia = "",
                        phienAm = "",
                        viDu = "",
                        dangTai = false
                    )
                } else {
                    state.copy(
                        tenBoThe = boThe?.tenBoThe ?: "Bộ thẻ #$boTheId",
                        tuVungGoc = tuVung,
                        tu = tuVung.tu,
                        nghia = tuVung.nghia,
                        phienAm = tuVung.phienAm,
                        viDu = tuVung.viDu,
                        dangTai = false
                    )
                }
            }
        }
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

        if (!kiemTraNoiDungHopLe(tu, nghia)) return

        viewModelScope.launch {
            _uiState.update { it.copy(dangLuu = true, thongBaoLoi = null) }
            runCatching {
                kiemTraTrungTu(state, tu)
                luuTuVung(state, tu, nghia)
            }.onSuccess {
                _uiState.update { it.copy(dangLuu = false, daLuu = true) }
            }.onFailure { loi ->
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoLoi = loi.message ?: "Không lưu được từ vựng"
                    )
                }
            }
        }
    }

    private fun kiemTraNoiDungHopLe(tu: String, nghia: String): Boolean {
        val loiNoiDung = KiemTraTuVung.kiemTraNoiDung(tu, nghia)
        if (loiNoiDung != null) {
            _uiState.update { it.copy(thongBaoLoi = loiNoiDung) }
            return false
        }
        return true
    }

    private suspend fun kiemTraTrungTu(state: ThemSuaTuVungUiState, tu: String) {
        val loiTrungTu = KiemTraTuVung.kiemTraTrungTu(
            tu = tu,
            danhSachTuVung = kho.layTuVungTheoBoThe(state.boTheId.toLong()).first(),
            tuVungGoc = state.tuVungGoc
        )
        if (loiTrungTu != null) {
            error(loiTrungTu)
        }
    }

    private suspend fun luuTuVung(state: ThemSuaTuVungUiState, tu: String, nghia: String) {
        val tuVungGoc = state.tuVungGoc
        if (tuVungGoc == null) {
            kho.themTuVung(taoTuVungMoi(state, tu, nghia))
        } else {
            kho.suaTuVung(capNhatTuVung(tuVungGoc, state, tu, nghia))
        }
    }

    private fun taoTuVungMoi(state: ThemSuaTuVungUiState, tu: String, nghia: String): TuVung {
        return TuVung(
            boTheId = state.boTheId.toLong(),
            tu = tu,
            nghia = nghia,
            phienAm = state.phienAm.trim(),
            viDu = state.viDu.trim()
        )
    }

    private fun capNhatTuVung(
        tuVungGoc: TuVung,
        state: ThemSuaTuVungUiState,
        tu: String,
        nghia: String
    ): TuVung {
        return tuVungGoc.copy(
            tu = tu,
            nghia = nghia,
            phienAm = state.phienAm.trim(),
            viDu = state.viDu.trim()
        )
    }
}
