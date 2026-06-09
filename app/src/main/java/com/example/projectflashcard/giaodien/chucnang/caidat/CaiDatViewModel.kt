package com.example.projectflashcard.giaodien.chucnang.caidat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.caidat.LuuCaiDatNguoiDung
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuCaiDat
import com.example.projectflashcard.nghiepvu.khodulieu.KhoCaiDat
import com.example.projectflashcard.nghiepvu.kiemtra.KiemTraCaiDat
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CaiDatViewModel(application: Application) : AndroidViewModel(application) {

    private val khoCaiDat: KhoCaiDat = KhoDuLieuCaiDat(
        LuuCaiDatNguoiDung(application.applicationContext)
    )

    private val _uiState = MutableStateFlow(CaiDatUiState())
    val uiState: StateFlow<CaiDatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            khoCaiDat.layCaiDatNguoiDung().collect { caiDat ->
                _uiState.update { trangThaiCu ->
                    trangThaiCu.copy(
                        mucTieuOnTapMoiNgay = caiDat.mucTieuOnTapMoiNgay,
                        cheDoGiaoDien = caiDat.cheDoGiaoDien,
                        dangTai = false,
                        dangLuu = false,
                        thongBaoLoi = null
                    )
                }
            }
        }
    }

    fun xuLySuKien(event: CaiDatEvent) {
        when (event) {
            is CaiDatEvent.DoiMucTieuOnTapMoiNgay -> doiMucTieuOnTapMoiNgay(event.mucTieu)
            is CaiDatEvent.DoiCheDoGiaoDien -> doiCheDoGiaoDien(event.cheDoGiaoDien)
            CaiDatEvent.XoaThongBao -> xoaThongBao()
        }
    }

    private fun doiMucTieuOnTapMoiNgay(mucTieu: Int) {
        val mucTieuDaLamTron = KiemTraCaiDat.lamTronTheoBuoc(mucTieu)
        val loi = KiemTraCaiDat.kiemTraMucTieuOnTapMoiNgay(mucTieuDaLamTron)
        if (loi != null) {
            _uiState.update {
                it.copy(thongBaoLoi = loi, thongBaoThanhCong = null, dangLuu = false)
            }
            return
        }

        _uiState.update {
            it.copy(
                mucTieuOnTapMoiNgay = mucTieuDaLamTron,
                dangLuu = true,
                thongBaoLoi = null,
                thongBaoThanhCong = null
            )
        }

        viewModelScope.launch {
            runCatching {
                khoCaiDat.luuMucTieuOnTapMoiNgay(mucTieuDaLamTron)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoThanhCong = "Đã lưu mục tiêu ${mucTieuDaLamTron} từ mỗi ngày"
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoLoi = "Không lưu được mục tiêu ôn tập. Vui lòng thử lại.",
                        thongBaoThanhCong = null
                    )
                }
            }
        }
    }

    private fun doiCheDoGiaoDien(cheDoGiaoDien: CheDoGiaoDien) {
        _uiState.update {
            it.copy(
                cheDoGiaoDien = cheDoGiaoDien,
                dangLuu = true,
                thongBaoLoi = null,
                thongBaoThanhCong = null
            )
        }

        viewModelScope.launch {
            runCatching {
                khoCaiDat.luuCheDoGiaoDien(cheDoGiaoDien)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoThanhCong = "Đã lưu chế độ ${cheDoGiaoDien.tenHienThi}"
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        dangLuu = false,
                        thongBaoLoi = "Không lưu được chế độ giao diện. Vui lòng thử lại.",
                        thongBaoThanhCong = null
                    )
                }
            }
        }
    }

    private fun xoaThongBao() {
        _uiState.update {
            it.copy(thongBaoLoi = null, thongBaoThanhCong = null)
        }
    }
}
