package com.example.thriftstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.thriftstore.data.model.Order
import com.example.thriftstore.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY order_date DESC")
    fun getOrdersByUser(userId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders ORDER BY order_date DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Int): Order?

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    suspend fun getOrderItems(orderId: Int): List<OrderItem>

    @Transaction
    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    suspend fun getOrderItemsWithProduct(orderId: Int): List<com.example.thriftstore.data.model.OrderItemWithProduct>

    @Update
    suspend fun updateOrder(order: Order)

    @Query("SELECT SUM(total_amount) FROM orders WHERE status = 'selesai'")
    fun getTotalRevenue(): Flow<Double?>
    
    @Query("SELECT COUNT(*) FROM orders WHERE status = 'selesai'")
    fun getTotalSalesCount(): Flow<Int?>

    @Query("SELECT * FROM orders WHERE status = 'selesai' ORDER BY updated_at DESC LIMIT 20")
    fun getCompletedOrders(): Flow<List<Order>>
}
