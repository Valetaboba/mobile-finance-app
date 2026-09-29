package com.example.myapplication.market

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

private val Paper = Color(0xFFFFFCF5)
private val Ink = Color(0xFF283340)
private val Accent = Color(0xFFFFC247)
private val Divider = Color(0xFFDFD8CA)

/** Host owns money and inventory. This window owns only navigation and confirmation state. */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MarketDialog(
    balance: Int,
    ownedProductIds: Set<String>,
    onPurchase: (String) -> PurchaseStatus,
    onClose: () -> Unit,
    departments: List<MarketDepartment> = MarketCatalog.departments,
) {
    var departmentId by rememberSaveable { mutableStateOf<String?>(null) }
    var categoryId by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingId by rememberSaveable { mutableStateOf<String?>(null) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    val department = departments.firstOrNull { it.id == departmentId }
    val pending = departments.flatMap { it.categories }.flatMap { it.groups }
        .flatMap { it.products }.firstOrNull { it.id == pendingId }

    MaterialTheme(colorScheme = lightColorScheme(
            primary = Ink, onPrimary = Color.White, surface = Paper, onSurface = Ink,
            background = Paper, onBackground = Ink,
        )) {
        BasicAlertDialog(
            onDismissRequest = onClose,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            BoxWithConstraints(Modifier.fillMaxSize().safeDrawingPadding().padding(8.dp), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth().height(maxHeight * 0.96f),
                    shape = RoundedCornerShape(16.dp), color = Paper, contentColor = Ink,
                    border = BorderStroke(1.dp, Divider),
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("МАГАЗИН", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("Баланс: $balance монет", fontSize = 14.sp)
                            }
                            TextButton(onClick = onClose) { Text("Закрыть") }
                        }
                        if (department != null) {
                            Text(department.title, Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Medium)
                            key(department.id) {
                                Row(
                                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    CategoryButton("Все", categoryId == null) { categoryId = null }
                                    department.categories.forEach { category ->
                                        CategoryButton(category.title, categoryId == category.id) { categoryId = category.id }
                                    }
                                }
                            }
                        }
                        message?.let { text ->
                            Row(Modifier.fillMaxWidth().background(Color(0xFFFFEABF)).padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text, Modifier.weight(1f), fontSize = 13.sp)
                                TextButton(onClick = { message = null }) { Text("ОК") }
                            }
                        }
                        Box(Modifier.weight(1f).fillMaxWidth()) {
                            if (department == null) {
                                Column(Modifier.align(Alignment.Center).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Выберите магазин", fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Spacer(Modifier.height(12.dp))
                                    Text("Одежда, товары для дома, спорт и техника — кнопки внизу окна.", textAlign = TextAlign.Center)
                                }
                            } else {
                                // A new filter starts at the top instead of keeping an unrelated scroll offset.
                                key(department.id, categoryId) {
                                    ProductList(department, categoryId, balance, ownedProductIds) {
                                        pendingId = it.id
                                        message = null
                                    }
                                }
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth().background(Color(0xFFF1ECDF)).padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            departments.forEach { item ->
                                Button(
                                    onClick = { departmentId = item.id; categoryId = null; pendingId = null; message = null },
                                    modifier = Modifier.weight(1f).heightIn(min = 60.dp)
                                        .semantics { selected = departmentId == item.id },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (departmentId == item.id) Accent else Color.White,
                                        contentColor = Ink,
                                    ),
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(item.symbol, fontSize = 19.sp)
                                        Text(item.title, fontSize = 11.sp, textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // A sibling dialog keeps Back/dismiss handling separate from the catalogue.
        pending?.let { product ->
            val owned = product.id in ownedProductIds
            val affordable = balance >= product.price
            AlertDialog(
                onDismissRequest = { pendingId = null },
                title = { Text(product.title) },
                text = {
                    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        MarketProductImage(product, Modifier.fillMaxWidth().height(120.dp))
                        Text(product.description)
                        Text("Цена: ${product.price} монет", fontWeight = FontWeight.Bold)
                        Text("Баланс: $balance монет")
                        when {
                            owned -> Text("Этот товар уже куплен.")
                            !affordable -> Text("Не хватает ${product.price - balance} монет.")
                            else -> Text("После покупки останется ${balance - product.price} монет.")
                        }
                    }
                },
                confirmButton = {
                    TextButton(modifier = Modifier.testTag("market-confirm-purchase"), enabled = !owned && affordable, onClick = {
                        message = when (onPurchase(product.id)) {
                            PurchaseStatus.PURCHASED -> "Куплено: ${product.title}"
                            PurchaseStatus.ALREADY_OWNED -> "Этот товар уже куплен."
                            PurchaseStatus.NOT_ENOUGH_MONEY -> "Недостаточно монет."
                            PurchaseStatus.INVALID_PRODUCT -> "Товар недоступен."
                        }
                        pendingId = null
                    }) { Text("Купить") }
                },
                dismissButton = { TextButton(onClick = { pendingId = null }) { Text("Отмена") } },
            )
        }
    }
}

@Composable
private fun CategoryButton(title: String, selected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.semantics { this.selected = selected },
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, if (selected) Accent else Divider),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected) Accent else Color.White, contentColor = Ink),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) { Text(title, fontSize = 13.sp) }
}

