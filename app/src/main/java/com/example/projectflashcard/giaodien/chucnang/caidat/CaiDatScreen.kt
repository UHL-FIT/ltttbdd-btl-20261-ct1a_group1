package com.example.projectflashcard.giaodien.chucnang.caidat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe
import com.example.projectflashcard.nghiepvu.kiemtra.KiemTraCaiDat
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien
import kotlin.math.roundToInt

@Composable
fun CaiDatScreen(
    onQuayLai: () -> Unit,
    viewModel: CaiDatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CaiDatNoiDung(
        uiState = uiState,
        onQuayLai = onQuayLai,
        onEvent = viewModel::xuLySuKien
    )
}

@Composable
private fun CaiDatNoiDung(
    uiState: CaiDatUiState,
    onQuayLai: () -> Unit,
    onEvent: (CaiDatEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ThanhTieuDe(
                tieuDe = "Cài đặt",
                coNutQuayLai = true,
                onQuayLai = onQuayLai
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                TheMucTieuOnTap(
                    mucTieu = uiState.mucTieuOnTapMoiNgay,
                    dangKhoa = uiState.dangTai || uiState.dangLuu,
                    onDoiMucTieu = { mucTieu ->
                        onEvent(CaiDatEvent.DoiMucTieuOnTapMoiNgay(mucTieu))
                    }
                )
            }

            item {
                TheCheDoGiaoDien(
                    cheDoDangChon = uiState.cheDoGiaoDien,
                    dangKhoa = uiState.dangTai || uiState.dangLuu,
                    onDoiCheDo = { cheDo ->
                        onEvent(CaiDatEvent.DoiCheDoGiaoDien(cheDo))
                    }
                )
            }

            if (uiState.dangTai || uiState.dangLuu) {
                item { TrangThaiDangLuu(dangTai = uiState.dangTai) }
            }

            uiState.thongBaoThanhCong?.let { thongBao ->
                item {
                    Text(
                        text = thongBao,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            uiState.thongBaoLoi?.let { loi ->
                item {
                    Text(
                        text = loi,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun TheMucTieuOnTap(
    mucTieu: Int,
    dangKhoa: Boolean,
    onDoiMucTieu: (Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Mục tiêu ôn tập mỗi ngày",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$mucTieu từ / ngày",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = mucTieu.toFloat(),
                onValueChange = { giaTri ->
                    val mucTieuMoi = (giaTri / KiemTraCaiDat.BUOC_MUC_TIEU)
                        .roundToInt() * KiemTraCaiDat.BUOC_MUC_TIEU
                    onDoiMucTieu(mucTieuMoi)
                },
                valueRange = KiemTraCaiDat.MUC_TIEU_TOI_THIEU.toFloat()..KiemTraCaiDat.MUC_TIEU_TOI_DA.toFloat(),
                steps = 8,
                enabled = !dangKhoa
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 20, 30).forEach { goiY ->
                    AssistChip(
                        onClick = { onDoiMucTieu(goiY) },
                        label = { Text("$goiY từ") },
                        enabled = !dangKhoa
                    )
                }
            }
        }
    }
}

@Composable
private fun TheCheDoGiaoDien(
    cheDoDangChon: CheDoGiaoDien,
    dangKhoa: Boolean,
    onDoiCheDo: (CheDoGiaoDien) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Chế độ giao diện",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Chọn Sáng, Tối hoặc Theo hệ thống.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            CheDoGiaoDien.entries.forEachIndexed { index, cheDo ->
                LuaChonCheDoGiaoDien(
                    cheDo = cheDo,
                    duocChon = cheDoDangChon == cheDo,
                    dangKhoa = dangKhoa,
                    onClick = { onDoiCheDo(cheDo) }
                )
                if (index < CheDoGiaoDien.entries.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LuaChonCheDoGiaoDien(
    cheDo: CheDoGiaoDien,
    duocChon: Boolean,
    dangKhoa: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !dangKhoa, onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = duocChon,
            onClick = onClick,
            enabled = !dangKhoa
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = cheDo.tenHienThi,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (duocChon) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(
                text = cheDo.moTa,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TrangThaiDangLuu(dangTai: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.width(20.dp).height(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (dangTai) "Đang tải cài đặt..." else "Đang lưu thay đổi...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun CaiDatScreenPreview() {
    ChuDeLearnFlash {
        CaiDatNoiDung(
            uiState = CaiDatUiState(
                mucTieuOnTapMoiNgay = 20,
                cheDoGiaoDien = CheDoGiaoDien.HE_THONG,
                dangTai = false
            ),
            onQuayLai = {},
            onEvent = {}
        )
    }
}
