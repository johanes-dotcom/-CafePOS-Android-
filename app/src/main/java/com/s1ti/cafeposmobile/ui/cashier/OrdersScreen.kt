package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.components.CashierBottomNavigation
import com.s1ti.cafeposmobile.ui.components.CashierNavigationRail
import com.s1ti.cafeposmobile.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit, 
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = viewModel(factory = OrdersViewModelFactory(LocalContext.current))
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Aktif", "Diproses", "Siap", "Selesai", "Dibatalkan")

    val activeOrders by viewModel.activeOrders.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()

    var selectedOrderForDetail by remember { mutableStateOf<TransactionEntity?>(null) }
    var orderItems by remember { mutableStateOf<List<TransactionItemEntity>>(emptyList()) }
    var showDeleteConfirm by remember { mutableStateOf<TransactionEntity?>(null) }

    LaunchedEffect(selectedOrderForDetail) {
        selectedOrderForDetail?.let {
            orderItems = viewModel.getItemsForTransaction(it.id)
        }
    }

    if (selectedOrderForDetail != null) {
        OrderDetailDialog(
            order = selectedOrderForDetail!!,
            items = orderItems,
            onDismiss = { selectedOrderForDetail = null }
        )
    }

    if (showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Hapus Pesanan") },
            text = { Text("Apakah Anda yakin ingin menghapus pesanan ini? Data yang dihapus tidak dapat dikembalikan.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteOrder(showDeleteConfirm!!.id)
                        showDeleteConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CafeError)
                ) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Batal")
                }
            }
        )
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "orders", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.orders)) },
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!isExpanded && !isMedium) {
                    CashierBottomNavigation(currentRoute = "orders", onNavigate = onNavigate)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { 
                                Text(
                                    title, 
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                ) 
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val filteredOrders = if (selectedTab == 0 || selectedTab == 1 || selectedTab == 2) {
                    activeOrders.filter { it.status == tabs[selectedTab] }
                } else {
                    allTransactions.filter { it.status == tabs[selectedTab] }
                }

                if (filteredOrders.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada pesanan", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    }
                } else {
                    if (isExpanded || isMedium) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (isExpanded) 3 else 2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onStatusChange = { viewModel.updateStatus(order, it) },
                                    onDetail = { selectedOrderForDetail = it },
                                    onDelete = { showDeleteConfirm = it }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onStatusChange = { viewModel.updateStatus(order, it) },
                                    onDetail = { selectedOrderForDetail = it },
                                    onDelete = { showDeleteConfirm = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: TransactionEntity, 
    onStatusChange: (String) -> Unit, 
    onDetail: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    val statusColor = when(order.status) {
        "Aktif" -> CafeWarning
        "Diproses" -> Color(0xFF60A5FA)
        "Siap" -> CafeSuccess
        "Selesai" -> Color.Gray
        "Dibatalkan" -> CafeError
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    CafeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if(order.tableNumber.isNotEmpty()) "Meja ${order.tableNumber}" else "Take Away", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = order.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor
                        )
                    }
                }
                Text(text = order.id, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(8.dp))
                
                if (order.customerName.isNotEmpty()) {
                    Text(text = "Pelanggan: ${order.customerName}", style = MaterialTheme.typography.bodySmall)
                }
                
                Text(text = "Total: Rp${order.totalAmount.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            
            IconButton(onClick = { onDelete(order) }) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = CafeError.copy(alpha = 0.6f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val buttonModifier = Modifier.weight(1f)
            when (order.status) {
                "Aktif" -> {
                    Button(
                        onClick = { onStatusChange("Diproses") },
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Proses", fontSize = 12.sp)
                    }
                }
                "Diproses" -> {
                    Button(
                        onClick = { onStatusChange("Siap") },
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = CafeSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Siap", fontSize = 12.sp)
                    }
                }
                "Siap" -> {
                    Button(
                        onClick = { onStatusChange("Selesai") },
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Selesaikan", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            OutlinedButton(
                onClick = { onDetail(order) },
                modifier = if (order.status == "Selesai" || order.status == "Dibatalkan") Modifier.fillMaxWidth() else buttonModifier,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(stringResource(R.string.order_detail), color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun OrderDetailDialog(order: TransactionEntity, items: List<TransactionItemEntity>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.order_detail),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                
                DetailRow(stringResource(R.string.transaction_no), order.id)
                DetailRow("Waktu", dateFormat.format(Date(order.timestamp)))
                DetailRow("Pelanggan", if(order.customerName.isEmpty()) "-" else order.customerName)
                DetailRow("Meja", if(order.tableNumber.isEmpty()) "Take Away" else order.tableNumber)
                DetailRow(stringResource(R.string.status), order.status)
                DetailRow("Metode", order.paymentMethod)
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.background)
                
                Text("Daftar Menu:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                
                items.forEach { item ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.quantity}x ${item.productName}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Text("Rp${(item.price * item.quantity).toInt()}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                
                if (order.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Catatan:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(order.note, style = MaterialTheme.typography.bodyMedium, color = CafeError)
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.background)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Rp${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.done))
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
