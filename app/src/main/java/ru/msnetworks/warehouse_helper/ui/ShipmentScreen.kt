package ru.msnetworks.warehouse_helper.ui

import QRCodePreview
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.msnetworks.warehouse_helper.MainViewModel
import ru.msnetworks.warehouse_helper.R
import ru.msnetworks.warehouse_helper.ShipmentViewModel
import ru.msnetworks.warehouse_helper.ShipmentViewModelFactory
import ru.msnetworks.warehouse_helper.model.Shipment.Companion.emptyShipment


@Composable
fun ShipmentScreen(
    id: Int,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    Log.d("ShipmentScreen", "init")
    val shipmentViewModel: ShipmentViewModel = viewModel(
        factory = ShipmentViewModelFactory(
            mainViewModel.getShipmentDao()
        )
    )
    LaunchedEffect(key1 = id) {
        shipmentViewModel.initShipment(id)
    }

    val isChanged = remember {
        shipmentViewModel.isChanged
    }
    val number = remember {
        shipmentViewModel.number
    }
    val shipmentState = remember { shipmentViewModel.shipment }
    val images = remember {
        shipmentViewModel.images
    }
    val isShowDialog = remember {
        mutableStateOf(false)
    }

    val isShowImageDialog = remember {
        mutableStateOf(false)
    }

    val isShowBarcodePreview = remember { mutableStateOf(false) }

    val fileUri = remember { shipmentViewModel.currentFileUri }

    val emptyBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap()
    val currentImageBitmap = remember {
        mutableStateOf(emptyBitmap)
    }

    var expanded by remember { mutableStateOf(false) }
    val options = getMocked()

    val cameraPictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && fileUri.value != Uri.EMPTY) {
            shipmentViewModel.updateImages(shipmentViewModel.currentFileUri.value.toString())
            shipmentViewModel.currentFileUri.value = Uri.EMPTY
            shipmentViewModel.isChanged.value = true
        }
    }

    val imageFromGalleryLauncher = rememberLauncherForActivityResult(
        object : ActivityResultContracts.GetMultipleContents() {
            override fun createIntent(context: Context, input: String): Intent {
                super.createIntent(context, input)
                return Intent(Intent.ACTION_PICK)
                    .setType(input)
                    .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        }
    ) { uris ->
        uris
            .map { uri ->
                val bitmap = mainViewModel.getBitmap(uri)
                // Original url rotten after relaunch app, so save it to local.
                mainViewModel.saveBitmapToLocalFile(bitmap)
            }
            .filter { it.isNotEmpty() }
            .also {
                shipmentViewModel.updateImages(it)
                shipmentViewModel.isChanged.value = true
            }
    }

    OnSaveErrorDialog(isShowDialog.value) { isShowDialog.value = false }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.image_button))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 12.dp)
        ) {
            ScreenHeader(
                if (shipmentState.value == emptyShipment) {
                    "Реализация: создание"
                } else {
                    "Реализация: № ${shipmentViewModel.number.value} ${shipmentViewModel.year}г."
                },
                isChanged.value,
                navController
            ) {
                if (number.value.isEmpty()) {
                    isShowDialog.value = true
                } else {
                    shipmentViewModel.saveShipment(images)
                    navController.popBackStack()
                }
            }

            if (shipmentState.value == emptyShipment) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = number.value,
                        singleLine = true,
                        label = {
                            Text("Номер реализации")
                        },
                        onValueChange = { newNumber ->
                            shipmentViewModel.number.value = newNumber
                            shipmentViewModel.isChanged.value = true
                            if (newNumber.length >= 2 && options.any { shipment ->
                                    shipment.number.contains(shipmentViewModel.number.value)
                                }
                            ) {
                                expanded = true
                            }
                        },
                        modifier = Modifier
                            .weight(0.8f)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = !expanded },
                        properties = PopupProperties(focusable = false),
                        modifier = Modifier.focusable(false)
                    ) {
                        options
                            .filter { shipment ->
                                shipment.number.contains(shipmentViewModel.number.value)
                            }
                            .forEach { shipment ->
                                DropdownMenuItem(
                                    onClick = {
                                        shipmentViewModel.number.value = shipment.number
                                        shipmentViewModel.year.value = shipment.year
                                        shipmentViewModel.customerName.value = shipment.customerName
                                        shipmentViewModel.totalPrice.value = shipment.totalPrice
                                        expanded = false
                                    }
                                ) {
                                    Card(
                                        elevation = 2.dp,
                                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(all = 4.dp)) {
                                            Row {
                                                Text(text = "№ ${shipment.number}")
                                                Spacer(
                                                    modifier = Modifier
                                                        .padding(all = 8.dp)
                                                        .fillMaxHeight()
                                                )
                                                Text(text = shipment.year)
                                            }
                                            Spacer(
                                                modifier = Modifier
                                                    .padding(all = 2.dp)
                                                    .fillMaxWidth()
                                            )
                                            Row {
                                                Text(text = shipment.customerName)
                                                Spacer(
                                                    modifier = Modifier
                                                        .padding(all = 8.dp)
                                                        .fillMaxHeight()
                                                )
                                                Text(text = shipment.totalPrice)
                                            }
                                        }
                                    }
                                }
                            }
                    }
                    ImagedButton(
                        iconRes = R.drawable.ic_qrcode,
                        modifier = Modifier.weight(0.2f)
                    ) {
                        isShowBarcodePreview.value = true
                    }
                }
            } else {
                Text(
                    text = shipmentViewModel.number.value,
                    modifier = Modifier.weight(0.8f)
                )
            }
            Column(
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                Text(text = shipmentViewModel.year.value)
                Text(text = shipmentViewModel.customerName.value)
                Text(text = shipmentViewModel.totalPrice.value)
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    ImagedButton(
                        iconRes = R.drawable.ic_camera,
                        modifier = Modifier.padding(all = 12.dp)
                    ) {
                        shipmentViewModel.currentFileUri.value = mainViewModel.getImageURI()
                        cameraPictureLauncher.launch(fileUri.value)
                    }

                    ImagedButton(
                        iconRes = R.drawable.ic_gallery,
                        modifier = Modifier.padding(all = 12.dp)
                    ) {
                        imageFromGalleryLauncher.launch("image/*")
                    }
                }
            }
            Box(
                contentAlignment = BiasAlignment(1f, 0.5f)
            ) {
                ImageGallery(images, mainViewModel) { bitmap ->
                    currentImageBitmap.value = bitmap
                    isShowImageDialog.value = true
                }
            }
        }

        if (isShowImageDialog.value) {
            FullscreenImage(
                currentImageBitmap.value
            ) {
                currentImageBitmap.value = emptyBitmap
                isShowImageDialog.value = false
            }
        }

        if (!shipmentViewModel.isChanged.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(all = 24.dp),
                    onClick = {
                        shipmentViewModel.sendToServer()
                    }
                ) {
                    Icon(painterResource(id = R.drawable.ic_send), "")
                }
            }
        }

        if (isShowBarcodePreview.value) {
            QRCodePreview { code ->
                if (code.isNotEmpty()) {
                    shipmentViewModel.number.value = code
                    expanded = true
                }
                isShowBarcodePreview.value = false
            }
        }
    }
}


@Composable
fun OnSaveErrorDialog(isShowDialog: Boolean, onDismiss: () -> Unit) {
    if (isShowDialog) {
        Dialog(onDismissRequest = { onDismiss.invoke() }) {
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(all = 12.dp)) {
                    Text(text = "Необходимо указать номер реализации!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { onDismiss.invoke() }) {
                            Text(text = "CANCEL")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImagedButton(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    onClickListener: () -> Unit
) {
    Surface(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .wrapContentWidth()
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "",
            modifier = Modifier
                .size(48.dp)
                .clickable { onClickListener.invoke() }
                .padding(all = 4.dp)
        )
    }
}