@Composable
private fun ProductList(
    department: MarketDepartment,
    categoryId: String?,
    balance: Int,
    owned: Set<String>,
    onSelect: (MarketProduct) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val fontScale = LocalDensity.current.fontScale
        val columns = if (maxWidth < 280.dp || fontScale > 1.3f) 1 else 2
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            department.categories.filter { categoryId == null || it.id == categoryId }.forEach { category ->
                category.groups.forEach { group ->
                    item(key = "${category.id}/${group.id}/heading") {
                        Text(group.title, Modifier.fillMaxWidth().padding(vertical = 6.dp), textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                    if (group.products.isEmpty()) {
                        item(key = "${category.id}/${group.id}/empty") { Text("Пока нет товаров") }
                    }
                    group.products.chunked(columns).forEach { row ->
                        item(key = "${category.id}/${group.id}/${row.first().id}") {
                            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                row.forEach { product ->
                                    MarketProductCard(product, product.id in owned, balance, { onSelect(product) }, Modifier.weight(1f).fillMaxHeight())
                                }
                                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Change the design of every card here; imageRes replaces the placeholder automatically. */
@Composable
fun MarketProductCard(
    product: MarketProduct,
    owned: Boolean,
    balance: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (owned) Color(0xFF83AD8F) else Divider),
    ) {
        MarketProductImage(product, Modifier.fillMaxWidth().aspectRatio(1.35f))
        Column(Modifier.fillMaxWidth().weight(1f).padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(product.title, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.weight(1f))
            Text("${product.price} монет", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(
                when { owned -> "✓ Куплено"; balance < product.price -> "Не хватает ${product.price - balance}"; else -> "Купить" },
                modifier = Modifier.fillMaxWidth().background(
                    when { owned -> Color(0xFFE0EEDC); balance < product.price -> Color(0xFFF0EBE4); else -> Accent },
                    RoundedCornerShape(6.dp),
                ).padding(horizontal = 4.dp, vertical = 8.dp),
                textAlign = TextAlign.Center, fontSize = 12.sp, color = Ink,
            )
        }
    }
}

/** Shared placeholder for both the card and purchase confirmation. */
@Composable
private fun MarketProductImage(product: MarketProduct, modifier: Modifier = Modifier) {
    Box(modifier.background(Color(product.placeholderColor)), contentAlignment = Alignment.Center) {
        if (product.imageRes != null) {
            Image(painterResource(product.imageRes), product.title, Modifier.fillMaxSize().padding(8.dp), contentScale = ContentScale.Fit)
        } else {
            Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("▧", fontSize = 32.sp, color = Ink.copy(alpha = 0.45f))
                Text(product.title, fontSize = 12.sp, color = Ink.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MarketClothesPreview() {
    MaterialTheme {
        ProductList(MarketCatalog.departments.first(), "trousers", 1000, emptySet(), {})
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MarketWallsPreview() {
    MaterialTheme {
        ProductList(MarketCatalog.departments.first { it.id == "home" }, "walls", 1000, emptySet(), {})
    }
}
