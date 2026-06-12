package com.example.projectflashcard.giaodien.chucnang.thongkehoctap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.chude.Cam
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.chude.DoNhe
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import com.example.projectflashcard.giaodien.chude.XanhChuDao
import com.example.projectflashcard.giaodien.chude.XanhLa
import com.example.projectflashcard.giaodien.thanhphan.DangTai
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe
import com.example.projectflashcard.giaodien.thanhphan.TheThongKe
import com.example.projectflashcard.giaodien.thanhphan.TrangThaiRong
import com.example.projectflashcard.nghiepvu.kieudulieu.ThongKeBoThe

@Composable
fun ThongKeHocTapScreen(
    onQuayLai: () -> Unit,
    viewModel: ThongKeHocTapViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ThongKeHocTapNoiDung(
        uiState = uiState,
        onQuayLai = onQuayLai
    )
}

@Composable
private fun ThongKeHocTapNoiDung(
    uiState: ThongKeHocTapUiState,
    onQuayLai: () -> Unit
) {
    Scaffold(
        topBar = {
            ThanhTieuDe(
                tieuDe = "Thống kê học tập",
                coNutQuayLai = true,
                onQuayLai = onQuayLai
            )
        }
    ) { innerPadding ->
        when {
            uiState.dangTai -> DangTai(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            uiState.tongSoBoThe == 0 && uiState.tongSoTu == 0 -> TrangThaiRong(
                tieuDe = "Chưa có dữ liệu thống kê",
                moTa = "Hãy tạo bộ thẻ và thêm từ vựng để bắt đầu theo dõi tiến độ học tập.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            else -> DanhSachThongKe(
                uiState = uiState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun DanhSachThongKe(
    uiState: ThongKeHocTapUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(KichThuocUi.khoangCachLon),
        verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)
    ) {
        item {
            Text(
                text = "Tổng quan tiến độ",
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            TheThongKe(
                nhan = "Tỷ lệ ghi nhớ",
                giaTri = "${uiState.tyLeGhiNho}%",
                moTa = "${uiState.soTuDaThuoc} / ${uiState.tongSoTu} từ đã thuộc",
                mauNhan = XanhLa
            )
        }
        item {
            TheThongKe(
                nhan = "Tổng số bộ thẻ",
                giaTri = uiState.tongSoBoThe.toString(),
                moTa = "Số bộ thẻ đang có trong ứng dụng",
                mauNhan = XanhChuDao
            )
        }
        item {
            TheThongKe(
                nhan = "Tổng số từ vựng",
                giaTri = uiState.tongSoTu.toString(),
                moTa = "${uiState.soTuChuaThuoc} từ chưa thuộc",
                mauNhan = Cam
            )
        }
        item {
            TheThongKe(
                nhan = "Cần ôn hôm nay",
                giaTri = uiState.soTuCanOnHomNay.toString(),
                moTa = "Từ chưa thuộc và đang được đánh dấu cần ôn",
                mauNhan = DoNhe
            )
        }
        item {
            TheThongKe(
                nhan = "Tổng lượt ôn tập",
                giaTri = uiState.tongLuotOn.toString(),
                moTa = "Số bản ghi trong lịch sử ôn tập",
                mauNhan = XanhChuDao
            )
        }

        if (uiState.thongKeTheoBoThe.isNotEmpty()) {
            item {
                Text(
                    text = "Thống kê theo bộ thẻ",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(uiState.thongKeTheoBoThe, key = { it.boTheId }) { thongKe ->
                MucThongKeBoThe(thongKe = thongKe)
            }
        }
    }
}

@Composable
private fun MucThongKeBoThe(
    thongKe: ThongKeBoThe,
    modifier: Modifier = Modifier
) {
    TheThongKe(
        nhan = thongKe.tenBoThe,
        giaTri = "${thongKe.tyLeGhiNho}%",
        moTa = "${thongKe.soTuDaThuoc} / ${thongKe.tongSoTu} từ đã thuộc",
        mauNhan = XanhLa,
        modifier = modifier
    )
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun ThongKeHocTapNoiDungPreview() {
    ChuDeLearnFlash {
        ThongKeHocTapNoiDung(
            uiState = ThongKeHocTapUiState(
                dangTai = false,
                tongSoBoThe = 2,
                tongSoTu = 10,
                soTuDaThuoc = 4,
                soTuChuaThuoc = 6,
                soTuCanOnHomNay = 3,
                tyLeGhiNho = 40,
                thongKeTheoBoThe = listOf(
                    ThongKeBoThe(1, "Tiếng Anh giao tiếp", 6, soTuDaThuoc = 3),
                    ThongKeBoThe(2, "TOEIC", 4, soTuDaThuoc = 1)
                )
            ),
            onQuayLai = {}
        )
    }
}
