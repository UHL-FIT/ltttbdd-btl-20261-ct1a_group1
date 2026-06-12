package com.example.projectflashcard.giaodien.chucnang.chitietbothe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.chude.ChuDeLearnFlash
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import com.example.projectflashcard.giaodien.thanhphan.HopThoaiXacNhan
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe
import com.example.projectflashcard.giaodien.thanhphan.TrangThaiRong
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung

@Composable
fun ChiTietBoTheScreen(
    boTheId: Int,
    onQuayLai: () -> Unit,
    onThemTuVung: (Int) -> Unit,
    onSuaTuVung: (Int, Long) -> Unit,
    onOnTapBoThe: (Int) -> Unit,
    viewModel: ChiTietBoTheViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(boTheId) {
        viewModel.taiBoThe(boTheId)
    }

    ChiTietBoTheNoiDung(
        uiState = uiState,
        onQuayLai = onQuayLai,
        onEvent = viewModel::xuLySuKien,
        onThemTuVung = {
            viewModel.xuLySuKien(ChiTietBoTheEvent.BamThemTuVung)
            onThemTuVung(boTheId)
        },
        onOnTapBoThe = {
            viewModel.xuLySuKien(ChiTietBoTheEvent.BamOnTap)
            onOnTapBoThe(boTheId)
        },
        onSuaTuVung = { tuVung ->
            viewModel.xuLySuKien(ChiTietBoTheEvent.BamSuaTuVung(tuVung))
            onSuaTuVung(boTheId, tuVung.id)
        }
    )
}

@Composable
private fun ChiTietBoTheNoiDung(
    uiState: ChiTietBoTheUiState,
    onQuayLai: () -> Unit,
    onEvent: (ChiTietBoTheEvent) -> Unit,
    onThemTuVung: () -> Unit,
    onOnTapBoThe: () -> Unit,
    onSuaTuVung: (MucTuVung) -> Unit
) {
    Scaffold(
        topBar = {
            ThanhTieuDe(
                tieuDe = uiState.tenBoThe.ifBlank { "Chi tiết bộ thẻ" },
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
                Text(
                    text = "Danh sách từ vựng",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.tuKhoaTimKiem,
                    onValueChange = {
                        onEvent(ChiTietBoTheEvent.ThayDoiTuKhoaTimKiem(it))
                    },
                    label = { Text(text = "Tìm từ vựng trong bộ thẻ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachNho)
                ) {
                    items(BoLocTuVung.entries.toList()) { boLoc ->
                        val dangChon = uiState.boLocDangChon == boLoc
                        if (dangChon) {
                            Button(
                                onClick = {
                                    onEvent(ChiTietBoTheEvent.ChonBoLoc(boLoc))
                                }
                            ) {
                                Text(text = boLoc.tieuDe)
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    onEvent(ChiTietBoTheEvent.ChonBoLoc(boLoc))
                                }
                            ) {
                                Text(text = boLoc.tieuDe)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)
                ) {
                    Button(
                        onClick = onThemTuVung,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Thêm từ vựng")
                    }
                    OutlinedButton(
                        onClick = onOnTapBoThe,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Ôn tập")
                    }
                }
            }

            if (uiState.danhSachHienThi.isEmpty()) {
                item {
                    TrangThaiRong(
                        tieuDe = "Chưa có từ vựng",
                        moTa = "Không tìm thấy từ vựng phù hợp trong bộ thẻ này."
                    )
                }
            } else {
                items(
                    items = uiState.danhSachHienThi,
                    key = { it.id }
                ) { tuVung ->
                    MucTuVungTrongBoThe(
                        tuVung = tuVung,
                        onSua = { onSuaTuVung(tuVung) },
                        onXoa = {
                            onEvent(ChiTietBoTheEvent.BamXoaTuVung(tuVung))
                        }
                    )
                }
            }
        }
    }

    uiState.tuVungDangXoa?.let {
        HopThoaiXacNhan(
            tieuDe = "Xóa từ vựng?",
            noiDung = "Bạn có chắc muốn xóa từ này khỏi bộ thẻ không?",
            chuXacNhan = "Xóa",
            chuHuy = "Hủy",
            onXacNhan = {
                onEvent(ChiTietBoTheEvent.XacNhanXoaTuVung)
            },
            onHuy = {
                onEvent(ChiTietBoTheEvent.HuyXoaTuVung)
            }
        )
    }
}

@Composable
private fun MucTuVungTrongBoThe(
    tuVung: MucTuVung,
    onSua: () -> Unit,
    onXoa: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachNho)
        ) {
            Text(
                text = tuVung.tu,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${tuVung.phienAm} - ${tuVung.nghia}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = tuVung.viDu,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = taoNhanTrangThai(tuVung),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)
            ) {
                OutlinedButton(
                    onClick = onSua,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Sửa")
                }
                OutlinedButton(
                    onClick = onXoa,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Xóa")
                }
            }
        }
    }
}

private fun taoNhanTrangThai(tuVung: MucTuVung): String {
    val trangThai = when (tuVung.trangThai) {
        TrangThaiTuVung.MOI_HOC -> "Mới học"
        TrangThaiTuVung.DANG_HOC -> "Đang học"
        TrangThaiTuVung.DA_THUOC -> "Đã thuộc"
    }

    return if (tuVung.canOnHomNay) {
        "$trangThai - Cần ôn hôm nay"
    } else {
        trangThai
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
private fun ChiTietBoTheScreenPreview() {
    ChuDeLearnFlash {
        ChiTietBoTheNoiDung(
            uiState = ChiTietBoTheUiState(
                boTheId = 1,
                tenBoThe = "English A1",
                danhSachTuVung = emptyList(),
                danhSachHienThi = emptyList()
            ),
            onQuayLai = {},
            onEvent = {},
            onThemTuVung = {},
            onOnTapBoThe = {},
            onSuaTuVung = {}
        )
    }
}
