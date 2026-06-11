package com.example.projectflashcard.giaodien.chucnang.thongkehoctap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectflashcard.dulieu.cucbo.cosodulieu.CoSoDuLieuLearnFlash
import com.example.projectflashcard.dulieu.khodulieu.KhoDuLieuFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.LichSuOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.ThongKeBoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung
import com.example.projectflashcard.nghiepvu.thongke.TinhThongKeHocTap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThongKeHocTapViewModel(application: Application) : AndroidViewModel(application) {
    private val kho: KhoDuLieuFlashcard
    private val _uiState = MutableStateFlow(ThongKeHocTapUiState())
    val uiState: StateFlow<ThongKeHocTapUiState> = _uiState.asStateFlow()

    init {
        val db = CoSoDuLieuLearnFlash.layInstance(application)
        kho = KhoDuLieuFlashcard(
            truyVanBoThe = db.truyVanBoThe(),
            truyVanTuVung = db.truyVanTuVung(),
            truyVanLichSuOnTap = db.truyVanLichSuOnTap()
        )

        viewModelScope.launch {
            combine(
                kho.layTatCaBoThe(),
                kho.layTatCaTuVung(),
                kho.layTatCaLichSuOnTap()
            ) { danhSachBoThe, danhSachTuVung, lichSuOnTap ->
                taoUiState(danhSachBoThe, danhSachTuVung, lichSuOnTap)
            }.collect { stateMoi ->
                _uiState.update { stateMoi }
            }
        }
    }

    private fun taoUiState(
        danhSachBoThe: List<BoThe>,
        danhSachTuVung: List<TuVung>,
        lichSuOnTap: List<LichSuOnTap>
    ): ThongKeHocTapUiState {
        val ketQua = TinhThongKeHocTap.tinh(
            danhSachTu = danhSachTuVung,
            lichSuOnTap = lichSuOnTap
        )
        val tuVungTheoBoThe = danhSachTuVung.groupBy { it.boTheId }
        val thongKeTheoBoThe = danhSachBoThe.map { boThe ->
            val tuVungCuaBo = tuVungTheoBoThe[boThe.id].orEmpty()
            ThongKeBoThe(
                boTheId = boThe.id,
                tenBoThe = boThe.tenBoThe,
                tongSoTu = tuVungCuaBo.size,
                soTuMoiHoc = tuVungCuaBo.count { it.trangThai == TrangThaiTuVung.MOI_HOC },
                soTuDangHoc = tuVungCuaBo.count { it.trangThai == TrangThaiTuVung.DANG_HOC },
                soTuDaThuoc = tuVungCuaBo.count { it.trangThai == TrangThaiTuVung.DA_THUOC }
            )
        }

        return ThongKeHocTapUiState(
            dangTai = false,
            tongSoBoThe = danhSachBoThe.size,
            tongSoTu = ketQua.tongSoTu,
            soTuDaThuoc = ketQua.soTuDaThuoc,
            soTuChuaThuoc = ketQua.soTuChuaThuoc,
            soTuCanOnHomNay = ketQua.soTuCanOnHomNay,
            tongLuotOn = ketQua.tongLuotOn,
            tyLeGhiNho = ketQua.tyLeHoanThanh,
            thongKeTheoBoThe = thongKeTheoBoThe
        )
    }
}
