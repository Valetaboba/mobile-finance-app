package com.example.myapplication.front

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun function_mid(imageId: Int){
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = imageId),
        contentDescription = "Левая_стена",
        contentScale = ContentScale.FillBounds,
    )
}
@Composable
fun function_mid2(imageId: Int){
    Image(
        modifier = Modifier.fillMaxSize()
            .graphicsLayer {
                cameraDistance = 0f * density  // 👈 Отдаляет/приближает камеру
            },
        painter = painterResource(id = imageId),
        contentDescription = "Правая_стена",
        contentScale = ContentScale.FillHeight,
    )
}
@Composable
fun function_bot(imageId: Int){
    Image(
        painter = painterResource(id = imageId),
        contentDescription = "пол",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()

    )
}
class DiagonalShape(
    private val fromTopLeft: Int   // true = от левого верхнего угла
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            if (fromTopLeft == 0) {
                // Треугольник: левый верхний -> правый нижний -> левый нижний
                moveTo(size.width * 5/10, 0f)
                lineTo(size.width * 5/10, size.height * 3/5)
                lineTo(0f, size.height * 70/100)
                lineTo(0f,y = 0f)
                close()
            } else if (fromTopLeft == 1){
                // Треугольник: правый верхний -> левый нижний -> правый нижний
                moveTo(size.width * 5/10, 0f)
                lineTo(size.width * 5/10, size.height * 3/5)
                lineTo(size.width, size.height * 70/100)
                lineTo(size.width, y = 0f)
                close()
            }
            else if (fromTopLeft ==2 ){
                moveTo(0f, size.height * 70/100)
                lineTo(size.width * 5/10, size.height * 3/5)
                lineTo(size.width, size.height * 70/100)
                lineTo(size.width, size.height * 100/100)
                lineTo(0f,size.height * 100/100)
                close()
            }
        }
        return Outline.Generic(path)
    }
}
@Composable
fun Mid_block() {
    Box(
        modifier = Modifier
            .clip(DiagonalShape(fromTopLeft = 0))
            .border(
                width = 4.dp,          // Толщина рамки
                color = Color.Yellow ,   // Цвет рамки
                shape = DiagonalShape(fromTopLeft = 0)
            )
    ) {
        function_mid(imageId = R.drawable.mid_texture)
    }

    Box(
        modifier = Modifier
            .clip(DiagonalShape(fromTopLeft = 1))

            .border(
                width = 4.dp,          // Толщина рамки
                color = Color.Red,    // Цвет рамки
                shape = DiagonalShape(fromTopLeft = 1)
            )
    ) {
        function_mid2(imageId = R.drawable.stena)
    }
    Box(
        modifier = Modifier
            .clip(DiagonalShape(fromTopLeft = 2))

        //.background(Color(0xFFD7B071))  // Коричневый фон
    ) {
        function_bot(imageId = R.drawable.pol_brebno)
    }
}