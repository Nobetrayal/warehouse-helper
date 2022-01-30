package ru.msnetworks.warehouse_helper.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.msnetworks.warehouse_helper.MainViewModel
import ru.msnetworks.warehouse_helper.R
import ru.msnetworks.warehouse_helper.Screen
import ru.msnetworks.warehouse_helper.model.Shipment
import ru.msnetworks.warehouse_helper.model.ShipmentSendingStatus

@Composable
fun ShipmentsListScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    val items = remember { mainViewModel.shipmentListFiltered }
    val filter = remember { mainViewModel.filter }

    LaunchedEffect(key1 = Unit) {
        mainViewModel.onLaunch()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.image_button))
    ) {
        Text(
            text = "Отгрузки:",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, start = 12.dp)
        )

        OutlinedTextField(
            value = filter.value,
            singleLine = true,
            label = {
                Text("Фильтр")
            },
            onValueChange = { filterText ->
                mainViewModel.onFilterChanged(filterText)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            mainViewModel.onFilterChanged("")
                        }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
        ) {
            items(items.value) { item ->
                ShipmentItem(item) {
                    navController.navigate(Screen.ShipmentScreen.withArgs(item.id ?: -1))
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            modifier = Modifier.padding(all = 24.dp),
            onClick = {
                navController.navigate(Screen.ShipmentScreen.withArgs(-1))
            }
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}

@Composable
fun ShipmentItem(shipment: Shipment, onClickAction: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClickAction),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(all = 4.dp)) {
                Row {
                    Text(text = "№ ${shipment.number}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.padding(all = 8.dp))
                    Text(text = shipment.year, fontSize = 16.sp)
                }
                Text(text = shipment.customerName)
                Text(text = shipment.totalPrice)
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                val resource = when (shipment.sendingStatus) {
                    ShipmentSendingStatus.Created.id -> {
                        R.drawable.ic_pending
                    }
                    ShipmentSendingStatus.Success.id -> {
                        R.drawable.ic_check
                    }
                    ShipmentSendingStatus.Fail.id -> {
                        R.drawable.ic_cross
                    }
                    else -> {
                        R.drawable.ic_progress
                    }
                }
                Image(
                    painter = painterResource(id = resource),
                    contentDescription = "",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        }
    }
}

fun getMocked(): List<Shipment> = listOf(
    Shipment(
        id = 1,
        number = "1512",
        year = "20.09.2020г"
    ),
    Shipment(
        id = 2,
        number = "45651",
        sendingStatus = 9,
        year = "21.09.2020г"
    ),
    Shipment(
        id = 3,
        number = "121320",
        year = "20.12.2021г"
    ),
    Shipment(
        id = 4,
        number = "1412",
        year = "20.01.2022г"
    ),
    Shipment(
        id = 5,
        number = "8852",
        year = "2.02.2020г"
    ),
    Shipment(
        id = 6,
        number = "2165415",
        customerName = "Жестяная лавка",
        sendingStatus = 1,
        year = "09.09.2021г"
    ),
    Shipment(
        id = 7,
        number = "14561",
        customerName = "Жестяная лавка",
        year = "02.09.2020г"
    ),
    Shipment(
        id = 8,
        number = "234234",
        customerName = "Жестяная лавка",
        sendingStatus = 2,
        year = "20.09.2010г"
    ),
    Shipment(
        id = 9,
        number = "457",
        customerName = "Жестяная лавка",
        year = "20.09.2014г"
    ),
    Shipment(
        id = 10,
        number = "234234",
        customerName = "Обдираловка",
        year = "10.09.2021г"
    ),
    Shipment(
        id = 11,
        number = "345546",
        customerName = "Обдираловка",
        year = "11.11.2021г"
    ),
    Shipment(
        id = 12,
        number = "679987",
        customerName = "Обдираловка",
        year = "22.11.2021г"
    ),
    Shipment(
        id = 13,
        number = "34264",
        customerName = "Обдираловкаа",
        year = "31.12.2021г"
    ),
    Shipment(
        id = 14,
        number = "2165415",
        year = "20.09.2020г"
    ),
)