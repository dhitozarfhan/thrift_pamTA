package com.example.thriftstore.data.repository

import com.example.thriftstore.data.local.CartDao
import com.example.thriftstore.data.local.OrderDao
import com.example.thriftstore.data.model.CartItem
import com.example.thriftstore.data.model.CartItemWithProduct
import com.example.thriftstore.data.model.Order
import com.example.thriftstore.data.model.OrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CartRepository(private val cartDao: CartDao, private val orderDao: OrderDao) {

    fun getCartItems(userId: Int): Flow<List<CartItemWithProduct>> = cartDao.getCartItems(userId).flowOn(Dispatchers.IO)

    suspend fun addToCart(cartItem: CartItem) = withContext(Dispatchers.IO) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) = withContext(Dispatchers.IO) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun removeFromCart(cartItem: CartItem) = withContext(Dispatchers.IO) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun checkout(userId: Int, items: List<CartItem>, total: Double, note: String?): Int = withContext(Dispatchers.IO) {
        // Create Order
        val order = Order(
            user_id = userId,
            order_date = System.currentTimeMillis(),
            total_amount = total,
            status = "menunggu",
            estimated_arrival = null,
            note = note
        )
        val orderId = orderDao.insertOrder(order).toInt()
        
        cartDao.clearCart(userId)
        orderId // Return order ID
    }
    
    suspend fun createOrderItems(items: List<OrderItem>) {
        withContext(Dispatchers.IO) {
            orderDao.insertOrderItems(items)
        }
    }
}
