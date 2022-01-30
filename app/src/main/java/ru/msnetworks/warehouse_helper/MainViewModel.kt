package ru.msnetworks.warehouse_helper

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.msnetworks.warehouse_helper.database.ShipmentDao
import ru.msnetworks.warehouse_helper.model.Shipment
import ru.msnetworks.warehouse_helper.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat.getDateTimeInstance
import java.util.*

class MainViewModel(
    private val appContext: Application,
    private val shipmentDao: ShipmentDao
) : AndroidViewModel(appContext) {

    val filter = mutableStateOf("")
    val shipmentListFiltered = mutableStateOf(emptyList<Shipment>())

    private val shipmentList = mutableStateOf(emptyList<Shipment>())

    fun getShipmentDao() = shipmentDao

    fun onLaunch() {
        viewModelScope
            .launch {
                shipmentDao
                    .getAll()
                    .collect { list ->
                        shipmentList.value = list
                        shipmentListFiltered.value = getShipmentListFiltered()
                    }
            }
    }

    fun getBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    appContext.contentResolver,
                    imageUri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(appContext.contentResolver, imageUri)
        }
    }

    fun saveBitmapToLocalFile(bitmap: Bitmap): String {
        var isError = false
        val file = File
            .createTempFile(
                "image_${getDateTimeInstance().format(Date())}",  /* prefix */
                ".jpg",  /* suffix */
                appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
            )

        try {
            FileOutputStream(file.absolutePath).use { out ->
                Utils
                    .getScaledBitmap(bitmap)
                    .compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
        } catch (e: IOException) {
            isError = true
            e.printStackTrace()
        }
        return if (isError) {
            ""
        } else {
            FileProvider.getUriForFile(appContext, appContext.packageName + ".provider", file)
                .toString()
        }
    }

    fun getImageURI(): Uri {
        val file = File
            .createTempFile(
                "image_${getDateTimeInstance().format(Date())}",  /* prefix */
                ".jpg",  /* suffix */
                appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
            )
        return FileProvider.getUriForFile(appContext, appContext.packageName + ".provider", file)
    }

    fun onFilterChanged(filterText: String) {
        filter.value = filterText
        shipmentListFiltered.value = getShipmentListFiltered()
    }

    private fun getShipmentListFiltered(): List<Shipment> {
        return shipmentList.value.filter { shipment ->
            if (filter.value.isEmpty()) {
                true
            } else {
                shipment.isMatchFilter(filter.value)
            }
        }
    }
}