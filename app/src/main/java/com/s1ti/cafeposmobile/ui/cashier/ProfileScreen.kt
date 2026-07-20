package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.local.entity.UserEntity
import com.s1ti.cafeposmobile.data.repository.UserRepository
import com.s1ti.cafeposmobile.ui.components.CafeButton
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.components.CashierBottomNavigation
import com.s1ti.cafeposmobile.ui.components.CashierNavigationRail
import com.s1ti.cafeposmobile.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit, 
    modifier: Modifier = Modifier,
    userName: String = "admin"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepository = remember { UserRepository(AppDatabase.getDatabase(context).userDao()) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    var user by remember { mutableStateOf<UserEntity?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    LaunchedEffect(userName) {
        user = userRepository.getUserByUsername(userName)
    }

    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    
    var isEditing by remember { mutableStateOf(false) }

    // State for validation errors
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        user?.let {
            fullName = it.fullName
            email = it.email
            phone = it.phoneNumber
            address = it.address
        }
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (fullName.isBlank()) {
            fullNameError = "Nama tidak boleh kosong"
            isValid = false
        } else {
            fullNameError = null
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isBlank()) {
            emailError = "Email tidak boleh kosong"
            isValid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            emailError = "Format email tidak valid"
            isValid = false
        } else {
            emailError = null
        }

        if (phone.isBlank()) {
            phoneError = "Nomor telepon tidak boleh kosong"
            isValid = false
        } else {
            phoneError = null
        }

        if (address.isBlank()) {
            addressError = "Alamat tidak boleh kosong"
            isValid = false
        } else {
            addressError = null
        }

        return isValid
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "profile", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.profile)) },
                    actions = {
                        if (!isEditing) {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!isExpanded && !isMedium) {
                    CashierBottomNavigation(currentRoute = "profile", onNavigate = onNavigate)
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = if (isExpanded) 800.dp else 600.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(if (isExpanded) 120.dp else 100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(if (isExpanded) 80.dp else 60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (!isEditing) {
                        Text(
                            text = fullName,
                            style = if (isExpanded) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${user?.role?.name ?: ""} • Aktif",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ProfileField(
                            label = "Nama Lengkap", 
                            value = fullName, 
                            isEditing = isEditing, 
                            onValueChange = { fullName = it; fullNameError = null }, 
                            icon = Icons.Default.Badge,
                            errorMessage = fullNameError
                        )
                        ProfileField(
                            label = "Email", 
                            value = email, 
                            isEditing = isEditing, 
                            onValueChange = { email = it; emailError = null }, 
                            icon = Icons.Default.Email,
                            errorMessage = emailError
                        )
                        ProfileField(
                            label = "Nomor HP", 
                            value = phone, 
                            isEditing = isEditing, 
                            onValueChange = { phone = it; phoneError = null }, 
                            icon = Icons.Default.Phone,
                            errorMessage = phoneError
                        )
                        ProfileField(
                            label = "Alamat", 
                            value = address, 
                            isEditing = isEditing, 
                            onValueChange = { address = it; addressError = null }, 
                            icon = Icons.Default.LocationOn,
                            errorMessage = addressError
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    if (isEditing) {
                        if (isLoading) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        CafeButton(
                            text = "Simpan Perubahan",
                            onClick = { 
                                if (validateInputs()) {
                                    user?.let {
                                        val updatedUser = it.copy(
                                            fullName = fullName.trim(),
                                            email = email.trim(),
                                            phoneNumber = phone.trim(),
                                            address = address.trim()
                                        )
                                        scope.launch {
                                            isLoading = true
                                            userRepository.updateUser(updatedUser)
                                            user = updatedUser
                                            isLoading = false
                                            isEditing = false
                                            snackbarHostState.showSnackbar("Profil berhasil diperbarui")
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { 
                                isEditing = false
                                // Reset fields and errors
                                fullName = user?.fullName ?: ""
                                email = user?.email ?: ""
                                phone = user?.phoneNumber ?: ""
                                address = user?.address ?: ""
                                fullNameError = null
                                emailError = null
                                phoneError = null
                                addressError = null
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            Text("Batal", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                        }
                    } else {
                        Button(
                            onClick = { onNavigate("logout") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CafeError.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = CafeError)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.logout), color = CafeError, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    errorMessage: String? = null
) {
    CafeCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary, 
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label, 
                    style = MaterialTheme.typography.labelSmall, 
                    color = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (isEditing) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage != null,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            errorContainerColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                } else {
                    Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                }
                
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
