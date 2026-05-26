package com.example.projectflashcard.dulieu.cucbo.bang

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bo_the")
data class BangBoThe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tenBoThe: String,
    val moTa: String,
    val ngayTao: Long
)