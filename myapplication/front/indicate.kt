package com.example.myapplication.front

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.back.GameState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.R
import com.example.myapplication.back.Game_progress
import com.example.myapplication.market.MarketDialog
import com.example.myapplication.market.PurchaseStatus
import com.example.myapplication.market.MarketRules
import com.example.myapplication.market.MarketCatalog

val TERMS_DEFINITIONS: Map<String, String> = mapOf(
    "Обязательные траты" to "Это то, что нужно купить в любом случае. Без этого не обойтись. В нашей ситуации это продукты из списка.",
    "Необязательные траты" to "Это всё остальное: мороженое, лимонад, новые наушники, жетоны в парке аттракционов. То, без чего можно прожить, хотя иногда очень хочется.",
    "Накопления" to "Это деньги, которые ты не тратишь сейчас, чтобы использовать позже. Например, на крупную покупку или на непредвиденные расходы.",
    "Дебетовая карта" to "Платёжный инструмент, который даёт доступ к деньгам на твоём банковском счёте. На ней лежат твои собственные деньги, поэтому в минус уйти нельзя.",
    "Детская банковская карта" to "Карта, счёт которой принадлежит родителям. Они видят все операции и могут ставить ограничения.",
    "Банковский счет" to "Твоя личная запись в системе банка, где хранятся твои деньги. У счёта есть уникальный номер из 20 цифр.",
    "Накопительный счет" to "Счёт, на который можно класть и снимать деньги в любой момент. Процент начисляется на остаток каждый день.",
    "Вклад" to "Счёт, который открывается на конкретный срок. Ставка фиксируется в момент открытия и не меняется до конца срока.",
    "Кэшбэк" to "Когда банк возвращает небольшой процент от суммы покупки обратно на счёт покупателя.",
    "Процентная ставка" to "Это плата за пользование деньгами, выраженная в процентах. Например, банк платит тебе за то, что ты хранишь у него деньги.",
    "Кредит" to "Деньги, которые банк даёт тебе в долг под проценты. Ты обязан вернуть их в срок с доплатой.",
    "Банк" to "Финансовая организация, которая принимает вклады, выдаёт кредиты и проводит платежи. Зарабатывает на разнице процентов.",
    "НДФЛ" to "Налог на доходы физических лиц. Обычно 13% от зарплаты — его удерживает работодатель.",
    "НДС" to "Налог на добавленную стоимость. Включён в цену товара, поэтому покупатель платит его автоматически.",
    "Сложный процент" to "Когда проценты начисляются не только на начальную сумму, но и на уже накопленные проценты.",
    "Пассивный доход" to "Доход, который приходит без активного труда — например, проценты по вкладу или дивиденды.",
    "Инфляция" to "Обесценивание денег со временем. То, что вчера стоило 100 рублей, сегодня может стоить 110.",
    "Валютный курс" to "Цена одной валюты, выраженная в другой. Например, сколько рублей стоит один доллар."
)
@Composable
fun top_indicate(
    person_value_indicate : GameState,
    iconIdmoney : Int,
    iconIdweariness : Int,
    iconIdfood: Int,
){
    val percent_foodBar = (person_value_indicate.food.value.toFloat() / 100f)
    val percent_wearinessBar = (person_value_indicate.weariness.value.toFloat() / 100f)
    var color_foodBar = when{
        percent_foodBar > 0.7 -> Color(0xFF4CAF50)
        percent_foodBar > 0.3 ->  Color(0xFFFFC107)
        else -> Color(0xFFD32F2F)
    }
    var color_wearinessBar = when{
        percent_wearinessBar > 0.7 -> Color(0xFF4CAF50)
        percent_wearinessBar > 0.3 ->  Color(0xFFFFC107)
        else -> Color(0xFFDC1919)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xAAE3E3E3), RoundedCornerShape(12.dp))  // 👈 Зелёный фон
            .padding(horizontal = 12.dp, vertical = 6.dp),              // 👈 Внутренние отступы
        verticalAlignment = Alignment.CenterVertically,                 // 👈 Центрируем по вертикали
        horizontalArrangement = Arrangement.SpaceEvenly              // 👈 Отступ между иконкой и тек

    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.Start
        ){
            Image(

                painter = painterResource(iconIdmoney),
                contentDescription = "Иконка_денег",
            )
            Text(
                text = "${person_value_indicate.money.value}"
            )
        }
        Row(
            modifier = Modifier
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painter = painterResource(iconIdfood),
                contentDescription = "Иконка_сытости",
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(6.dp))       // 👈 Скругленные углы
                    .background(Color(0xFF918B8B))            // 👈 Фон полоски
            ) {
                // Заполненная часть
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percent_foodBar)         // 👈 Ширина = процент заполнения
                        .background(color_foodBar)               // 👈 Цвет заполнения
                )
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(iconIdweariness),
                contentDescription = "Иконка_усталости",
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF918B8B))
            ) {
                // Заполненная часть
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percent_wearinessBar)
                        .background(color_wearinessBar)
                )
            }
        }
    }
}
@Composable
fun button_botBar(
    gameState: GameState,
    gameProgress: Game_progress,
    currentLocation: String,
    onLocationChange: (String) -> Unit,
    onShowMiniMap:() -> Unit,
    onShowMarket: () -> Unit,

){
    var dialog_button_1 = remember { mutableStateOf(false) }
    var dialog_button_2 = remember { mutableStateOf(false) }
    var dialog_button_3 = remember { mutableStateOf(false) }
    var dialog_button_4 = remember { mutableStateOf(false) }
    val itemsList = List(30) { "Элемент #${it + 1}" }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,  // Равномерно по ширине
        verticalAlignment = Alignment.Top  // По центру по вертикали
    )
    {
        Button(
            onClick = { onShowMiniMap() },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = RectangleShape,  // 👈 Убираем скругление
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCF53E),  // Темно-желтый
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Миникарта",

                fontSize = 16.sp
            )
        }
        Button(
            onClick = { dialog_button_2.value = true },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = RectangleShape,  // 👈 Убираем скругление
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF18DA18),  // Темно-желтый
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Банк",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
        Button(
            onClick = { dialog_button_3.value = true },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = RectangleShape,  // 👈 Убираем скругление
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3FB5AB),  // Темно-желтый
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Список квестов",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
        Button(
            onClick = onShowMarket,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = RectangleShape,  // 👈 Убираем скругление
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF6E7ED),  // Темно-желтый
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Магазин",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
    if (dialog_button_1.value == true){

        miniMap(
            onClose = { dialog_button_1.value = false },
            onStorageClick = { onLocationChange("storage") },
            onHomeClick = { onLocationChange("lounge_room") },
            onShopClick = { onLocationChange("shop") },
            onParkClick = { onLocationChange("park") },
            onSchoolClick = { onLocationChange("school") },
            onBankClick = { onLocationChange("bank") }
        )
    }
    if (dialog_button_3.value == true){
        quest_button(
            gameProgress = gameProgress,
            onClose = {dialog_button_3.value = false}
        )
    }
    if (dialog_button_2.value == true){
        bank_window(
            onClose = { dialog_button_2.value = false },
            gameState = gameState
        )
    }
    if (dialog_button_4.value == true){

    }
}
@Composable
fun bank_window(
    onClose : () -> Unit,
    gameState : GameState,
){
    var window_saving_depozit = remember { mutableStateOf(false) }
    var window_vklad_depozit = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)      // 90% ширины
                .fillMaxHeight(0.75f),   // 75% высоты
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(0.25f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top
                ) {

                    BankCard(
                        title = "Карта",
                        balance = gameState.money.value,
                        color = Color(0xFF4CAF50),
                        clickable = false,
                        onClick = { }
                    )

                    // Накопительный счёт (кликабельный)
                    BankCard(
                        title = "Накопительный",
                        balance = gameState.savingsBalance.value,
                        color = Color(0xFF2196F3),
                        clickable = true,
                        onClick = { window_saving_depozit.value = true }
                    )

                    // Вклад (кликабельный)
                    BankCard(
                        title = "Вклад",
                        balance = gameState.depositBalance.value,
                        color = Color(0xFFFFC107),
                        clickable = true,
                        onClick = { window_vklad_depozit.value = true }
                    )
                }
            }
        }
    }
    if (window_saving_depozit.value == true ) {
        BankOperationDialog(
            title = "Накопительный счёт",
            onDeposit = { amount ->
                gameState.addToSavings(amount)
            },
            onWithdraw = { amount ->
                gameState.withdrawFromSavings(amount)
            },
            onClose = { window_saving_depozit.value = false }
        )
    }
    if (window_vklad_depozit.value == true){
        BankOperationDialog(
            title = "Вклад",
            onDeposit = { amount ->
                gameState.addToDeposit(amount)
            },
            onWithdraw = { amount ->
                // Вклад нельзя снять до завершения
                // Но окно всё равно показываем (можно показать сообщение)
                if (!gameState.depositActive.value) {
                    // Нельзя снять, вклад не активен
                } else {
                    // Нельзя снять, вклад активен
                }
            },
            onClose = { window_vklad_depozit.value = false }
        )
    }
}
@Composable
fun BankCard(
    title: String,
    balance: Int,
    color: Color,
    clickable: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color.copy(alpha = if (clickable) 0.3f else 0.15f),
                RoundedCornerShape(12.dp)
            )
            .then(
                if (clickable) Modifier.clickable { onClick() }
                else Modifier
            )
            .padding(12.dp)
    ) {
        Text(title, color = Color.White, fontSize = 12.sp)
        Text(
            "$balance ₽",
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        if (!clickable) {
            Text("🔒", fontSize = 10.sp)
        }
    }
}

