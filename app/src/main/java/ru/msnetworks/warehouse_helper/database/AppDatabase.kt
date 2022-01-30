package ru.msnetworks.warehouse_helper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.msnetworks.warehouse_helper.model.Shipment

@Database(entities = [Shipment::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shipmentDao(): ShipmentDao
}