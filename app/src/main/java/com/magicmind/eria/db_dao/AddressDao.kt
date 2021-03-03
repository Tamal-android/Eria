package com.magicmind.eria.db_dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.magicmind.eria.db_dao.model.Address

@Dao
interface AddressDao {
    @Query("SELECT * FROM Address_Table")
    suspend fun getAll(): List<Address>

    @Query("SELECT * FROM Address_Table WHERE addressid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Address>

    /* @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
             "last_name LIKE :last LIMIT 1")
     fun findByName(first: String, last: String): User*/

    @Insert
    fun insertAll(addresses: Address)

    @Delete
    fun delete(addresses: Address)

    @Query("DELETE FROM Address_Table WHERE addressid = :addressId")
    fun deleteByUserId(addressId: Int)
}