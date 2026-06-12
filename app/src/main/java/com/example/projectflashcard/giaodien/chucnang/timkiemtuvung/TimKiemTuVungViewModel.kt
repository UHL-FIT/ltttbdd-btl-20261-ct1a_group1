package com.example.projectflashcard.giaodien.chucnang.timkiemtuvung

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TimKiemTuVungViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard
    private val _uiState = MutableStateFlow(TimKiemTuVungUiState())
    val uiState: StateFlow<TimKiemTuVungUiState> = _uiState.asStateFlow()

    private val _tuKhoa = MutableStateFlow("")

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())

        viewModelScope.launch {
            _tuKhoa
                .flatMapLatest { tuKhoa ->
                    if (tuKhoa.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        kho.timTuVung(tuKhoa)
                    }
                }
                .combine(kho.layTatCaBoThe()) { danhSachTuVung, danhSachBoThe ->
                    taoKetQuaTimKiem(danhSachTuVung, danhSachBoThe)
                }
                .collect { ketQua ->
                    _uiState.update { it.copy(ketQua = ketQua, dangTai = false) }
                }
        }
    }

    fun xuLySuKien(event: TimKiemTuVungEvent) {
        when (event) {
            is TimKiemTuVungEvent.ThayDoiTuKhoa -> thayDoiTuKhoa(event.tuKhoa)
        }
    }

    private fun thayDoiTuKhoa(tuKhoa: String) {
        _uiState.update {
            it.copy(
                tuKhoa = tuKhoa,
                dangTai = tuKhoa.isNotBlank()
            )
        }
        _tuKhoa.value = tuKhoa
    }

    private fun taoKetQuaTimKiem(
        danhSachTuVung: List<TuVung>,
        danhSachBoThe: List<BoThe>
    ): List<KetQuaTimKiemTuVung> {
        val tenBoTheTheoId = danhSachBoThe.associate { it.id to it.tenBoThe }
        return danhSachTuVung.map { tuVung -> tuVung.thanhKetQuaTimKiem(tenBoTheTheoId) }
    }

    private fun TuVung.thanhKetQuaTimKiem(tenBoTheTheoId: Map<Long, String>): KetQuaTimKiemTuVung {
        return KetQuaTimKiemTuVung(
            id = id,
            boTheId = boTheId,
            tu = tu,
            nghia = nghia,
            phienAm = phienAm,
            viDu = viDu,
            tenBoThe = tenBoTheTheoId[boTheId] ?: "Bo the #$boTheId"
        )
    }
}
