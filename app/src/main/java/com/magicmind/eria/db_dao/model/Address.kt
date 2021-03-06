package com.magicmind.eria.db_dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Address_Table")
data class Address(
    @ColumnInfo(name = "address")
    val Address: String?,
    @ColumnInfo(name = "floor")
    val Floor: String?,
    @ColumnInfo(name = "tag")
    val Tag: String?,
    @ColumnInfo(name = "latitude")
    val Latitude: String?,
    @ColumnInfo(name = "longitude")
    val Longitude: String?
) {
    @PrimaryKey(autoGenerate = true)
    var addressid: Int = 0
}