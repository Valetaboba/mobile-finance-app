package com.example.myapplication.wardrobe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.myapplication.R

@Composable
fun PlayerCharacter(
    equippedIds: Set<String>, modifier: Modifier = Modifier, onClick_wardrobe: (() -> Unit)? = null,
) {
    val body = ImageBitmap.imageResource(R.drawable.player_body)
    // Enum order defines painting order: body → top → pants → shoes.
    val layers = ClothingSlot.entries.mapNotNull { slot ->
        ClothingCatalog.items.firstOrNull { it.slot == slot && it.productId in equippedIds }
    }.map { it to ImageBitmap.imageResource(it.imageRes) }
    val interaction = if (onClick_wardrobe == null) Modifier else Modifier.clickable(
        onClickLabel = "Открыть шкаф", onClick = onClick_wardrobe,
    )
    Canvas(modifier.aspectRatio(520f / 800f).then(interaction).semantics {
        contentDescription = "Персонаж"
    }) {
        withTransform({ scale(size.width / 520f, size.height / 800f, pivot = androidx.compose.ui.geometry.Offset.Zero) }) {
            drawImage(body)
            layers.forEach { (item, bitmap) ->
                val src = item.crop
                val dst = item.placement
                drawImage(bitmap, srcOffset = IntOffset(src.x, src.y), srcSize = IntSize(src.width, src.height),
                    dstOffset = IntOffset(dst.x, dst.y), dstSize = IntSize(dst.width, dst.height), filterQuality = FilterQuality.Medium)
            }
        }
    }
}
