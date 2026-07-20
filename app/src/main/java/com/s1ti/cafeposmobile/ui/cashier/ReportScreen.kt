package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.TopProduct
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.ui.components.*
import com.s1ti.cafeposmobile.ui.theme.*
import com.s1ti.cafeposmobile.util.ReportUtils
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportViewModel = viewModel(factory = OrdersViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    var selectedFilter by remember { mutableStateOf("Hari Ini") }
    val filters = listOf("Hari Ini", "Kemarin", "Minggu Ini", "Bulan Ini", "Tahun Ini")

    var selectedTransaction by remember { mutableStateOf<TransactionEntity?>(null) }
    var transactionItems by remember { mutableStateOf<List<TransactionItemEntity>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val allTransactions by viewModel.allTransactions.collectAsState()

    val periodTransactions = remember(selectedFilter, allTransactions) {
        viewModel.filterTransactionsByPeriod(allTransactions, selectedFilter)
    }

    val periodRangeText = remember(selectedFilter) {
        ReportUtils.getPeriodDateRangeText(selectedFilter)
    }

    // Calculations based on periodTransactions
    val totalRevenue = periodTransactions.sumOf { it.totalAmount }
    val totalTransactions = periodTransactions.size
    val totalProfit = periodTransactions.sumOf { it.totalAmount - it.totalCostPrice }
    val averageOrder = if (totalTransactions > 0) totalRevenue / totalTransactions else 0.0

    // For simplicity in this example, we'll assume we don't have discount column in TransactionEntity yet
    val totalDiscount = 0.0 

    val filteredTransactions = remember(searchQuery, periodTransactions) {
        periodTransactions.filter {
            it.id.contains(searchQuery, ignoreCase = true) ||
                    it.cashierName.contains(searchQuery, ignoreCase = true)
        }.reversed()
    }

    LaunchedEffect(selectedTransaction) {
        selectedTransaction?.let {
            transactionItems = viewModel.getItemsForTransaction(it.id)
        }
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "report", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.report), fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { onNavigate("dashboard") }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Filter Periode
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }

                    // Informasi Rentang Tanggal
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(selectedFilter, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                Text(periodRangeText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Summary Stats
                    Text("Ringkasan Penjualan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SummaryCard(Modifier.weight(1f), "Total Penjualan", formatCurrency(totalRevenue), Icons.Default.Payments, CafePrimary)
                            SummaryCard(Modifier.weight(1f), "Total Transaksi", totalTransactions.toString(), Icons.Default.Receipt, Color(0xFF3B82F6))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SummaryCard(Modifier.weight(1f), "Total Profit", formatCurrency(totalProfit), Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF8B5CF6))
                            SummaryCard(Modifier.weight(1f), "Average Order", formatCurrency(averageOrder), Icons.Default.Calculate, Color(0xFF10B981))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Transaction History
                    SectionHeader("Riwayat Transaksi", Icons.Default.History)

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari Invoice...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (filteredTransactions.isEmpty()) {
                        Text("Belum ada data untuk periode ini.", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    } else {
                        filteredTransactions.forEach { tx ->
                            TransactionItemRow(tx, onClick = { selectedTransaction = tx })
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    selectedTransaction?.let { tx ->
        OrderDetailDialog(
            order = tx,
            items = transactionItems,
            onDismiss = { selectedTransaction = null }
        )
    }
}

@Composable
fun TransactionItemRow(tx: TransactionEntity, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    CafeCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(tx.id, fontWeight = FontWeight.Bold)
                Text(dateFormat.format(Date(tx.timestamp)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Text("Rp${tx.totalAmount.toInt()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(12.dp))
}

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("Rp", "Rp ")
}
