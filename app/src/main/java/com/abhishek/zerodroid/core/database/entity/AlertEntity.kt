package com.abhishek.zerodroid.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey
    val id: String,
    val source: String,
    val severity: String,
    val title: String,
    val detail: String,
    val timestamp: Long = System.currentTimeMillis()
)
