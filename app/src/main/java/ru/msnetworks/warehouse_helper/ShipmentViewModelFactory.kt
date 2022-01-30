package ru.msnetworks.warehouse_helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.msnetworks.warehouse_helper.database.ShipmentDao

class ShipmentViewModelFactory(
    private val shipmentDao: ShipmentDao
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ShipmentViewModel(shipmentDao) as T
}