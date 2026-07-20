package com.s1ti.cafeposmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_items")
data class TransactionItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionId: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val costPrice: Double = 0.0
)
