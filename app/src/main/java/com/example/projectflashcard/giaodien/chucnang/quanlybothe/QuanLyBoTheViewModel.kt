package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kiemtra.KiemTraBoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class QuanLyBoTheViewModel(application: Application) : AndroidViewModel(application) {

    private val kho: KhoFlashcard
    private val _uiState = MutableStateFlow(QuanLyBoTheUiState())
    val uiState: StateFlow<QuanLyBoTheUiState> = _uiState.asStateFlow()

    private val _tuKhoa = MutableStateFlow("")

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())

        viewModelScope.launch {
            _tuKhoa
                .flatMapLatest { tuKhoa ->
                    if (tuKhoa.isBlank()) kho.layTatCaBoThe()
                    else kho.timBoTheoTen(tuKhoa)
                }
                .collect { danhSach ->
                    _uiState.update { it.copy(danhSach = danhSach, dangTai = false) }
                }
        }
    }

    fun xuLySuKien(suKien: QuanLyBoTheEvent) {
        when (suKien) {
            QuanLyBoTheEvent.TaiDanhSach -> Unit
            is QuanLyBoTheEvent.DoiTuKhoa -> doiTuKhoa(suKien.tuKhoa)
            QuanLyBoTheEvent.MoHopThoaiThem -> moHopThoaiThem()
            is QuanLyBoTheEvent.MoHopThoaiSua -> moHopThoaiSua(suKien.boThe)
            is QuanLyBoTheEvent.LuuBoThe -> luuBoThe(suKien.tenBoThe, suKien.moTa)
            QuanLyBoTheEvent.HuyHopThoai -> huyHopThoai()
            is QuanLyBoTheEvent.YeuCauXoa -> yeuCauXoa(suKien.boThe)
            QuanLyBoTheEvent.XacNhanXoa -> xacNhanXoa()
            QuanLyBoTheEvent.QuayLai,
            is QuanLyBoTheEvent.MoChiTiet -> Unit
        }
    }

    private fun doiTuKhoa(tuKhoa: String) {
        _uiState.update { it.copy(tuKhoaTimKiem = tuKhoa, dangTai = true) }
        _tuKhoa.value = tuKhoa
    }

    private fun moHopThoaiThem() {
        _uiState.update { it.copy(hienThiHopThoai = true, boTheChinhSua = null, thongBaoLoi = null) }
    }

    private fun moHopThoaiSua(boThe: BoThe) {
        _uiState.update { it.copy(hienThiHopThoai = true, boTheChinhSua = boThe, thongBaoLoi = null) }
    }

    private fun luuBoThe(tenBoThe: String, moTa: String) {
        val tenTrimmed = tenBoThe.trim()
        val boTheChinhSua = _uiState.value.boTheChinhSua

        val loiKiemTra = KiemTraBoThe.kiemTraTenBoThe(
            tenBoThe = tenTrimmed,
            danhSachBoThe = _uiState.value.danhSach,
            boTheChinhSua = boTheChinhSua
        )
        if (loiKiemTra != null) {
            _uiState.update { it.copy(thongBaoLoi = loiKiemTra) }
            return
        }

        viewModelScope.launch {
            if (boTheChinhSua == null) {
                kho.themBoThe(BoThe(tenBoThe = tenTrimmed, moTa = moTa))
            } else {
                kho.suaBoThe(boTheChinhSua.copy(tenBoThe = tenTrimmed, moTa = moTa))
            }
            _uiState.update { it.copy(hienThiHopThoai = false, boTheChinhSua = null, thongBaoLoi = null) }
        }
    }

    private fun huyHopThoai() {
        _uiState.update {
            it.copy(
                hienThiHopThoai = false,
                boTheChinhSua = null,
                hienThiXacNhanXoa = false,
                boTheSeXoa = null,
                thongBaoLoi = null
            )
        }
    }

    private fun yeuCauXoa(boThe: BoThe) {
        _uiState.update { it.copy(hienThiXacNhanXoa = true, boTheSeXoa = boThe) }
    }

    private fun xacNhanXoa() {
        val boThe = _uiState.value.boTheSeXoa ?: return
        viewModelScope.launch {
            kho.xoaBoThe(boThe)
            _uiState.update { it.copy(hienThiXacNhanXoa = false, boTheSeXoa = null) }
        }
    }
}
