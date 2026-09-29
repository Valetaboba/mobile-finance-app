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
import com.example.myapplication.Dialogs.vova_dialogs
import com.example.myapplication.R
import com.example.myapplication.back.GameDataStore
import com.example.myapplication.back.Game_progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 1 - ребенок , 6 - гера
val dialogLines_bank_kart_jл = listOf(
    DialogLine(1,"Привет!"),
    DialogLine(2,"Ну здарова ЕБАТЬ"),
    DialogLine(2,"Ну как там с деньгами?"),
    DialogLine(1,"Да вот ,депнул в Евротранс"),
    DialogLine(2,"Красава ебать , а в ОИЛ Ресурс зашел ?")
)


@Composable
fun background_storage(imageId: Int){
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = imageId),
        contentDescription = "back_lounge_room",
        contentScale = ContentScale.FillHeight,
    )
}
@Composable
fun storage(
    gameResurs: Game_progress,
    gameDataStore: GameDataStore,
    scope: CoroutineScope,
    dialogResurs: vova_dialogs,
    onStartWork: () -> Unit,
) {
    val showDialogList = remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        background_storage(R.drawable.storage)
        Box(Modifier.fillMaxSize().clickable { showDialogList.value = true })
    }
    if (showDialogList.value) {
        AlertDialog(
            onDismissRequest = { showDialogList.value = false },
            title = { Text("Склад") },
            text = {
                ListItem(headlineContent = { Text("Готов к работе") }, modifier = Modifier.clickable {
                    showDialogList.value = false
                    onStartWork()
                })
            },
            confirmButton = { TextButton(onClick = { showDialogList.value = false }) { Text("Закрыть") } },
        )
    }
}

@Composable
fun DialogScene_storage(
    lines: List<DialogLine>,
    onComplete: () -> Unit
) {
    // Индекс текущей реплики
    var currentIndex = remember { mutableIntStateOf(0) }

    // Текущая реплика
    val currentLine = lines[currentIndex.value]

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
                .fillMaxSize()
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
                        painter = painterResource(id = R.drawable.vova),
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