@Composable
fun BankOperationDialog(
    title: String,
    withdrawEnabled: Boolean = true,
    onDeposit: (Int) -> Unit,   // 👈 что делать при пополнении
    onWithdraw: (Int) -> Unit,  // 👈 что делать при снятии
    onClose: () -> Unit
) {
    // Сумма для пополнения
    var depositText = remember { mutableStateOf("") }

    // Сумма для снятия
    var withdrawText = remember { mutableStateOf("") }

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                // ============================================================
                // ЗАГОЛОВОК
                // ============================================================
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ============================================================
                // СЕКЦИЯ 1: ПОПОЛНЕНИЕ
                // ============================================================
                Text(
                    text = "Введите сумму для пополнения",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = depositText.value,
                        onValueChange = {
                            depositText.value = it.filter { c -> c.isDigit() }
                        },
                        label = { Text("Сумма") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val amount = depositText.value.toIntOrNull() ?: 0
                            if (amount > 0) {
                                onDeposit(amount)
                                depositText.value = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("Пополнить")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ============================================================
                // СЕКЦИЯ 2: СНЯТИЕ
                // ============================================================
                Text(
                    text = "Введите сумму для снятия",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = withdrawText.value,
                        onValueChange = {
                            withdrawText.value = it.filter { c -> c.isDigit() }
                        },
                        label = { Text("Сумма") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val amount = withdrawText.value.toIntOrNull() ?: 0
                            if (amount > 0) {
                                onWithdraw(amount)
                                withdrawText.value = ""
                            }
                        },
                        enabled = withdrawEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Text("Снять")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ============================================================
                // КНОПКА ЗАКРЫТИЯ
                // ============================================================
                TextButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Закрыть", color = Color.Gray)
                }
            }
        }
    }
}
@Composable
fun miniMap(
    onClose: () -> Unit,             // 👈 НОВЫЙ: закрыть миникарту
    onStorageClick: () -> Unit,
    onHomeClick: () -> Unit,
    onShopClick: () -> Unit,
    onParkClick: () -> Unit,
    onSchoolClick: () -> Unit,
    onBankClick: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = maxWidth.value
        val h = maxHeight.value

        Box(modifier = Modifier.fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) { detectTapGestures(onTap = {}) }
        ) {
            // Фон
            Image(
                painter = painterResource(id = R.drawable.mini_map),
                contentDescription = "Миникарта",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            ClickableArea(
                onClick = { onClose(); onStorageClick() }, label = "Склад",
                x = (w * 0.015f).dp, y = (h * 0.049f).dp,
                width = (w * 0.34f).dp, height = (h * 0.162f).dp,
            )

            ClickableArea(
                onClick = { onClose(); onHomeClick() }, label = "Дом",
                x = (w * 0.275f).dp, y = (h * 0.251f).dp,
                width = (w * 0.255f).dp, height = (h * 0.047f).dp,
            )

            ClickableArea(
                onClick = { onClose(); onShopClick() }, label = "Продуктовый магазин",
                x = (w * 0.018f).dp, y = (h * 0.375f).dp,
                width = (w * 0.2f).dp, height = (h * 0.06f).dp,
            )

            ClickableArea(
                onClick = { onClose(); onParkClick() }, label = "Парк",
                x = (w * 0.39f).dp, y = (h * 0.343f).dp,
                width = (w * 0.33f).dp, height = (h * 0.055f).dp,
            )

            ClickableArea(
                onClick = { onClose(); onSchoolClick() }, label = "Школа",
                x = (w * 0.29f).dp, y = (h * 0.53f).dp,
                width = (w * 0.305f).dp, height = (h * 0.067f).dp,
            )

            ClickableArea(
                onClick = { onClose(); onBankClick() }, label = "Банк",
                x = (w * 0.795f).dp, y = (h * 0.388f).dp,
                width = (w * 0.115f).dp, height = (h * 0.06f).dp,
            )

            // 👈 Кнопка «Назад» (опционально)
            Button(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("← Назад")
            }
        }
    }
}

