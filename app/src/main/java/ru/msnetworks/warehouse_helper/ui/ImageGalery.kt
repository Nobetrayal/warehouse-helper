package ru.msnetworks.warehouse_helper.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ru.msnetworks.warehouse_helper.MainViewModel
import ru.msnetworks.warehouse_helper.utils.Utils

@Composable
fun ImageGallery(
    images: SnapshotStateList<String>,
    mainViewModel: MainViewModel,
    onItemClicked: (ImageBitmap) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        items(images) { item ->
            Box(
                modifier = Modifier
                    .padding(all = 4.dp)
                    .size(128.dp)
            ) {
                Surface(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val bitmap = Utils
                        .getScaledBitmap(mainViewModel.getBitmap(Uri.parse(item)), true)
                        .asImageBitmap()
                    Image(
                        bitmap,
                        null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onItemClicked.invoke(bitmap)
                            }
                    )
                }
            }
        }
    }
}