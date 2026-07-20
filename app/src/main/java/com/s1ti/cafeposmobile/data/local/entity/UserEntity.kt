package com.s1ti.cafeposmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s1ti.cafeposmobile.data.model.UserRole

/**
 * Tabel pengguna (Admin/Owner & Kasir).
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val fullName: String,
    val role: UserRole,
    val isActive: Boolean = true,
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val photoUri: String? = null
)