@Composable
fun ClickableArea(
    onClick: () -> Unit,
    x: Dp,
    y: Dp,
    width: Dp,
    height: Dp,
    label: String = "",
    debug: Boolean = false
) {
    val outline = RoundedCornerShape(6.dp)
    Box(
        modifier = Modifier.offset(x = x, y = y).size(width, height)
            .clip(outline)
            .background(Color(0xFFFFD45A).copy(alpha = .10f))
            .border(2.dp, Color(0xFFFFD45A), outline)
            .semantics { contentDescription = label }
            .clickable(onClickLabel = "Перейти: $label", onClick = onClick)
    )
}

@Composable
fun quest_button(
    gameProgress: Game_progress,
    onClose: () -> Unit
) {
    var selectedTab = remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { selectedTab.value = 0 },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab.value == 0) Color(0xFF4CAF50) else Color(0xFF424242),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Термины", fontSize = 14.sp)
                    }

                    Button(
                        onClick = { selectedTab.value = 1 },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab.value == 1) Color(0xFF4CAF50) else Color(0xFF424242),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Квесты", fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // ============================================================
                // СОДЕРЖИМОЕ (занимает всё оставшееся место)
                // ============================================================
                when (selectedTab.value) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f),  // 👈 ЗАНИМАЕТ ВСЁ МЕСТО
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(gameProgress.progress_terms.entries.toList()) { (term, isUnlocked) ->
                                TermCard(
                                    termName = term,
                                    isUnlocked = isUnlocked
                                )
                            }
                        }
                    }

                    1 -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f),  // 👈 ЗАНИМАЕТ ВСЁ МЕСТО
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(gameProgress.progress_quests.entries.toList()) { (quest, isComplited) ->
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = quest,
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    },
                                    trailingContent = {
                                        Text(
                                            text = if (isComplited) "✅" else "⬜",
                                            fontSize = 20.sp
                                        )
                                    },
                                    modifier = Modifier
                                        .background(
                                            if (isComplited) Color(0xFF2E4A2E) else Color(0xFF2A2A2A),
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ============================================================
                // КНОПКА ЗАКРЫТИЯ
                // ============================================================
                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF424242),
                        contentColor = Color.White
                    )
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}
@Composable
fun TermCard(
    termName: String,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        if (isUnlocked) { Color(0xFFF5F5F5)}
        else { Color(0xFF4A4A4A)}
    val titleColor =
        if (isUnlocked) {Color(0xFF1C1B1F)}
        else Color(0xFFBDBDBD)
    val descriptionColor =
        if (isUnlocked) Color(0xFF5A5A5A)
        else Color(0xFF9E9E9E)

    val descriptionText = if (isUnlocked) {
        TERMS_DEFINITIONS[termName] ?: "Описание отсутствует" }
        else { "..." }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)  // 👈 ФИКСИРОВАННАЯ ВЫСОТА
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = termName,
                color = titleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis  // Если тектс не влезает получаем ...
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = descriptionText,
                color = descriptionColor,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 4,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}
