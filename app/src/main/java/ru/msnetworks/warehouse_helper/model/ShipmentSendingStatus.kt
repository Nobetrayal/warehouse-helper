package ru.msnetworks.warehouse_helper.model

sealed class ShipmentSendingStatus(val id: Int) {
    object Created : ShipmentSendingStatus(0)
    object InProcess : ShipmentSendingStatus(1)
    object Success : ShipmentSendingStatus(2)
    object Fail : ShipmentSendingStatus(3)

    companion object {
        fun getById(id: Int): ShipmentSendingStatus = when (id) {
            Success.id -> Success
            Fail.id -> Fail
            Created.id -> Created
            InProcess.id -> InProcess
            else -> Created
        }
    }
}
