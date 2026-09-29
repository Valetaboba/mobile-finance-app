package com.example.myapplication.front


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.Dialogs.DialogResult
import com.example.myapplication.Dialogs.father_dialogs
import com.example.myapplication.Dialogs.gera_dialogs
import com.example.myapplication.Dialogs.processing_dialogResult
import com.example.myapplication.R
import com.example.myapplication.back.GameDataStore
import com.example.myapplication.back.Game_progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 1 - ребенок , 6 - гера
val dialogLines_bank_kart_j = listOf(
    DialogLine(1,"Привет!"),
    DialogLine(2,"Ну здарова ЕБАТЬ"),
    DialogLine(2,"Ну как там с деньгами?"),
    DialogLine(1,"Да вот ,депнул в Евротранс"),
    DialogLine(2,"Красава ебать , а в ОИЛ Ресурс зашел ?")
)


@Composable
fun background_bank(imageId: Int){
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = imageId),
        contentDescription = "back_lounge_room",
        contentScale = ContentScale.FillHeight,
    )
}
@Composable
fun bank(
    gameResurs: Game_progress,
    gameDataStore: GameDataStore,
    scope: CoroutineScope,
    dialogResurs: gera_dialogs

){
    var showDialogList = remember { mutableStateOf(false) }
    var showDialog = remember { mutableStateOf(false) }
    var currentDialogResult = remember { mutableStateOf<DialogResult?>(null) }
    var status_LazyColumn = remember {  mutableStateOf<List<DialogLine>>(emptyList()) }
    val dialogList = gameResurs.progress_dialogsGera_bank.keys.toList()
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        background_loungeRoom(R.drawable.bank)
        Box(
            modifier = Modifier.fillMaxSize()

                .clickable{showDialogList.value = true}
        ){
        }
    }
    if (showDialogList.value == true){
        AlertDialog(
            onDismissRequest = { showDialogList.value = false }, // Закрыть
            confirmButton = {
                TextButton(onClick = { showDialogList.value = false }) {
                    Text("ОК")
                }
            },
            text = {
                LazyColumn {
                    items(dialogList) { dialogName ->
                        var itUnlocked = gameResurs.progress_dialogsGera_bank[dialogName] ?: false
                        ListItem(
                            headlineContent = { Text(dialogName) },
                            modifier = Modifier.clickable(enabled = itUnlocked) {
                                var result = when(dialogName){
                                    "Что такое банк, и на чем он зарабатывает?" -> dialogResurs.dialog_3_tax_bank()
                                    "Что такое процентная ставка?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_1_interest_rate()
                                    }
                                    "Что такое кредит?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_2_credit()
                                    }
                                    "Что такое сложный процент?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_6_tax_hard_interest()

                                    }
                                    "Что такое инфляция?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_8_inflation()

                                    }
                                    "Что такое пассивный доход?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_7_passiv_profit()

                                    }
                                    "Что такое НДС?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_5_tax_NDS()

                                    }
                                    "Что такое НДФЛ", "Что такое НДФЛ?" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_4_tax_NDFL()

                                    }
                                    else -> null
                                }
                                if (result != null) {
                                    currentDialogResult.value = result
                                    status_LazyColumn.value = listOf(
                                        DialogLine(1, dialogName),
                                        DialogLine(6, TERMS_DEFINITIONS[result.unlocketTerms.keys.firstOrNull()]
                                            ?: "Давай обсудим этот вопрос."),
                                    )
                                    showDialogList.value = false      // закрываем список
                                    showDialog.value = true       // открываем сцену
                                }

                            }
                        )
                    }
                }
            }
        )
    }
    if (showDialog.value == true){

        DialogScene_bank(
            lines = status_LazyColumn.value,
            onComplete = {
                // 👈 Диалог завершён — сохраняем
                if (status_LazyColumn.value.isNotEmpty()) {
                    currentDialogResult.value?.let { processing_dialogResult(it, gameResurs) }
                }
                scope.launch {
                    gameDataStore.saveProgress(gameResurs)
                }
                showDialog.value = false
            }
        )
    }

}

@Composable
fun DialogScene_bank(
    lines: List<DialogLine>,
    onComplete: () -> Unit
) {
    // Индекс текущей реплики
    var currentIndex = remember(lines) { mutableIntStateOf(0) }

    // Текущая реплика
    val currentLine = lines.getOrNull(currentIndex.value)
    if (currentLine == null) {
        AlertDialog(onDismissRequest = onComplete,
            text = { Text("Этот диалог пока не подготовлен.") },
            confirmButton = { TextButton(onClick = onComplete) { Text("Закрыть") } })
        return
    }

    Dialog(
        onDismissRequest = { /* нельзя закрыть */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false  // 👈 на весь экран
        )
    ) {
        // 👈 Контейнер на весь экран
        Box(
            modifier = Modifier
                .fillMaxSize().testTag("dialogue-scene")
                .background(Color.Black.copy(alpha = 0.7f))  // затемнение
                .clickable {
                    // 👈 Клик — следующая реплика
                    if (currentIndex.value < lines.size - 1) {
                        currentIndex.value ++
                    } else {
                        onComplete()  // конец диалога
                    }
                }
        ) {
            // 👈 Персонаж (силуэт) и текст
            when (currentLine.number_npc) {
                1 -> {
                    // Отец — силуэт слева
                    Image(
                        painter = painterResource(id = R.drawable.kesha),
                        contentDescription = "Отец",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    )

                    // Текст справа
                    Text(
                        text = currentLine.text_npc, // текст
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(32.dp)
                            .background(
                                Color(0xAA000000),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    )
                }

                6 -> {
                    // Отец — силуэт справа
                    Image(
                        painter = painterResource(id = R.drawable.gera),
                        contentDescription = "Сын",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )

                    // Текст слева
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(
                                Color(0xAA000000),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    )
                }

            }

            // 👈 Подсказка внизу
            Text(
                text = "Нажми, чтобы продолжить",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            )
        }
    }
}