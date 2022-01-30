package ru.msnetworks.warehouse_helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.msnetworks.warehouse_helper.database.ShipmentDao

class MainViewModelFactory(
    private val shipmentDao: ShipmentDao,
    private val application: Application
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(application, shipmentDao) as T
}