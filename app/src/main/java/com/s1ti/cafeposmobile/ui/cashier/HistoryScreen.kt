package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.ui.components.*
import com.s1ti.cafeposmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    onDetail: (TransactionHistory) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(LocalContext.current))
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    
    var searchQuery by remember { mutableStateOf("") }
    
    val transactions by viewModel.allTransactions.collectAsState()
    val filteredHistory = remember(searchQuery, transactions) {
        transactions.filter { it.id.contains(searchQuery, ignoreCase = true) }
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "history", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.history)) },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.FilterList, contentDescription = null)
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!isExpanded && !isMedium) {
                    CashierBottomNavigation(currentRoute = "history", onNavigate = onNavigate)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = if (isExpanded) 32.dp else 16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(if (isExpanded) 0.5f else 1f),
                    placeholder = { Text(stringResource(R.string.search_menu), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredHistory.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada riwayat transaksi.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                } else {
                    if (isExpanded || isMedium) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (isExpanded) 3 else 2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredHistory, key = { it.id }) { item ->
                                HistoryItemCard(item, onClick = { onDetail(item) })
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredHistory, key = { it.id }) { item ->
                                HistoryItemCard(item, onClick = { onDetail(item) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(history: TransactionHistory, onClick: () -> Unit) {
    CafeCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = history.id, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Text(text = "${history.date} • ${history.time} • ${history.method}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Rp${history.total.toInt()}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Surface(
                    color = CafeSuccess.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = history.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = CafeSuccess
                    )
                }
            }
        }
    }
}
