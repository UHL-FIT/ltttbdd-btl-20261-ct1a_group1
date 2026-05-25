package com.example.projectflashcard.giaodien.chucnang.chitietbothe

import androidx.lifecycle.ViewModel
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChiTietBoTheViewModel : ViewModel() {
    private val danhSachGoc = MutableStateFlow(taoDanhSachTuVungMau())

    private val _uiState = MutableStateFlow(
        ChiTietBoTheUiState(
            boTheId = 1,
            tenBoThe = "English A1",
            danhSachTuVung = danhSachGoc.value,
            danhSachHienThi = danhSachGoc.value
        )
    )
    val uiState: StateFlow<ChiTietBoTheUiState> = _uiState.asStateFlow()

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
        val tenBoThe = when (boTheId) {
            2 -> "Giao tiếp hằng ngày"
            3 -> "Từ vựng công nghệ"
            else -> "English A1"
        }

        _uiState.update { state ->
            state.copy(
                boTheId = boTheId,
                tenBoThe = tenBoThe
            )
        }
        capNhatDanhSachHienThi()
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
        danhSachGoc.update { danhSach ->
            danhSach.filterNot { it.id == tuCanXoa.id }
        }
        _uiState.update {
            it.copy(
                danhSachTuVung = danhSachGoc.value,
                tuVungDangXoa = null
            )
        }
        capNhatDanhSachHienThi()
    }

    private fun capNhatDanhSachHienThi() {
        val state = _uiState.value
        val tuKhoa = state.tuKhoaTimKiem.trim()

        val danhSachDaLoc = danhSachGoc.value
            .filter { it.boTheId == state.boTheId }
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

        _uiState.update {
            it.copy(
                danhSachTuVung = danhSachGoc.value,
                danhSachHienThi = danhSachDaLoc
            )
        }
    }

    private fun taoDanhSachTuVungMau(): List<MucTuVung> {
        // Dữ liệu mẫu tạm thời, sẽ thay bằng Repository/Room ở các bước sau.
        return listOf(
            MucTuVung(
                id = 1,
                boTheId = 1,
                tu = "Apple",
                nghia = "quả táo",
                phienAm = "/ˈæp.əl/",
                viDu = "I eat an apple every morning.",
                trangThai = TrangThaiTuVung.MOI_HOC,
                canOnHomNay = true
            ),
            MucTuVung(
                id = 2,
                boTheId = 1,
                tu = "Improve",
                nghia = "cải thiện",
                phienAm = "/ɪmˈpruːv/",
                viDu = "Practice helps you improve.",
                trangThai = TrangThaiTuVung.DANG_HOC,
                canOnHomNay = true
            ),
            MucTuVung(
                id = 3,
                boTheId = 1,
                tu = "Memory",
                nghia = "trí nhớ",
                phienAm = "/ˈmem.ər.i/",
                viDu = "Flashcards support long-term memory.",
                trangThai = TrangThaiTuVung.DA_THUOC,
                canOnHomNay = false
            ),
            MucTuVung(
                id = 4,
                boTheId = 1,
                tu = "Review",
                nghia = "ôn tập",
                phienAm = "/rɪˈvjuː/",
                viDu = "Review your cards every day.",
                trangThai = TrangThaiTuVung.DANG_HOC,
                canOnHomNay = true
            )
        )
    }
}
