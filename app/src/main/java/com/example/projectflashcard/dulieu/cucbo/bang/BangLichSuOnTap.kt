package com.example.projectflashcard.dulieu.cucbo.bang

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lich_su_on_tap",
    foreignKeys = [
        ForeignKey(
            entity = BangTuVung::class,
            parentColumns = ["id"],
            childColumns = ["tuVungId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BangBoThe::class,
            parentColumns = ["id"],
            childColumns = ["boTheId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tuVungId"]),
        Index(value = ["boTheId"])
    ]
)
data class BangLichSuOnTap(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tuVungId: Long,
    val boTheId: Long,
    val mucDoOnTap: String,
    val ngayOn: Long
)
