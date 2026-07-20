package com.s1ti.cafeposmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    // REQ-2.1: daftar menu, di-observe reaktif lewat Flow
    @Query("SELECT * FROM menu ORDER BY nama ASC")
    fun getAllMenus(): Flow<List<MenuEntity>>

    // REQ-2.9: detail menu
    @Query("SELECT * FROM menu WHERE id = :id LIMIT 1")
    suspend fun getMenuById(id: Long): MenuEntity?

    // REQ-2.2
    @Insert
    suspend fun insertMenu(menu: MenuEntity): Long

    // REQ-2.3
    @Update
    suspend fun updateMenu(menu: MenuEntity)

    // REQ-2.4
    @Delete
    suspend fun deleteMenu(menu: MenuEntity)

    @Query("SELECT COUNT(*) FROM menu")
    suspend fun countMenu(): Int
}
