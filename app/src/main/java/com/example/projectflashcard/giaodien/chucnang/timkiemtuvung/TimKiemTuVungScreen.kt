package com.example.projectflashcard.giaodien.chucnang.timkiemtuvung

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import com.example.projectflashcard.giaodien.thanhphan.DangTai
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe
import com.example.projectflashcard.giaodien.thanhphan.TrangThaiRong

@Composable
fun TimKiemTuVungScreen(
    onQuayLai: () -> Unit,
    viewModel: TimKiemTuVungViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TimKiemTuVungNoiDung(
        uiState = uiState,
        onQuayLai = onQuayLai,
        onEvent = viewModel::xuLySuKien
    )
}

@Composable
private fun TimKiemTuVungNoiDung(
    uiState: TimKiemTuVungUiState,
    onQuayLai: () -> Unit,
    onEvent: (TimKiemTuVungEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ThanhTieuDe(
                tieuDe = "Tìm kiếm từ vựng",
                coNutQuayLai = true,
                onQuayLai = onQuayLai
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(KichThuocUi.khoangCachLon),
            verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.tuKhoa,
                    onValueChange = {
                        onEvent(TimKiemTuVungEvent.ThayDoiTuKhoa(it))
                    },
                    label = { Text("Nhập từ tiếng Anh, nghĩa hoặc phiên âm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            when {
                uiState.tuKhoa.isBlank() -> {
                    item {
                        TrangThaiRong(
                            tieuDe = "Nhập từ khóa để tìm kiếm",
                            moTa = "Bạn có thể tìm theo từ tiếng Anh, nghĩa tiếng Việt hoặc phiên âm."
                        )
                    }
                }

                uiState.dangTai -> {
                    item {
                        DangTai(modifier = Modifier.fillMaxWidth())
                    }
                }

                uiState.ketQua.isEmpty() -> {
                    item {
                        TrangThaiRong(
                            tieuDe = "Không có kết quả",
                            moTa = "Không tìm thấy từ vựng phù hợp trong dữ liệu đã lưu."
                        )
                    }
                }

                else -> {
                    items(uiState.ketQua, key = { it.id }) { tuVung ->
                        MucKetQuaTimKiem(tuVung = tuVung)
                    }
                }
            }
        }
    }
}

@Composable
private fun MucKetQuaTimKiem(
    tuVung: KetQuaTimKiemTuVung,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(KichThuocUi.boGocThe),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = KichThuocUi.doCaoThe)
    ) {
        Column(
            modifier = Modifier.padding(KichThuocUi.khoangCachLon),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = tuVung.tu,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = listOf(tuVung.phienAm, tuVung.nghia)
                    .filter { it.isNotBlank() }
                    .joinToString(" - "),
                style = MaterialTheme.typography.bodyLarge
            )
            if (tuVung.viDu.isNotBlank()) {
                Text(
                    text = tuVung.viDu,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Bộ thẻ: ${tuVung.tenBoThe}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun TimKiemTuVungNoiDungPreview() {
    ChuDeLearnFlash {
        TimKiemTuVungNoiDung(
            uiState = TimKiemTuVungUiState(
                tuKhoa = "apple",
                ketQua = listOf(
                    KetQuaTimKiemTuVung(
                        id = 1,
                        boTheId = 1,
                        tu = "apple",
                        nghia = "quả táo",
                        phienAm = "/ˈæp.əl/",
                        viDu = "I eat an apple every morning.",
                        tenBoThe = "English A1"
                    )
                )
            ),
            onQuayLai = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun TimKiemTuVungHuongDanPreview() {
    ChuDeLearnFlash {
        TimKiemTuVungNoiDung(
            uiState = TimKiemTuVungUiState(),
            onQuayLai = {},
            onEvent = {}
        )
    }
}
