package ru.msnetworks.warehouse_helper.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.msnetworks.warehouse_helper.R

@Composable
fun ScreenHeader(
    screenTitle: String,
    isChanged: Boolean,
    navController: NavController,
    onSaveClickListener: () -> Unit = {}
) {
    val isClickable = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "",
            modifier = Modifier
                .weight(0.1f)
                .width(24.dp)
                .height(24.dp)
                .clickable(
                    enabled = isClickable.value
                ) {
                    isClickable.value = false
                    // ask for save.
                    navController.popBackStack()
                }
        )
        Text(
            text = screenTitle,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.7f)
                .padding(start = 8.dp)
                .clickable(
                    enabled = isClickable.value
                ) {
                    isClickable.value = false
                    // ask for save.
                    navController.popBackStack()
                },
            maxLines = 1
        )
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth().weight(0.2f)
        ) {
            Text(text = "Save",
                color = colorResource(R.color.orange),
                modifier = Modifier
                    .clickable {
                        if (isChanged) onSaveClickListener.invoke()
                    }
            )
        }
    }
    TabRowDefaults.Divider(
        color = colorResource(id = R.color.black_semitransparent),
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
}