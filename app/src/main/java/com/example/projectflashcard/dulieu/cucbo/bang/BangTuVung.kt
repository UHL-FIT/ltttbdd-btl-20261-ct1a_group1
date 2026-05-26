package com.example.projectflashcard.dulieu.cucbo.bang

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tu_vung",
    foreignKeys = [
        ForeignKey(
            entity = BangBoThe::class,
            parentColumns = ["id"],
            childColumns = ["boTheId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["boTheId"])]
)
data class BangTuVung(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val boTheId: Long,
    val tu: String,
    val nghia: String,
    val phienAm: String,
    val viDu: String,
    val trangThai: String,
    val canOnHomNay: Boolean,
    val ngayTao: Long
)
