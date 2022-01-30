package ru.msnetworks.warehouse_helper.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.msnetworks.warehouse_helper.R

@Composable
fun FullscreenImage(
    image: ImageBitmap,
    onClick: () -> Unit = {}
) {
    BackHandler {
        onClick.invoke()
    }
    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.black_semitransparent))
            .clickable {
                onClick.invoke()
            }
    ) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .clickable {
                    onClick.invoke()
                }
                .padding(all = 12.dp)
        ) {
            Image(
                image,
                null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onClick.invoke()
                    }
            )
        }
    }
}