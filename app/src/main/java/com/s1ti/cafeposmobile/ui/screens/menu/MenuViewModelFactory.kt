package com.s1ti.cafeposmobile.ui.screens.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.MenuRepository

class MenuViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(context).menuDao()
        val repository = MenuRepository(dao)
        @Suppress("UNCHECKED_CAST")
        return MenuViewModel(repository) as T
    }
}
