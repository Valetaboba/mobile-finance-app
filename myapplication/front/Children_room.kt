package com.example.myapplication.front

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import com.example.myapplication.wardrobe.PlayerCharacter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import com.example.myapplication.R

private const val FAR_TOP_Y    = 0f        // Y дальнего верхнего угла (0 = верх экрана)
private const val FAR_BOTTOM_Y = 3f / 5f   // Y дальнего нижнего угла комнаты
private const val SIDE_BOTTOM_Y = 7f / 10f // Y, где пол стыкуется с боковыми стенами
private const val CENTER_X     = 5f / 10f  // X центральной вертикали комнаты



// ============================================================
// ПЕРСПЕКТИВНОЕ РИСОВАНИЕ
// ============================================================

@Composable
private fun rememberBitmap(imageId: Int): ImageBitmap? {
    val context = LocalContext.current
    return remember(imageId) {
        BitmapFactory.decodeResource(context.resources, imageId)?.asImageBitmap()
    }
}

/**
 * Рисует участок исходной картинки [bitmap] в произвольный четырёхугольник [dst]
 * (проективное преобразование через Matrix.setPolyToPoly).
 *
 * @param srcFrac координаты углов источника в долях (0..1) в порядке TL, TR, BR, BL
 * @param dst     координаты углов назначения в пикселях в том же порядке
 */
private fun DrawScope.drawPerspective(
    bitmap: ImageBitmap,
    srcFrac: FloatArray,
    dst: FloatArray,
    alpha: Int = 255
) {
    if (srcFrac.size != 8 || dst.size != 8) return
    drawIntoCanvas { canvas ->
        val nativeCanvas = canvas.nativeCanvas
        val matrix = android.graphics.Matrix()
        val src = floatArrayOf(
            srcFrac[0] * bitmap.width,  srcFrac[1] * bitmap.height,
            srcFrac[2] * bitmap.width,  srcFrac[3] * bitmap.height,
            srcFrac[4] * bitmap.width,  srcFrac[5] * bitmap.height,
            srcFrac[6] * bitmap.width,  srcFrac[7] * bitmap.height
        )
        if (!matrix.setPolyToPoly(src, 0, dst, 0, 4)) return@drawIntoCanvas
        nativeCanvas.save()
        // Clip each transformed texture to its destination, including each half of the floor.
        nativeCanvas.clipPath(android.graphics.Path().apply {
            moveTo(dst[0], dst[1])
            lineTo(dst[2], dst[3]); lineTo(dst[4], dst[5]); lineTo(dst[6], dst[7]); close()
        })
        nativeCanvas.concat(matrix)
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG or android.graphics.Paint.FILTER_BITMAP_FLAG).apply { this.alpha = alpha }
        nativeCanvas.drawBitmap(bitmap.asAndroidBitmap(), 0f, 0f, paint)
        nativeCanvas.restore()
    }
}

/**
 * Стена. Использует ОДНУ прямоугольную текстуру для обеих стен.
 * @param imageId id прямоугольной картинки-текстуры
 * @param isLeft  true — левая стена, false — правая
 */
@Composable
private fun ChildrenWall(imageId: Int, isLeft: Boolean) {
    val bitmap = rememberBitmap(imageId) ?: return
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val fullSrc = floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f)

        val dst = if (isLeft) {
            // Ближний край — слева (x=0), дальний — по центру (x=w*CENTER_X)
            floatArrayOf(
                0f,               h * FAR_TOP_Y,      // TL — ближний верх
                w * CENTER_X,     h * FAR_TOP_Y,      // TR — дальний верх
                w * CENTER_X,     h * FAR_BOTTOM_Y,   // BR — дальний низ
                0f,               h * SIDE_BOTTOM_Y   // BL — ближний низ
            )
        } else {
            // Ближний край — справа (x=w), дальний — по центру (x=w*CENTER_X)
            floatArrayOf(
                w * CENTER_X,     h * FAR_TOP_Y,
                w,                h * FAR_TOP_Y,
                w,                h * SIDE_BOTTOM_Y,
                w * CENTER_X,     h * FAR_BOTTOM_Y
            )
        }
        drawPerspective(bitmap, fullSrc, dst)
    }
}

/**
 * Пол. Использует ОДНУ прямоугольную текстуру пола.
 * Пол делится на две трапеции; каждая получает свою половину картинки,
 * чтобы текстура оставалась непрерывной в центре.
 */
