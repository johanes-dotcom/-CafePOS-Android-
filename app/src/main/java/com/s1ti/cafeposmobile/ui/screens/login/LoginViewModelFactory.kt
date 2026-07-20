package com.s1ti.cafeposmobile.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.SettingsManager
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.UserRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(context).userDao()
        val repository = UserRepository(dao)
        val settingsManager = SettingsManager(context)
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(repository, settingsManager) as T
    }
}
