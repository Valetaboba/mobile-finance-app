package com.example.myapplication.back

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey

class GameState(money: Int = 1000, food: Int = 20, weariness: Int = 100, context: Context? = null) {
    private val save = context?.let { PlayerSave(it) }
    var money = mutableIntStateOf(save?.int("money", money) ?: money)
    var food = mutableIntStateOf(save?.int("food", food) ?: food)
    var weariness = mutableIntStateOf(save?.int("weariness", weariness) ?: weariness)

    var savingsBalance = mutableIntStateOf(save?.int("savings", 0) ?: 0)      // Накопительный счёт
    var depositBalance = mutableIntStateOf(save?.int("deposit", 0) ?: 0)      // Вклад
    var depositDaysLeft = mutableIntStateOf(save?.int("deposit_days", 0) ?: 0)     // Сколько дней осталось до конца вклада
    var depositActive = mutableStateOf(save?.bool("deposit_active") ?: false)       // Активен ли вклад
    var ownedProductIds by mutableStateOf(save?.ids("owned") ?: emptySet())
        private set
    var equippedProductIds by mutableStateOf(save?.ids("equipped") ?: emptySet())
        private set
    internal var lastSettledSession = save?.lastSession()
    internal var inventoryMigrated = save?.bool("inventory_migrated") ?: false
    private fun persist() { save?.write(this) }

    /** One-time import of the existing DataStore inventory, including its older ids. */
    fun importInventory(progress: Game_progress) {
        if (inventoryMigrated) return
        ownedProductIds = ownedProductIds + progress.progress_items_owned.filterValues { it }.keys
            .map(com.example.myapplication.wardrobe.ClothingCatalog::canonicalId)
        equippedProductIds = progress.progress_equipped.filterValues { it }.keys
            .map(com.example.myapplication.wardrobe.ClothingCatalog::canonicalId)
            .filter { it in ownedProductIds }
            .mapNotNull { com.example.myapplication.wardrobe.ClothingCatalog.item(it) }
            .distinctBy { it.slot }.map { it.productId }.toSet()
        inventoryMigrated = true
        persist()
    }

    fun purchaseProduct(id: String): com.example.myapplication.market.PurchaseStatus {
        val product = com.example.myapplication.market.MarketCatalog.product(id)
            ?: return com.example.myapplication.market.PurchaseStatus.INVALID_PRODUCT
        val result = com.example.myapplication.market.MarketRules.checkPurchase(money.intValue, ownedProductIds, product)
        if (result == com.example.myapplication.market.PurchaseStatus.PURCHASED) {
            money.intValue -= product.price
            ownedProductIds = ownedProductIds + id
            persist()
        }
        return result
    }

    fun equipProduct(id: String) {
        val item = com.example.myapplication.wardrobe.ClothingCatalog.item(id) ?: return
        if (id !in ownedProductIds) return
        equippedProductIds = equippedProductIds.filterNot {
            com.example.myapplication.wardrobe.ClothingCatalog.item(it)?.slot == item.slot
        }.toSet() + id
        persist()
    }

    fun unequip(slot: com.example.myapplication.wardrobe.ClothingSlot) {
        equippedProductIds = equippedProductIds.filterNot {
            com.example.myapplication.wardrobe.ClothingCatalog.item(it)?.slot == slot
        }.toSet()
        persist()
    }

    fun spendOnGroceries(amount: Int): Boolean {
        if (amount < 0 || amount > money.intValue) return false
        money.intValue -= amount
        persist()
        return true
    }

    /** A repeated finish callback must never credit the same run twice. */
    fun settleMiniGame(session: String, result: ru.finny.games.api.MiniGameResult): Boolean {
        if (lastSettledSession == session) return false
        val reward = when (result.gameId) {
            "warehouse" -> result.reward.coerceIn(0, 100)
            "bikes" -> result.reward.coerceIn(0, 50)
            else -> 0
        }
        // Grocery purchases were charged on collection; do not charge the receipt twice.
        money.intValue += reward
        lastSettledSession = session
        persist()
        return true
    }

