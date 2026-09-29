package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import com.example.myapplication.Dialogs.*
import com.example.myapplication.back.*
import com.example.myapplication.front.*
import com.example.myapplication.front.chill_room
import com.example.myapplication.market.MarketDialog
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.wardrobe.*
import ru.finny.games.api.MiniGameMemory
import ru.finny.games.api.MiniGameResult
import ru.finny.games.bikes.BikeGameScreen
import ru.finny.games.grocery.GroceryGameScreen
import ru.finny.games.grocery.sampleGroceryConfig
import ru.finny.games.warehouse.WarehouseGameScreen
import java.util.UUID
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val memory = ViewModelProvider(this)[MiniGameMemory::class.java]
        setContent { MyApplicationTheme { GameApp(memory) } }
    }
}

@Composable
fun GameApp(memory: MiniGameMemory) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gameDataStore = remember { GameDataStore(context) }
    var gameProgress by remember { mutableStateOf(Game_progress()) }
    val gameState = remember { GameState(context = context) }
    var savingProlog by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }
    var currentLocation by rememberSaveable { mutableStateOf("children_room") }
    var showMiniMap by rememberSaveable { mutableStateOf(false) }
    var showMarket by rememberSaveable { mutableStateOf(false) }
    var showWardrobe by rememberSaveable { mutableStateOf(false) }
    var activeGame by rememberSaveable { mutableStateOf<String?>(null) }
    var session by rememberSaveable { mutableStateOf("") }
    var groceryBudget by rememberSaveable { mutableIntStateOf(0) }
    var resultText by rememberSaveable { mutableStateOf<String?>(null) }
    var notice by rememberSaveable { mutableStateOf<String?>(null) }

    // A killed process cannot restore a live simulation. Already charged purchases remain saved.
    LaunchedEffect(Unit) {
        if (activeGame != null && memory.session != session) {
            activeGame = null
            notice = "Прерванная мини-игра завершена. Баланс и покупки сохранены."
        }
    }

    LaunchedEffect(gameDataStore) {
        gameDataStore.gameProgressFlow.collect {
            gameState.importInventory(it)
            gameProgress = it
            loaded = true
        }
    }
    val onClick_wardrobe: () -> Unit = { showWardrobe = true }
    val startGame: (String) -> Unit = { game ->
        if (game == "grocery" && gameState.money.intValue <= 0) {
            notice = "Для покупок нужны монеты. Их можно заработать на складе или в велогонке."
        } else {
            session = UUID.randomUUID().toString()
            memory.beginRun(session)
            groceryBudget = gameState.money.intValue
            activeGame = game
        }
    }
    val exitGame: () -> Unit = { activeGame = null; memory.clearRun() }
    val finishGame: (MiniGameResult) -> Unit = { result ->
        if (activeGame == result.gameId) {
            gameState.settleMiniGame(session, result)
            resultText = if (result.gameId == "grocery") {
                "Потрачено: ${result.spent} монет.\nБаланс: ${gameState.money.intValue} монет.\n\n${result.feedback}"
            } else {
                "Награда: ${result.reward} монет.\nБаланс: ${gameState.money.intValue} монет.\n\n${result.feedback}"
            }
            exitGame()
        }
    }
    BackHandler(enabled = activeGame != null || showMiniMap) {
        if (activeGame != null) exitGame() else showMiniMap = false
    }
    Surface(Modifier.fillMaxSize()) {
        if (!loaded) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (!gameProgress.prolog_completed) {
            prolog_gane(gameProgress) {
                if (!savingProlog) {
                    savingProlog = true
                    scope.launch {
                        try {
                            gameDataStore.completeProlog()
                            currentLocation = "children_room"
                        } catch (_: IOException) {
                            notice = "Не удалось сохранить завершение пролога. Нажми на последнюю реплику ещё раз."
                        } finally {
                            savingProlog = false
                        }
                    }
                }
            }
        } else if (activeGame != null && memory.session == session) {
            key(session) {
                when (activeGame) {
                    "bikes" -> BikeGameScreen(onFinished = finishGame, onExit = exitGame, memory = memory)
                    "warehouse" -> WarehouseGameScreen(onFinished = finishGame, onExit = exitGame)
                    "grocery" -> GroceryGameScreen(
                        config = sampleGroceryConfig().copy(budget = groceryBudget),
                        onFinished = finishGame, onExit = exitGame,
                        onSpend = gameState::spendOnGroceries, memory = memory,
                    )
                }
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize().safeDrawingPadding()) {
                    top_indicate(gameState, R.drawable.icon_money, R.drawable.icon_wearless, R.drawable.icon_food)
                    Box(Modifier.weight(1f).testTag("location")) {
                        when (currentLocation) {
                            "children_room" -> chill_room(gameState.equippedProductIds, onClick_wardrobe)
                            "lounge_room" -> loungeRoom(gameProgress, gameDataStore, scope, father_dialogs())
                            "kitchen" -> kitchen(gameProgress, gameDataStore, scope, mother_dialogs())
                            "park" -> park(gameProgress, gameDataStore, scope, kesha_dialogs(), onStartRace = { startGame("bikes") })
                            "bank" -> bank(gameProgress, gameDataStore, scope, gera_dialogs())
                            "storage" -> storage(gameProgress, gameDataStore, scope, vova_dialogs(), onStartWork = { startGame("warehouse") })
                            "shop" -> shop(gameProgress, gameDataStore, scope, shop_dialogs(), onStartShopping = { startGame("grocery") })
                            else -> loungeRoom(gameProgress, gameDataStore, scope, father_dialogs())
                        }
                        val nextRoom = when (currentLocation) {
                            "children_room" -> "lounge_room" to "В гостиную"
                            "lounge_room" -> "kitchen" to "На кухню"
                            "kitchen" -> "children_room" to "В детскую"
                            else -> null
                        }
                        if (nextRoom != null) {
                            FilledTonalIconButton(
                                onClick = { currentLocation = nextRoom.first },
                                modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp).size(48.dp)
                                    .testTag("home-next-room").semantics { contentDescription = nextRoom.second },
                            ) { Text("›", fontSize = 36.sp) }
                        } else {
                            Column(Modifier.align(Alignment.BottomEnd).padding(end = 12.dp, bottom = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                PlayerCharacter(gameState.equippedProductIds, Modifier.height(190.dp), onClick_wardrobe)
                                FilledTonalButton(onClick = onClick_wardrobe) { Text("Шкаф") }
                            }
                        }
                    }
                    Box(Modifier.fillMaxWidth().height(60.dp)) {
                        button_botBar(gameState, gameProgress, currentLocation,
                            onLocationChange = { currentLocation = it },
                            onShowMiniMap = { showMiniMap = true }, onShowMarket = { showMarket = true })
                    }
                }
                if (showMiniMap) {
                    fun moveTo(location: String) { currentLocation = location; showMiniMap = false }
                    miniMap(onClose = { showMiniMap = false },
                        onStorageClick = { moveTo("storage") }, onHomeClick = { moveTo("children_room") },
                        onShopClick = { moveTo("shop") }, onParkClick = { moveTo("park") },
                        onSchoolClick = { moveTo("school") }, onBankClick = { moveTo("bank") })
                }
            }
        }
    }
    if (loaded && gameProgress.prolog_completed && showMarket) {
        MarketDialog(gameState.money.intValue, gameState.ownedProductIds,
            onPurchase = gameState::purchaseProduct, onClose = { showMarket = false })
    }
    if (loaded && gameProgress.prolog_completed && showWardrobe) {
        WardrobeDialog(gameState.ownedProductIds, gameState.equippedProductIds,
            onEquip = gameState::equipProduct, onUnequip = gameState::unequip, onClose = { showWardrobe = false })
    }
    resultText?.let { text ->
        AlertDialog(onDismissRequest = { resultText = null }, title = { Text("Результат") },
            text = { Text(text, Modifier.verticalScroll(rememberScrollState())) },
            confirmButton = { TextButton(onClick = { resultText = null }) { Text("Вернуться в локацию") } })
    }
    notice?.let { text ->
        AlertDialog(onDismissRequest = { notice = null }, text = { Text(text) },
            confirmButton = { TextButton(onClick = { notice = null }) { Text("ОК") } })
    }
}
