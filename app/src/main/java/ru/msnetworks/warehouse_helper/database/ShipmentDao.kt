package ru.msnetworks.warehouse_helper.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.msnetworks.warehouse_helper.model.Shipment

@Dao
interface ShipmentDao {
    @Query("Select * from shipment ORDER by id desc")
    fun getAll(): Flow<List<Shipment>>

    @Query("Select * from shipment Where id = :id")
    suspend fun getShipmentById(id: Int): Shipment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shipment: Shipment)

    @Delete
    suspend fun delete(shipment: Shipment)
}