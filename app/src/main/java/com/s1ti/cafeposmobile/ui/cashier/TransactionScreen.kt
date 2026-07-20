package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.CartItem
import com.s1ti.cafeposmobile.data.Product
import com.s1ti.cafeposmobile.ui.components.CafeButton
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.components.CashierNavigationRail
import com.s1ti.cafeposmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    windowSizeClass: WindowSizeClass,
    cartItems: SnapshotStateList<CartItem>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = viewModel(factory = TransactionViewModelFactory(LocalContext.current))
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    
    var searchQuery by remember { mutableStateOf("") }
    
    val allLabel = stringResource(R.string.all_categories)
    val categories = listOf(
        allLabel, 
        "KOPI", 
        "MINUMAN", 
        "MAKANAN", 
        "SNACK"
    )
    var selectedCategory by remember { mutableStateOf(allLabel) }
    
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
    val totalItems = cartItems.sumOf { it.quantity }

    val products by viewModel.availableProducts.collectAsState()

    fun addToCart(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            if (existing.quantity > 1) {
                cartItems[index] = existing.copy(quantity = existing.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "transaction", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.new_transaction)) },
                    navigationIcon = {
                        IconButton(onClick = { onNavigate("dashboard") }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!isExpanded && cartItems.isNotEmpty()) {
                    CartSummaryBar(itemCount = totalItems, total = totalAmount, onCheckout = { onNavigate("payment") })
                }
            }
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(if (isExpanded) 0.65f else 1f)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.search_menu), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                border = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (products.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                                Icon(
                                    Icons.Default.RestaurantMenu, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(100.dp), 
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Belum Ada Produk Tersedia", 
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Silakan tambahkan produk terlebih dahulu melalui Menu Produk.", 
                                    style = MaterialTheme.typography.bodyMedium, 
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                CafeButton(
                                    text = stringResource(R.string.add_product), 
                                    onClick = { onNavigate("product_menu") }, 
                                    modifier = Modifier.width(200.dp)
                                )
                            }
                        }
                    } else {
                        val filteredProducts = products.filter { 
                            (selectedCategory == allLabel || it.category == selectedCategory) &&
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                        
                        if (filteredProducts.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Produk tidak ditemukan", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(if (isExpanded) 3 else 2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(filteredProducts, key = { it.id }) { product ->
                                    val cartItem = cartItems.find { it.product.id == product.id }
                                    ProductCard(
                                        product = product, 
                                        quantity = cartItem?.quantity ?: 0,
                                        onAdd = { if (product.isAvailable) addToCart(product) },
                                        onRemove = { removeFromCart(product) }
                                    )
                                }
                            }
                        }
                    }
                }

                if (isExpanded) {
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                    Column(
                        modifier = Modifier
                            .weight(0.35f)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(stringResource(R.string.order_detail), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (cartItems.isEmpty()) {
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text("Keranjang Kosong", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(cartItems) { item ->
                                    CartRow(item, 
                                        onIncrease = { addToCart(item.product) },
                                        onDecrease = { removeFromCart(item.product) }
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.total), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Text("Rp${totalAmount.toInt()}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        CafeButton(
                            text = stringResource(R.string.process_payment),
                            onClick = { onNavigate("payment") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = cartItems.isNotEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text("Rp${item.product.price.toInt()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Text("${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product, 
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    CafeCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (product.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUri),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), modifier = Modifier.size(40.dp))
            }
            
            if (!product.isAvailable) {
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.out_of_stock), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
        Text(text = "Rp${product.price.toInt()}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (quantity == 0) {
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth().height(36.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = product.isAvailable,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.add), fontSize = 12.sp)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.RemoveCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Text(text = quantity.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier.size(32.dp),
                    enabled = product.isAvailable
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun CartSummaryBar(itemCount: Int, total: Double, onCheckout: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "$itemCount Item", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), style = MaterialTheme.typography.labelSmall)
                Text(text = "Total: Rp${total.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            CafeButton(text = stringResource(R.string.pay), onClick = onCheckout, modifier = Modifier.width(140.dp))
        }
    }
}
