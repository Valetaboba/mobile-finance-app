package com.example.myapplication.front

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
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
import com.example.myapplication.Dialogs.mother_dialogs
import com.example.myapplication.Dialogs.processing_dialogResult
import com.example.myapplication.R
import com.example.myapplication.back.GameDataStore
import com.example.myapplication.back.Game_progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 1 - ребенок , 3 - мама
val dialogLines_mom = listOf(
    DialogLine(1,"Привет!"),
    DialogLine(2,"Ну здарова ЕБАТЬ"),
    DialogLine(2,"Ну как там с деньгами?"),
    DialogLine(1,"Да вот ,депнул в Евротранс"),
    DialogLine(2,"Красава ебать , а в ОИЛ Ресурс зашел ?")

)
@Composable
fun background_kitchenRoom(imageId: Int){
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = imageId),
        contentDescription = "back_lounge_room",
        contentScale = ContentScale.Crop,
    )
}
@Composable
fun kitchen(
    gameResurs: Game_progress,
    gameDataStore: GameDataStore,
    scope: CoroutineScope,
    dialogResurs: mother_dialogs

){
    var showDialogList = remember { mutableStateOf(false) }
    var showDialog = remember { mutableStateOf(false) }
    var currentDialogResult = remember { mutableStateOf<DialogResult?>(null) }
    var status_LazyColumn = remember {  mutableStateOf<List<DialogLine>>(emptyList()) }
    val dialogList = gameResurs.progress_dialogsMother_home.keys.toList()
    Box(
        modifier = Modifier.fillMaxSize().testTag("kitchen-room")
    ){
        background_kitchenRoom(R.drawable.kitchen_with_pers)
        Box(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(.60f).fillMaxHeight(.72f)
                .semantics { contentDescription = "Поговорить с мамой" }
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
                        val itUnlocked = gameResurs.progress_dialogsMother_home[dialogName] ?: false
                        ListItem(
                            headlineContent = { Text(dialogName) },
                            modifier = Modifier.clickable(enabled = itUnlocked) {
                                var result = when(dialogName){
                                    "Я купил нужные продукты" -> {
                                        "Расписанный диалог про это"
                                        dialogResurs.dialog_going_production()
                                    }
                                    else -> null
                                }
                                if (result != null) {
                                    currentDialogResult.value = result
                                    status_LazyColumn.value = when(dialogName){
                                        "Я купил нужные продукты" -> listOf(
                                            DialogLine(1, "Я купил нужные продукты."),
                                            DialogLine(3, "Спасибо за помощь!"),
                                        )
                                        "Разговор про вклады" -> dialogLines_vklad
                                        else -> emptyList()
                                    }
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
        DialogScene_mom(
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
fun DialogScene_mom(
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
                    // Ребенок — силуэт слева
                    Image(
                        painter = painterResource(id = R.drawable.icon_food),
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

                3 -> {
                    // Мама — силуэт справа
                    Image(
                        painter = painterResource(id = R.drawable.icon_money),
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