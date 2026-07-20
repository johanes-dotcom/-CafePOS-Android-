package com.s1ti.cafeposmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s1ti.cafeposmobile.data.model.MenuKategori


@Entity(tableName = "menu")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val kategori: MenuKategori,
    val hargaJual: Long,
    val hargaModal: Long? = null,
    val deskripsi: String = "",
    val fotoUri: String? = null,
    val tersedia: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
