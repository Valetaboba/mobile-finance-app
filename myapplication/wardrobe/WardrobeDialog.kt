package com.example.myapplication.wardrobe

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.market.MarketCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeDialog(
    ownedIds: Set<String>, equippedIds: Set<String>, onEquip: (String) -> Unit,
    onUnequip: (ClothingSlot) -> Unit, onClose: () -> Unit,
) {
    var slot by rememberSaveable { mutableStateOf(ClothingSlot.TOP) }
    val clothes = ClothingCatalog.items.filter { it.slot == slot && it.productId in ownedIds }
    BasicAlertDialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxWidth().widthIn(max = 600.dp).fillMaxHeight(.94f).safeDrawingPadding().padding(12.dp),
            shape = MaterialTheme.shapes.large) {
            Column(Modifier.fillMaxSize().padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Шкаф", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    TextButton(onClick = onClose) { Text("Закрыть") }
                }
                PlayerCharacter(equippedIds, Modifier.heightIn(max = 220.dp).weight(.4f).align(Alignment.CenterHorizontally))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ClothingSlot.entries.forEach { item ->
                        FilterChip(selected = slot == item, onClick = { slot = item }, label = { Text(item.title) })
                    }
                }
                TextButton(onClick = { onUnequip(slot) }, enabled = equippedIds.any { ClothingCatalog.item(it)?.slot == slot }) {
                    Text("Снять: ${slot.title.lowercase()}")
                }
                if (clothes.isEmpty()) {
                    Text("Здесь пока нет купленных вещей. Загляни в магазин!", Modifier.padding(12.dp))
                }
                LazyColumn(Modifier.weight(.6f)) {
                    items(clothes, key = { it.productId }) { item ->
                        val equipped = item.productId in equippedIds
                        ListItem(
                            headlineContent = { Text(MarketCatalog.product(item.productId)?.title ?: item.productId) },
                            leadingContent = { Image(painterResource(item.imageRes), null, Modifier.size(64.dp)) },
                            trailingContent = {
                                TextButton(onClick = { onEquip(item.productId) }, enabled = !equipped) {
                                    Text(if (equipped) "Надето" else "Надеть")
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