@Composable
private fun ChildrenFloor(imageId: Int) {
    val bitmap = rememberBitmap(imageId) ?: return
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Левая половина текстуры -> левая трапеция пола
        drawPerspective(
            bitmap = bitmap,
            srcFrac = floatArrayOf(0f, 0f, 0.5f,0f, 0.5f, 1f, 0f, 1f),
            dst = floatArrayOf(
                0f,               h * SIDE_BOTTOM_Y,  // дальний левый угол комнаты
                w * CENTER_X,     h * FAR_BOTTOM_Y,   // дальний центральный угол
                w * CENTER_X,     h,                  // ближний центр экрана
                0f,               h                   // ближний левый угол экрана
            )
        )
        // Правая половина текстуры -> правая трапеция пола
        drawPerspective(
            bitmap = bitmap,
            srcFrac = floatArrayOf(0.5f, 0f, 1f, 0f, 1f, 1f, 0.5f, 1f),
            dst = floatArrayOf(
                w * CENTER_X,     h * FAR_BOTTOM_Y,
                w,                h * SIDE_BOTTOM_Y,
                w,                h,
                w * CENTER_X,     h
            )
        )
    }
}

/** Children's room assembled from the supplied room/ textures; no window. */
@Composable
fun chill_room(equippedIds: Set<String>, onClick_wardrobe: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize().clipToBounds().testTag("children-room")) {
        val w = maxWidth
        val h = maxHeight
        ChildrenWall(R.drawable.wall1, isLeft = true)
        ChildrenWall(R.drawable.wall1, isLeft = false)
        ChildrenFloor(R.drawable.floor3)
        Canvas(Modifier.fillMaxSize()) {
            val corner = Offset(size.width * CENTER_X, size.height * FAR_BOTTOM_Y)
            val stroke = 2.dp.toPx()
            drawLine(Color(0xFF112E2C), Offset(size.width * CENTER_X, 0f), corner, stroke)
            drawLine(Color(0xFF112E2C), Offset(0f, size.height * SIDE_BOTTOM_Y), corner, stroke)
            drawLine(Color(0xFF112E2C), corner, Offset(size.width, size.height * SIDE_BOTTOM_Y), stroke)
        }
        // Furniture positions adapt to the available location area, preserving texture proportions.
        val wardrobeHeight = minOf(h * .48f, w * .88f)
        RoomObject(R.drawable.shkaf, "Шкаф в комнате", 196, 20, 539, 942,
            Modifier.align(Alignment.TopEnd).offset(y = h * .25f)
                .size(wardrobeHeight * 539f / 942f, wardrobeHeight)
                .clickable(onClickLabel = "Открыть шкаф", onClick = onClick_wardrobe))
        val bedWidth = minOf(w * .64f, h * .62f)
        RoomObject(R.drawable.bed_4, "Кровать", 19, 37, 517, 363,
            Modifier.offset(x = w * .01f, y = h * .55f).size(bedWidth, bedWidth * 363f / 517f))
        val tableHeight = minOf(h * .27f, w * .59f)
        RoomObject(R.drawable.table_1, "Стол", 38, 20, 222, 268,
            Modifier.align(Alignment.BottomStart).offset(x = -w * .06f)
                .size(tableHeight * 222f / 268f, tableHeight))
        val flowerWidth = minOf(w * .18f, h * .12f)
        RoomObject(R.drawable.flower, "Цветок", 64, 21, 236, 254,
            Modifier.offset(x = w * .20f - 15.dp, y = h - tableHeight + tableHeight * .28f - flowerWidth * 254f / 236f - 5.dp)
                .size(flowerWidth, flowerWidth * 254f / 236f))
        val headphoneWidth = minOf(w * .21f, h * .15f)
        RoomObject(R.drawable.earphone, "Наушники", 24, 40, 276, 208,
            Modifier.offset(x = w * .02f - 5.dp, y = h - tableHeight + tableHeight * .12f - 24.dp)
                .size(headphoneWidth, headphoneWidth * 208f / 276f))
        val ballSize = minOf(w * .13f, h * .09f)
        RoomObject(R.drawable.ball, "Мяч", 89, 60, 138, 137,
            Modifier.align(Alignment.BottomStart).offset(x = w * .09f + 55.dp, y = -h * .035f).size(ballSize))
        val playerHeight = minOf(h * .64f, w * 1.38f)
        PlayerCharacter(equippedIds,
            Modifier.align(Alignment.BottomEnd).offset(x = w * .04f, y = -h * .045f).height(playerHeight),
            onClick_wardrobe)
        FilledTonalButton(onClick = onClick_wardrobe,
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)) { Text("Шкаф") }
    }
}

/** Crop transparent export margins at draw time; original assets remain unchanged. */
@Composable
private fun RoomObject(
    imageId: Int, description: String, x: Int, y: Int, width: Int, height: Int, modifier: Modifier,
) {
    val bitmap = rememberBitmap(imageId) ?: return
    Canvas(modifier.semantics { contentDescription = description }) {
        drawImage(bitmap, srcOffset = IntOffset(x, y), srcSize = IntSize(width, height),
            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
            filterQuality = androidx.compose.ui.graphics.FilterQuality.Medium)
    }
}
