package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.SettingsManager
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.components.CashierBottomNavigation
import com.s1ti.cafeposmobile.ui.components.CashierNavigationRail
import com.s1ti.cafeposmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    settingsManager: SettingsManager,
    onThemeChanged: (String) -> Unit,
    onLanguageChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLangDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(stringResource(R.string.theme)) },
            text = {
                Column {
                    ThemeOption(
                        label = stringResource(R.string.light_mode),
                        selected = settingsManager.theme == SettingsManager.THEME_LIGHT,
                        onClick = {
                            settingsManager.theme = SettingsManager.THEME_LIGHT
                            onThemeChanged(SettingsManager.THEME_LIGHT)
                            showThemeDialog = false
                        }
                    )
                    ThemeOption(
                        label = stringResource(R.string.dark_mode),
                        selected = settingsManager.theme == SettingsManager.THEME_DARK,
                        onClick = {
                            settingsManager.theme = SettingsManager.THEME_DARK
                            onThemeChanged(SettingsManager.THEME_DARK)
                            showThemeDialog = false
                        }
                    )
                }
            },
            confirmButton = {}
        )
    }

    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = { Text(stringResource(R.string.language)) },
            text = {
                Column {
                    ThemeOption(
                        label = "Indonesia",
                        selected = settingsManager.language == SettingsManager.LANG_ID,
                        onClick = {
                            settingsManager.language = SettingsManager.LANG_ID
                            onLanguageChanged(SettingsManager.LANG_ID)
                            showLangDialog = false
                        }
                    )
                    ThemeOption(
                        label = "English",
                        selected = settingsManager.language == SettingsManager.LANG_EN,
                        onClick = {
                            settingsManager.language = SettingsManager.LANG_EN
                            onLanguageChanged(SettingsManager.LANG_EN)
                            showLangDialog = false
                        }
                    )
                }
            },
            confirmButton = {}
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar Akun") },
            text = { Text("Apakah Anda yakin ingin keluar dari akun?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onNavigate("logout")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CafeError)
                ) {
                    Text("Ya, Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "profile", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = { onNavigate("dashboard") }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                )
            },
            bottomBar = {
                if (!isExpanded && !isMedium) {
                    CashierBottomNavigation(currentRoute = "profile", onNavigate = onNavigate)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Aplikasi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                SettingsItem(
                    icon = Icons.Default.Palette, 
                    label = stringResource(R.string.theme), 
                    value = if (settingsManager.theme == SettingsManager.THEME_DARK) stringResource(R.string.dark_mode) else stringResource(R.string.light_mode),
                    onClick = { showThemeDialog = true }
                )
                SettingsItem(icon = Icons.Default.Notifications, label = "Notifikasi", isSwitch = true)
                SettingsItem(
                    icon = Icons.Default.Language, 
                    label = stringResource(R.string.language), 
                    value = if (settingsManager.language == SettingsManager.LANG_ID) "Indonesia" else "English",
                    onClick = { showLangDialog = true }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Lainnya",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                SettingsItem(icon = Icons.Default.Info, label = stringResource(R.string.about_app), onClick = { onNavigate("about") })
                SettingsItem(icon = Icons.AutoMirrored.Filled.Help, label = stringResource(R.string.help), onClick = { onNavigate("help") })
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp, 
                    label = stringResource(R.string.logout), 
                    labelColor = CafeError,
                    onClick = { showLogoutDialog = true }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "CafePOS Mobile v1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    value: String = "",
    isSwitch: Boolean = false,
    labelColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit = {}
) {
    var checked by remember { mutableStateOf(true) }
    
    CafeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Surface(
            onClick = onClick,
            color = androidx.compose.ui.graphics.Color.Transparent,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = if (labelColor == CafeError) CafeError else MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = label, modifier = Modifier.weight(1f), color = labelColor)
                
                if (isSwitch) {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                    )
                } else if (value.isNotEmpty()) {
                    Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                } else {
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            }
        }
    }
}
