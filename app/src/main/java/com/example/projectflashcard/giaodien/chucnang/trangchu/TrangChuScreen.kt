package com.example.projectflashcard.giaodien.chucnang.trangchu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.chude.Cam
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.chude.DoNhe
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import com.example.projectflashcard.giaodien.chude.XanhChuDao
import com.example.projectflashcard.giaodien.chude.XanhLa
import com.example.projectflashcard.giaodien.thanhphan.NutChucNang
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe

private data class ThongKeTrangChu(
    val nhan: String,
    val giaTri: String,
    val moTa: String,
    val mauNhan: Color
)

@Composable
fun TrangChuScreen(
    onEvent: (TrangChuEvent) -> Unit,
    viewModel: TrangChuViewModel = viewModel()
) {
    TrangChuNoiDung(
        uiState = viewModel.uiState,
        onEvent = onEvent
    )
}

@Composable
private fun TrangChuNoiDung(
    uiState: TrangChuUiState,
    onEvent: (TrangChuEvent) -> Unit
) {
    val thongKe = listOf(
        ThongKeTrangChu("Bộ thẻ", uiState.tongSoBoThe.toString(), "Bộ thẻ đang học", XanhChuDao),
        ThongKeTrangChu("Từ vựng", uiState.tongSoTuVung.toString(), "Tổng số từ đã lưu", XanhLa),
        ThongKeTrangChu("Đã thuộc", uiState.soTuDaThuoc.toString(), "Từ đã ghi nhớ", Cam),
        ThongKeTrangChu("Cần ôn hôm nay", uiState.soTuCanOnHomNay.toString(), "Số từ cần ôn trong ngày", DoNhe)
    )
    val chucNang = listOf(
        Triple("Quản lý bộ thẻ", "Xem, tạo và mở chi tiết từng bộ thẻ", TrangChuEvent.MoQuanLyBoThe),
        Triple("Ôn tập hôm nay", "Ôn các từ cần học trong hôm nay", TrangChuEvent.MoOnTapHomNay),
        Triple("Thống kê học tập", "Theo dõi tiến độ và kết quả học tập", TrangChuEvent.MoThongKeHocTap),
        Triple("Tìm kiếm từ vựng", "Tìm trong danh sách từ vựng đã lưu", TrangChuEvent.MoTimKiemTuVung),
        Triple("Cài đặt", "Điều chỉnh mục tiêu học và giao diện", TrangChuEvent.MoCaiDat),
        Triple("Giới thiệu ứng dụng", "Thông tin về LearnFlash", TrangChuEvent.MoGioiThieu)
    )

    Scaffold(
        topBar = { ThanhTieuDe(tieuDe = "LearnFlash") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(KichThuocUi.khoangCachLon),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "LearnFlash",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Học từ vựng bằng Flashcard và lặp ngắt quãng",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Text(
                    text = "Tổng quan học tập",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)) {
                        TheThongKeNho(thongKe = thongKe[0], modifier = Modifier.weight(1f))
                        TheThongKeNho(thongKe = thongKe[1], modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)) {
                        TheThongKeNho(thongKe = thongKe[2], modifier = Modifier.weight(1f))
                        TheThongKeNho(thongKe = thongKe[3], modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Text(
                    text = "Chức năng",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(chucNang) { item ->
                NutChucNang(
                    tieuDe = item.first,
                    moTa = item.second,
                    onClick = { onEvent(item.third) }
                )
            }
        }
    }
}

@Composable
private fun TheThongKeNho(
    thongKe: ThongKeTrangChu,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 116.dp),
        shape = RoundedCornerShape(KichThuocUi.boGocThe),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = KichThuocUi.doCaoThe)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachNho)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(thongKe.mauNhan, CircleShape)
            )
            Text(
                text = thongKe.giaTri,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = thongKe.mauNhan
            )
            Text(
                text = thongKe.nhan,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = thongKe.moTa,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun TrangChuScreenPreview() {
    ChuDeLearnFlash {
        TrangChuNoiDung(
            uiState = TrangChuUiState(),
            onEvent = {}
        )
    }
}
