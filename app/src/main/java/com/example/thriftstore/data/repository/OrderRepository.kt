package com.example.thriftstore.data.repository

import com.example.thriftstore.data.local.OrderDao
import com.example.thriftstore.data.model.Order
import com.example.thriftstore.data.model.OrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class OrderRepository(private val orderDao: OrderDao) {

    val allOrders: Flow<List<Order>> = orderDao.getAllOrders().flowOn(Dispatchers.IO)
    val totalRevenue: Flow<Double?> = orderDao.getTotalRevenue().flowOn(Dispatchers.IO)
    val totalSalesCount: Flow<Int?> = orderDao.getTotalSalesCount().flowOn(Dispatchers.IO)
    val completedOrders: Flow<List<Order>> = orderDao.getCompletedOrders().flowOn(Dispatchers.IO)

    fun getOrdersByUser(userId: Int): Flow<List<Order>> = orderDao.getOrdersByUser(userId).flowOn(Dispatchers.IO)

    suspend fun getOrderById(id: Int): Order? = withContext(Dispatchers.IO) {
        orderDao.getOrderById(id)
    }

    suspend fun getOrderItems(orderId: Int): List<OrderItem> = withContext(Dispatchers.IO) {
        orderDao.getOrderItems(orderId)
    }

    suspend fun getOrderItemsWithProduct(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.getOrderItemsWithProduct(orderId)
    }

    suspend fun updateOrder(order: Order) = withContext(Dispatchers.IO) {
        orderDao.updateOrder(order)
    }
}
