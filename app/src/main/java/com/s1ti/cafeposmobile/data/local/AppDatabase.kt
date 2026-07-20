package com.s1ti.cafeposmobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.s1ti.cafeposmobile.data.local.dao.BahanBakuDao
import com.s1ti.cafeposmobile.data.local.dao.MenuDao
import com.s1ti.cafeposmobile.data.local.dao.UserDao
import com.s1ti.cafeposmobile.data.local.dao.TransactionDao
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import com.s1ti.cafeposmobile.data.local.entity.UserEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.data.model.MenuKategori
import com.s1ti.cafeposmobile.data.model.UserRole
import com.s1ti.cafeposmobile.util.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromRole(role: UserRole): String = role.name

    @TypeConverter
    fun toRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromMenuKategori(kategori: MenuKategori): String = kategori.name

    @TypeConverter
    fun toMenuKategori(value: String): MenuKategori = MenuKategori.valueOf(value)
}

@Database(
    entities = [
        UserEntity::class, 
        MenuEntity::class, 
        BahanBakuEntity::class, 
        TransactionEntity::class, 
        TransactionItemEntity::class
    ],
    version = 2001, // Incremented to 2001 due to TransactionEntity changes.
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun menuDao(): MenuDao
    abstract fun bahanBakuDao(): BahanBakuDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cafepos_mobile.db"
                )
                    .addCallback(SeedCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedCallback(
        private val context: Context
    ) : Callback() {
        
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                val dao = database.userDao()
                
                dao.insertUser(UserEntity(
                    username = "admin",
                    passwordHash = PasswordHasher.hash("admin123"),
                    fullName = "Michael (Owner)",
                    role = UserRole.ADMIN_OWNER,
                    isActive = true
                ))
                dao.insertUser(UserEntity(
                    username = "kasir 1",
                    passwordHash = PasswordHasher.hash("kasir 123"),
                    fullName = "Kasir Satu",
                    role = UserRole.KASIR,
                    isActive = true
                ))
            }
        }
    }
}
