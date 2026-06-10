package com.example.projectflashcard.giaodien.chucnang.caidat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.caidat.LuuCaiDatNguoiDung
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuCaiDat
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.dulieu.nhapxuat.QuanLyNhapXuat
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

    private val quanLyNhapXuat: QuanLyNhapXuat

    private val _uiState = MutableStateFlow(CaiDatUiState())
    val uiState: StateFlow<CaiDatUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        quanLyNhapXuat = QuanLyNhapXuat(
            KhoDuLieuFlashcard(
                truyVanBoThe = db.truyVanBoThe(),
                truyVanTuVung = db.truyVanTuVung(),
                coSoDuLieu = db
            )
        )

        viewModelScope.launch {
            khoCaiDat.layCaiDatNguoiDung().collect { caiDat ->
                _uiState.update { trangThaiCu ->
                    trangThaiCu.copy(
                        mucTieuOnTapMoiNgay = caiDat.mucTieuOnTapMoiNgay,
                        cheDoGiaoDien = caiDat.cheDoGiaoDien,
                        dangTai = false,
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
            is CaiDatEvent.CapNhatNoiDungJsonNhap -> capNhatNoiDungJsonNhap(event.noiDung)
            CaiDatEvent.XuatJson -> xuatJson()
            CaiDatEvent.NhapJson -> nhapJson()
            CaiDatEvent.XoaJsonDaXuat -> xoaJsonDaXuat()
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
                        thongBaoThanhCong = "Đã lưu cài đặt"
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
                        thongBaoThanhCong = "Đã lưu cài đặt"
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

    private fun capNhatNoiDungJsonNhap(noiDung: String) {
        _uiState.update {
            it.copy(
                noiDungJsonNhap = noiDung,
                thongBaoLoi = null,
                thongBaoThanhCong = null
            )
        }
    }

    private fun xuatJson() {
        _uiState.update {
            it.copy(dangLuu = true, thongBaoLoi = null, thongBaoThanhCong = null)
        }

        viewModelScope.launch {
            quanLyNhapXuat.xuatDuLieuRaJson()
                .onSuccess { json ->
                    _uiState.update {
                        it.copy(
                            jsonDaXuat = json,
                            dangLuu = false,
                            thongBaoThanhCong = "Đã xuất dữ liệu"
                        )
                    }
                }
                .onFailure { loi ->
                    _uiState.update {
                        it.copy(
                            dangLuu = false,
                            thongBaoLoi = loi.message ?: "Không xuất được dữ liệu",
                            thongBaoThanhCong = null
                        )
                    }
                }
        }
    }

    private fun nhapJson() {
        val noiDungJson = _uiState.value.noiDungJsonNhap
        _uiState.update {
            it.copy(dangLuu = true, thongBaoLoi = null, thongBaoThanhCong = null)
        }

        viewModelScope.launch {
            quanLyNhapXuat.nhapDuLieuTuJson(noiDungJson)
                .onSuccess { ketQua ->
                    _uiState.update {
                        it.copy(
                            noiDungJsonNhap = "",
                            dangLuu = false,
                            thongBaoThanhCong = ketQua.thongBao
                        )
                    }
                }
                .onFailure { loi ->
                    _uiState.update {
                        it.copy(
                            dangLuu = false,
                            thongBaoLoi = loi.message ?: "Không nhập được dữ liệu",
                            thongBaoThanhCong = null
                        )
                    }
                }
        }
    }

    private fun xoaJsonDaXuat() {
        _uiState.update { it.copy(jsonDaXuat = "") }
    }

    private fun xoaThongBao() {
        _uiState.update {
            it.copy(thongBaoLoi = null, thongBaoThanhCong = null)
        }
    }
}
