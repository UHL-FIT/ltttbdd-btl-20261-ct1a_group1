package com.example.projectflashcard.giaodien.chucnang.chitietbothe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChiTietBoTheViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoFlashcard
    private var congViecTaiBoThe: Job? = null

    private val _uiState = MutableStateFlow(
        ChiTietBoTheUiState(
            boTheId = 1,
            tenBoThe = "",
            dangTai = true
        )
    )
    val uiState: StateFlow<ChiTietBoTheUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(db.truyVanBoThe(), db.truyVanTuVung())
    }

    fun xuLySuKien(event: ChiTietBoTheEvent) {
        when (event) {
            is ChiTietBoTheEvent.ThayDoiTuKhoaTimKiem -> thayDoiTuKhoaTimKiem(event.tuKhoa)
            is ChiTietBoTheEvent.ChonBoLoc -> chonBoLoc(event.boLoc)
            ChiTietBoTheEvent.BamThemTuVung -> Unit
            ChiTietBoTheEvent.BamOnTap -> Unit
            is ChiTietBoTheEvent.BamSuaTuVung -> Unit
            is ChiTietBoTheEvent.BamXoaTuVung -> moXacNhanXoa(event.tuVung)
            ChiTietBoTheEvent.XacNhanXoaTuVung -> xacNhanXoaTuVung()
            ChiTietBoTheEvent.HuyXoaTuVung -> dongXacNhanXoa()
        }
    }

    fun taiBoThe(boTheId: Int) {
        congViecTaiBoThe?.cancel()
        _uiState.update { it.copy(boTheId = boTheId, dangTai = true) }

        val id = boTheId.toLong()
        congViecTaiBoThe = viewModelScope.launch {
            combine(
                kho.layBoTheTheoId(id),
                kho.layTuVungTheoBoThe(id)
            ) { boThe, danhSachTuVung ->
                boThe to danhSachTuVung
            }.collect { (boThe, danhSachTuVung) ->
                _uiState.update { state ->
                    val danhSach = danhSachTuVung.map { it.thanhMucTuVung() }
                    val stateMoi = state.copy(
                        boTheId = boTheId,
                        tenBoThe = boThe?.tenBoThe ?: "Bo the #$boTheId",
                        danhSachTuVung = danhSach,
                        dangTai = false
                    )
                    stateMoi.copy(danhSachHienThi = locDanhSach(stateMoi))
                }
            }
        }
    }

    private fun thayDoiTuKhoaTimKiem(tuKhoa: String) {
        _uiState.update { it.copy(tuKhoaTimKiem = tuKhoa) }
        capNhatDanhSachHienThi()
    }

    private fun chonBoLoc(boLoc: BoLocTuVung) {
        _uiState.update { it.copy(boLocDangChon = boLoc) }
        capNhatDanhSachHienThi()
    }

    private fun moXacNhanXoa(tuVung: MucTuVung) {
        _uiState.update { it.copy(tuVungDangXoa = tuVung) }
    }

    private fun dongXacNhanXoa() {
        _uiState.update { it.copy(tuVungDangXoa = null) }
    }

    private fun xacNhanXoaTuVung() {
        val tuCanXoa = _uiState.value.tuVungDangXoa ?: return
        viewModelScope.launch {
            kho.xoaTuVungTheoId(tuCanXoa.id)
            _uiState.update { it.copy(tuVungDangXoa = null) }
        }
    }

    private fun capNhatDanhSachHienThi() {
        _uiState.update { state ->
            state.copy(danhSachHienThi = locDanhSach(state))
        }
    }

    private fun locDanhSach(state: ChiTietBoTheUiState): List<MucTuVung> {
        val tuKhoa = state.tuKhoaTimKiem.trim()

        return state.danhSachTuVung
            .filter { it.boTheId == state.boTheId.toLong() }
            .filter { tuVung ->
                tuKhoa.isBlank() ||
                    tuVung.tu.contains(tuKhoa, ignoreCase = true) ||
                    tuVung.nghia.contains(tuKhoa, ignoreCase = true)
            }
            .filter { tuVung ->
                when (state.boLocDangChon) {
                    BoLocTuVung.TAT_CA -> true
                    BoLocTuVung.CHUA_THUOC -> tuVung.trangThai != TrangThaiTuVung.DA_THUOC
                    BoLocTuVung.DA_THUOC -> tuVung.trangThai == TrangThaiTuVung.DA_THUOC
                    BoLocTuVung.CAN_ON -> tuVung.canOnHomNay
                }
            }
    }

    private fun TuVung.thanhMucTuVung(): MucTuVung = MucTuVung(
        id = id,
        boTheId = boTheId,
        tu = tu,
        nghia = nghia,
        phienAm = phienAm,
        viDu = viDu,
        trangThai = trangThai,
        canOnHomNay = canOnHomNay
    )
}