    fun addToSavings(amount: Int) {
        if (amount > 0 && money.value >= amount) {
            money.value -= amount
            savingsBalance.value += amount
            persist()
        }
    }

    fun withdrawFromSavings(amount: Int) {
        if (amount > 0 && savingsBalance.value >= amount) {
            savingsBalance.value -= amount
            money.value += amount
            persist()
        }
    }

    fun addToDeposit(amount: Int) {
        if (amount > 0 && money.value >= amount && !depositActive.value) {
            money.value -= amount
            depositBalance.value = amount
            depositDaysLeft.value = 2
            depositActive.value = true
            persist()
        }
    }

    // 👈 Вызывается при нажатии на кровать
    fun onSleep() {
        // Накопительный счёт: +1% каждый сон
        if (savingsBalance.value > 0) {
            val interest = (savingsBalance.value * 0.01f).toInt()
            savingsBalance.value += interest
        }

        // Вклад: уменьшаем счётчик дней
        if (depositActive.value) {
            depositDaysLeft.value--
            if (depositDaysLeft.value <= 0) {
                // Вклад завершён: +2.2%, деньги возвращаются на карту
                val finalAmount = (depositBalance.value * 1.022f).toInt()
                money.value += finalAmount
                depositBalance.value = 0
                depositActive.value = false
            }
        }

        // Существующая логика сна
        food.value = (food.value - 10).coerceIn(0, 100)
        weariness.value = (weariness.value + 30).coerceIn(0, 100)
        persist()
    }
    fun operation_value_money(delta: Int) {
        money.value = (money.value + delta).coerceAtLeast(0)
        persist()
    }
    fun operation_value_food(delta: Int) {
        food.value = (food.value + delta).coerceIn(0, 100)
        persist()
    }
    fun operation_value_weariness(delta: Int) {
        weariness.value = (weariness.value + delta).coerceIn(0, 100)
        persist()
    }
}
data class Game_progress(
    val prolog_completed: Boolean = false,
    var progress_terms :MutableMap<String, Boolean> = mutableMapOf(
        "Обязательные траты" to true ,
        "Необязательные траты" to true,
        "Накопления" to true,
        "Дебетовая карта" to true,
        "Детская банковская карта" to true,
        "Банковский счет" to true,
        "Накопительный счет" to true,
        "Вклад" to true,
        "Кэшбэк" to true,

        "Процентная ставка" to false,
        "Кредит" to false,
        "Банк" to false,
        "НДФЛ" to false,
        "НДС" to false,
        "Сложный процент" to false,
        "Пассивный доход" to false,
        "Инфляция" to false,
        "Валютный курс" to false,

    ),
    var progress_quests :MutableMap<String, Boolean> = mutableMapOf(
        "Сходить на работу,получить первый рассчетный лист и узнать на практике что такое НДФЛ" to false,
        "Воспользоваться накопительным счетом" to false,
        "Воспользоваться вкладом" to false,
        "Сходить в магазин и понять на практике что такое НДС" to false
    ),
    var progress_dialogsFather_home :MutableMap<String, Boolean> = mutableMapOf(
        "Я узнал новые экономические термины..." to true
    ),
    var progress_dialogsMother_home :MutableMap<String, Boolean> = mutableMapOf(
        "Я купил нужные продукты" to true,
    ),
    var progress_dialogsGera_bank: MutableMap<String, Boolean> = mutableMapOf(
        "Что такое процентная ставка?" to true,
        "Что такое кредит?" to false,
        "Что такое сложный процент?" to false,
        "Что такое банк, и на чем он зарабатывает?" to true,
        "Что такое пассивный доход?" to false,
        "Что такое инфляция?" to false,
        "Что такое НДС?" to true,
        "Что такое НДФЛ" to false,
        //"Что такое валютный курс?" to false
    ),
    var progress_dialogsElya_scholl: MutableMap<String , Boolean> = mutableMapOf(),
    var progress_dialogs_storage:MutableMap<String , Boolean> = mutableMapOf(
        "Готов к работе?" to true
    ),
    var progress_dialogsKesha_park: MutableMap<String, Boolean> = mutableMapOf(
        "Давай уcтроим гонку" to true
    ),
    var progress_production_shop: MutableMap<String, Boolean> = mutableMapOf(
        "Начать закупку продуктов" to false
    ),
    var progress_items_owned: MutableMap<String, Boolean> = mutableMapOf(
        // Верх
        "top_tshirt" to false,
        "top_jacket_wind" to false,
        "top_jacket_denim" to false,
        "top_jacket_winter" to false,
        // Штаны
        "pants_shorts_sand" to false,
        "pants_shorts_sport" to false,
        "pants_jeans" to false,
        "pants_cargo" to false,
        // Обувь
        "shoes_canvas" to false,
        "shoes_running" to false,
        "shoes_boots" to false
    ),

    // ============================================================
    // 👈 НАДЕТО ЛИ (true = надето, false = не надето)
    // ============================================================
    var progress_equipped: MutableMap<String, Boolean> = mutableMapOf(
        "top_tshirt" to false,
        "top_jacket_wind" to false,
        "top_jacket_denim" to false,
        "top_jacket_winter" to false,
        "pants_shorts_sand" to false,
        "pants_shorts_sport" to false,
        "pants_jeans" to false,
        "pants_cargo" to false,
        "shoes_canvas" to false,
        "shoes_running" to false,
        "shoes_boots" to false
    )

)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_progress")
/*Создаем*/
class GameDataStore(private val context: Context) {
    // Определяем ключи. DataStore Preferences не хранит Map,
    // поэтому храним Set<String> с ключами, у которых значение true.
    private object Keys {
        val PROLOG_COMPLETED = booleanPreferencesKey("prolog_completed")
        val TERMS_UNLOCKED = stringSetPreferencesKey("terms_unlocked")
        val FATHER_DIALOGS_UNLOCKED = stringSetPreferencesKey("father_dialogs_unlocked")
        val MOTHER_DIALOGS_UNLOCKED = stringSetPreferencesKey("mother_dialogs_unlocked")
        val GERA_DIALOGS_UNLOCKED = stringSetPreferencesKey("gera_dialogs_unlocked")
        val ELYA_DIALOGS_UNLOCKED = stringSetPreferencesKey("elya_dialogs_unlocked")
        val STORAGE_DIALOGS_UNLOCKED = stringSetPreferencesKey("storage_dialogs_unlocked")
        val KESHA_DIALOGS_UNLOCKED = stringSetPreferencesKey("kesha_dialogs_unlocked")

