package com.magicmind.eria.db_dao

import androidx.room.*

/*
@Dao
interface CartDao {
    @Query("SELECT * FROM Cart_Table")
    suspend fun getAll(): List<Cart>

    @Query("SELECT * FROM Cart_Table WHERE cartid = :cartIds")
    suspend fun loadAllByIds(cartIds: Int): Cart

    @Query("SELECT * FROM Cart_Table WHERE food_name = :foodName")
    fun getbyfoodName(foodName: String): Cart

    @Query("UPDATE Cart_Table SET food_quantity=:count WHERE food_name = :foodName")
    fun update(count: Int?, foodName: String): Cart

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(cart: Cart)

    @Delete
    fun delete(cart: Cart)

    @Query("DELETE FROM Cart_Table WHERE cartid = :cartIds")
    fun deleteByCartId(cartIds: Int)
}*/
