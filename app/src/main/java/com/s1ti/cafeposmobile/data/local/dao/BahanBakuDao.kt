package com.s1ti.cafeposmobile.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BahanBakuDao {

    // REQ-3.1
    @Query("SELECT * FROM bahan_baku ORDER BY nama ASC")
    fun getAllBahanBaku(): Flow<List<BahanBakuEntity>>

    @Query("SELECT * FROM bahan_baku WHERE id = :id LIMIT 1")
    suspend fun getBahanBakuById(id: Long): BahanBakuEntity?

    // REQ-3.2
    @Insert
    suspend fun insertBahanBaku(bahan: BahanBakuEntity): Long

    // REQ-3.3, REQ-3.9
    @Update
    suspend fun updateBahanBaku(bahan: BahanBakuEntity)

    // REQ-3.4
    @Delete
    suspend fun deleteBahanBaku(bahan: BahanBakuEntity)

    @Query("SELECT COUNT(*) FROM bahan_baku")
    suspend fun countBahanBaku(): Int
}
