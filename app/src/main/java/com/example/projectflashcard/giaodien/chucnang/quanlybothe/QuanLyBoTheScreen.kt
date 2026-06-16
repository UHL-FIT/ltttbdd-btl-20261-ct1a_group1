package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectflashcard.giaodien.thanhphan.DangTai
import com.example.projectflashcard.giaodien.thanhphan.HopThoaiXacNhan
import com.example.projectflashcard.giaodien.thanhphan.ThanhTieuDe
import com.example.projectflashcard.giaodien.thanhphan.TrangThaiRong

@Composable
fun QuanLyBoTheScreen(
    viewModel: QuanLyBoTheViewModel = viewModel(),
    onEvent: (QuanLyBoTheEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.hienThiHopThoai) {
        HopThoaiBoThe(
            boTheChinhSua = uiState.boTheChinhSua,
            thongBaoLoi = uiState.thongBaoLoi,
            onLuu = { ten, moTa ->
                viewModel.xuLySuKien(QuanLyBoTheEvent.LuuBoThe(ten, moTa))
            },
            onHuy = { viewModel.xuLySuKien(QuanLyBoTheEvent.HuyHopThoai) }
        )
    }

    if (uiState.hienThiXacNhanXoa) {
        HopThoaiXacNhan(
            tieuDe = "Xóa bộ thẻ",
            noiDung = "Bạn có chắc chắn muốn xóa \"${uiState.boTheSeXoa?.tenBoThe}\"? Hành động này không thể hoàn tác.",
            chuXacNhan = "Xóa",
            onXacNhan = { viewModel.xuLySuKien(QuanLyBoTheEvent.XacNhanXoa) },
            onHuy = { viewModel.xuLySuKien(QuanLyBoTheEvent.HuyHopThoai) }
        )
    }

    Scaffold(
        topBar = {
            ThanhTieuDe(
                tieuDe = "Quản lý bộ thẻ chinh",
                coNutQuayLai = true,
                onQuayLai = { onEvent(QuanLyBoTheEvent.QuayLai) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.xuLySuKien(QuanLyBoTheEvent.MoHopThoaiThem) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm bộ thẻ")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.tuKhoaTimKiem,
                onValueChange = { viewModel.xuLySuKien(QuanLyBoTheEvent.DoiTuKhoa(it)) },
                placeholder = { Text("Tìm kiếm bộ thẻ...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when {
                uiState.dangTai -> DangTai(modifier = Modifier.fillMaxSize())

                uiState.danhSach.isEmpty() -> TrangThaiRong(
                    tieuDe = "Chưa có bộ thẻ nào",
                    moTa = if (uiState.tuKhoaTimKiem.isBlank()) {
                        "Nhấn nút + để tạo bộ thẻ đầu tiên"
                    } else {
                        "Không tìm thấy bộ thẻ nào khớp với \"${uiState.tuKhoaTimKiem}\""
                    },
                    modifier = Modifier.fillMaxSize()
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachNho),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.danhSach, key = { it.id }) { boThe ->
                        MucBoThe(
                            boThe = boThe,
                            onNhanVao = { onEvent(QuanLyBoTheEvent.MoChiTiet(boThe.id)) },
                            onSua = { viewModel.xuLySuKien(QuanLyBoTheEvent.MoHopThoaiSua(boThe)) },
                            onXoa = { viewModel.xuLySuKien(QuanLyBoTheEvent.YeuCauXoa(boThe)) }
                        )
                    }
                }
            }
        }
    }
}
