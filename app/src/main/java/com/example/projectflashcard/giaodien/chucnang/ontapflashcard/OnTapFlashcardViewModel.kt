package com.example.projectflashcard.giaodien.chucnang.ontapflashcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.LichSuOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.MucDoOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import com.example.projectflashcard.nghiepvu.laplai.TinhLichLapLai
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnTapFlashcardViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard
    private var congViecTai: Job? = null
    private var danhSachTuVungGoc: List<TuVung> = emptyList()
    private var boTheIdHienTai: Int? = null

    private val _uiState = MutableStateFlow(OnTapFlashcardUiState())
    val uiState: StateFlow<OnTapFlashcardUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(
            truyVanBoThe = db.truyVanBoThe(),
            truyVanTuVung = db.truyVanTuVung(),
            truyVanLichSuOnTap = db.truyVanLichSuOnTap()
        )
    }

    fun xuLySuKien(event: OnTapFlashcardEvent) {
        when (event) {
            OnTapFlashcardEvent.LatThe -> latThe()
            is OnTapFlashcardEvent.DanhGia -> danhGia(event.mucDo)
            OnTapFlashcardEvent.OnTapLai -> batDauPhien(boTheIdHienTai)
        }
    }

    fun batDauPhien(boTheId: Int?) {
        congViecTai?.cancel()
        boTheIdHienTai = boTheId
        _uiState.value = OnTapFlashcardUiState(dangTai = true)

        congViecTai = viewModelScope.launch {
            runCatching {
                layDuLieuPhienOnTap(boTheId)
            }.onSuccess { (tenPhien, danhSachCanOn) ->
                hienThiPhienOnTap(tenPhien, danhSachCanOn)
            }.onFailure { loi ->
                _uiState.update {
                    it.copy(
                        dangTai = false,
                        thongBaoLoi = loi.message ?: "Khong tai duoc phien on tap"
                    )
                }
            }
        }
    }

    private suspend fun layDuLieuPhienOnTap(boTheId: Int?): Pair<String, List<TuVung>> {
        val tenPhien: String
        val danhSachTuVung: List<TuVung>

        if (boTheId == null) {
            tenPhien = "On tap hom nay"
            danhSachTuVung = kho.layTatCaTuVung().first()
        } else {
            val boThe = kho.layBoTheTheoId(boTheId.toLong()).first()
            tenPhien = boThe?.tenBoThe ?: "Bo the #$boTheId"
            danhSachTuVung = kho.layTuVungTheoBoThe(boTheId.toLong()).first()
        }

        return tenPhien to danhSachTuVung.filter { it.canOnHomNay }
    }

    private fun hienThiPhienOnTap(tenPhien: String, danhSachCanOn: List<TuVung>) {
        danhSachTuVungGoc = danhSachCanOn
        _uiState.update {
            it.copy(
                tenPhienOnTap = tenPhien,
                dangTai = false,
                danhSachThe = danhSachCanOn.map { tuVung -> tuVung.thanhMucOnTap() },
                thongBaoLoi = null
            )
        }
    }

    private fun latThe() {
        _uiState.update { it.copy(daLatThe = !it.daLatThe) }
    }

    private fun danhGia(mucDo: MucDoOnTap) {
        val state = _uiState.value
        if (!state.daLatThe || state.dangXuLy) return

        val tuVung = danhSachTuVungGoc.getOrNull(state.viTriHienTai) ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(dangXuLy = true, thongBaoLoi = null) }

            runCatching {
                luuKetQuaOnTap(tuVung, mucDo)
            }.onSuccess {
                chuyenSangTheTiepTheo(mucDo)
            }.onFailure { loi ->
                _uiState.update {
                    it.copy(
                        dangXuLy = false,
                        thongBaoLoi = loi.message ?: "Khong luu duoc ket qua on tap"
                    )
                }
            }
        }
    }

    private suspend fun luuKetQuaOnTap(tuVung: TuVung, mucDo: MucDoOnTap) {
        val thoiDiemOn = System.currentTimeMillis()
        val soLanNhoTot = demSoLanNhoTot(tuVung)
        val ketQua = TinhLichLapLai.tinh(
            soLanNhoTotHienTai = soLanNhoTot,
            mucDoOnTap = mucDo,
            thoiDiemOn = thoiDiemOn
        )

        kho.suaTuVung(
            tuVung.copy(
                trangThai = ketQua.trangThaiMoi,
                canOnHomNay = mucDo == MucDoOnTap.DA_QUEN || mucDo == MucDoOnTap.KHO_NHO
            )
        )
        kho.themLichSuOnTap(
            LichSuOnTap(
                tuVungId = tuVung.id,
                boTheId = tuVung.boTheId,
                mucDoOnTap = mucDo,
                ngayOn = thoiDiemOn
            )
        )
    }

    private suspend fun demSoLanNhoTot(tuVung: TuVung): Int {
        return kho.layLichSuOnTapTheoTuVung(tuVung.id)
            .first()
            .count { it.mucDoOnTap == MucDoOnTap.NHO_DUOC || it.mucDoOnTap == MucDoOnTap.RAT_DE }
    }

    private fun chuyenSangTheTiepTheo(mucDo: MucDoOnTap) {
        _uiState.update { hienTai ->
            val viTriTiepTheo = hienTai.viTriHienTai + 1
            hienTai.copy(
                viTriHienTai = viTriTiepTheo,
                daLatThe = false,
                dangXuLy = false,
                daHoanThanh = viTriTiepTheo >= hienTai.tongSoThe,
                soDaQuen = hienTai.soDaQuen + if (mucDo == MucDoOnTap.DA_QUEN) 1 else 0,
                soKhoNho = hienTai.soKhoNho + if (mucDo == MucDoOnTap.KHO_NHO) 1 else 0,
                soNhoDuoc = hienTai.soNhoDuoc + if (mucDo == MucDoOnTap.NHO_DUOC) 1 else 0,
                soRatDe = hienTai.soRatDe + if (mucDo == MucDoOnTap.RAT_DE) 1 else 0
            )
        }
    }

    private fun TuVung.thanhMucOnTap(): MucOnTap = MucOnTap(
        id = id,
        tu = tu,
        nghia = nghia,
        phienAm = phienAm,
        viDu = viDu
    )
}
