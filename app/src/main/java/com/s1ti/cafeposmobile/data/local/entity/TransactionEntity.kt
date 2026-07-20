package com.s1ti.cafeposmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val totalAmount: Double,
    val totalCostPrice: Double,
    val paymentMethod: String,
    val status: String, // Aktif, Diproses, Siap, Selesai, Dibatalkan
    val customerName: String = "",
    val tableNumber: String = "",
    val note: String = "",
    val cashierName: String = "",
    val amountPaid: Double = 0.0,
    val tax: Double = 0.0
)
