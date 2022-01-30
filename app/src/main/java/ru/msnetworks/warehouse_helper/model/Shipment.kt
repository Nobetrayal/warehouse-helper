package ru.msnetworks.warehouse_helper.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Shipment(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val number: String = "",
    val year: String = "",
    val customerName: String = "",
    val totalPrice: String = "",
    val files: List<String> = emptyList(),
    val sendingStatus: Int = 0
) {

    fun isMatchFilter(filter: String): Boolean {
        val lowerCaseFilter = filter.lowercase(Locale.getDefault())
        return number.lowercase(Locale.getDefault()).contains(lowerCaseFilter)
                || customerName.lowercase(Locale.getDefault()).contains(lowerCaseFilter)
                || year.lowercase(Locale.getDefault()).contains(lowerCaseFilter)
    }

    companion object {
        val emptyShipment = Shipment(id = null)
    }
}