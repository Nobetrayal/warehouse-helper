package ru.msnetworks.warehouse_helper

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.msnetworks.warehouse_helper.database.ShipmentDao
import ru.msnetworks.warehouse_helper.model.Shipment
import ru.msnetworks.warehouse_helper.model.Shipment.Companion.emptyShipment
import ru.msnetworks.warehouse_helper.model.ShipmentSendingStatus

class ShipmentViewModel(
    private val shipmentDao: ShipmentDao
) : ViewModel() {

    var currentId = -1
    var number = mutableStateOf("")
    var year = mutableStateOf("")
    var customerName = mutableStateOf("")
    var totalPrice = mutableStateOf("")
    var sendingStatus: MutableState<ShipmentSendingStatus> =
        mutableStateOf(ShipmentSendingStatus.Created)
    var shipment = mutableStateOf(emptyShipment)

    var isChanged = mutableStateOf(false)
    val images = mutableStateListOf<String>()

    var currentFileUri = mutableStateOf(Uri.EMPTY)

    fun initShipment(id: Int) {
        currentId = id
        if (id < 0) {
            isChanged.value = true
        } else {
            viewModelScope.launch {
                val storedShipment = shipmentDao.getShipmentById(id)
                shipment.value = storedShipment
                number.value = storedShipment.number
                year.value = storedShipment.year
                customerName.value = storedShipment.customerName
                images.clear()
                images.addAll(storedShipment.files)
                totalPrice.value = storedShipment.totalPrice
                sendingStatus.value = ShipmentSendingStatus.getById(storedShipment.sendingStatus)
                isChanged.value = false
            }
        }
    }

    fun sendToServer() {
        if (sendingStatus.value.id != ShipmentSendingStatus.Success.id) {
            sendingStatus.value = ShipmentSendingStatus.InProcess
            viewModelScope.launch {
                val newShipment = shipment.value.copy(sendingStatus = ShipmentSendingStatus.InProcess.id)
                shipment.value = newShipment
                shipmentDao.insert(newShipment)
            }
            // send to server
        }
    }

    fun updateImages(image: String) {
        images.add(image)
    }

    fun updateImages(imageList: List<String>) {
        images.addAll(imageList)
    }

    fun saveShipment(images: List<String>) {
        viewModelScope.launch {
            val newShipment = Shipment(
                number = number.value,
                year = year.value,
                customerName = customerName.value,
                files = images,
                id = if (currentId < 0) null else currentId
            )
            shipment.value = newShipment
            shipmentDao.insert(newShipment)
            isChanged.value = false
            // send to server
        }
    }
}