        val QUESTS_UNLOCKED = stringSetPreferencesKey("quests_unlocked")
        val ITEMS_OWNED = stringSetPreferencesKey("items_owned")        // 👈
        val ITEMS_EQUIPPED = stringSetPreferencesKey("items_equipped")  // 👈
    }
    val defaultProgress = Game_progress()
    // ЧТЕНИЕ: Превращаем Flow<Preferences> в Flow<Game_progress>
    val gameProgressFlow: Flow<Game_progress> = context.dataStore.data.map { preferences ->
        Game_progress(
            prolog_completed = preferences[Keys.PROLOG_COMPLETED] ?: false,

            progress_terms = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.TERMS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_terms)
                } else {
                    defaultProgress.progress_terms.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogsFather_home = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.FATHER_DIALOGS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogsFather_home)
                } else {
                    defaultProgress.progress_dialogsFather_home.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogsMother_home = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.MOTHER_DIALOGS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogsMother_home)
                } else {
                    defaultProgress.progress_dialogsMother_home.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogsGera_bank = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.GERA_DIALOGS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogsGera_bank)
                } else {
                    defaultProgress.progress_dialogsGera_bank.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogsElya_scholl = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.ELYA_DIALOGS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogsElya_scholl)
                } else {
                    defaultProgress.progress_dialogsElya_scholl.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogs_storage = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.STORAGE_DIALOGS_UNLOCKED]
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogs_storage)
                } else {
                    defaultProgress.progress_dialogs_storage.keys.forEach { put(it, it in unlocked) }
                }
            },

            progress_dialogsKesha_park = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.KESHA_DIALOGS_UNLOCKED]  // 👈 KESHA, не STORAGE!
                if (unlocked == null) {
                    putAll(defaultProgress.progress_dialogsKesha_park)
                } else {
                    defaultProgress.progress_dialogsKesha_park.keys.forEach { put(it, it in unlocked) }
                }
            },
            progress_quests = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.QUESTS_UNLOCKED]  // 👈 KESHA, не STORAGE!
                if (unlocked == null) {
                    putAll(defaultProgress.progress_quests)
                } else {
                    defaultProgress.progress_quests.keys.forEach { put(it, it in unlocked) }
                }
            },
            progress_items_owned = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.ITEMS_OWNED]
                if (unlocked == null) putAll(defaultProgress.progress_items_owned)
                else (defaultProgress.progress_items_owned.keys + unlocked).forEach { put(it, it in unlocked) }
            },

            progress_equipped = mutableMapOf<String, Boolean>().apply {
                val unlocked = preferences[Keys.ITEMS_EQUIPPED]
                if (unlocked == null) putAll(defaultProgress.progress_equipped)
                else (defaultProgress.progress_equipped.keys + unlocked).forEach { put(it, it in unlocked) }
            }
        )
    }

    suspend fun completeProlog() {
        context.dataStore.edit { it[Keys.PROLOG_COMPLETED] = true }
    }

    // ЗАПИСЬ: Сохраняем Game_progress в DataStore
    suspend fun saveProgress(progress: Game_progress) {
        context.dataStore.edit { preferences ->
            // Сохраняем только разблокированные (true) в виде Set<String>
            preferences[Keys.PROLOG_COMPLETED] = progress.prolog_completed
            preferences[Keys.TERMS_UNLOCKED] = progress.progress_terms.filterValues { it }.keys
            preferences[Keys.FATHER_DIALOGS_UNLOCKED] = progress.progress_dialogsFather_home.filterValues { it }.keys
            preferences[Keys.MOTHER_DIALOGS_UNLOCKED] = progress.progress_dialogsMother_home.filterValues { it }.keys
            preferences[Keys.GERA_DIALOGS_UNLOCKED] = progress.progress_dialogsGera_bank.filterValues { it }.keys
            preferences[Keys.ELYA_DIALOGS_UNLOCKED] = progress.progress_dialogsElya_scholl.filterValues { it }.keys
            preferences[Keys.STORAGE_DIALOGS_UNLOCKED] = progress.progress_dialogs_storage.filterValues { it }.keys
            preferences[Keys.KESHA_DIALOGS_UNLOCKED] = progress.progress_dialogsKesha_park.filterValues { it }.keys
            preferences[Keys.QUESTS_UNLOCKED] = progress.progress_quests.filterValues { it }.keys
            preferences[Keys.ITEMS_OWNED] = progress.progress_items_owned.filterValues { it }.keys
            preferences[Keys.ITEMS_EQUIPPED] = progress.progress_equipped.filterValues { it }.keys
        }
    }
}
@Composable
fun GameScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1. Создаём GameDataStore
    val gameDataStore = remember { GameDataStore(context) }

    // 2. Загружаем состояние
    var gameProgress = remember { mutableStateOf(Game_progress()) }

    // 3. Подписываемся на Flow
    LaunchedEffect(Unit) {
        gameDataStore.gameProgressFlow.collect { savedProgress ->
            gameProgress.value = savedProgress
        }
    }

}

