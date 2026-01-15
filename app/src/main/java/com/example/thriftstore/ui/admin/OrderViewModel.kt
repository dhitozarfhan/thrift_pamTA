package com.example.thriftstore.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thriftstore.data.model.Order
import com.example.thriftstore.data.model.OrderItem
import com.example.thriftstore.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    val allOrders: LiveData<List<Order>> = repository.allOrders.asLiveData()
    val totalRevenue: LiveData<Double?> = repository.totalRevenue.asLiveData()
    val totalSalesCount: LiveData<Int?> = repository.totalSalesCount.asLiveData()
    val completedOrders: LiveData<List<Order>> = repository.completedOrders.asLiveData()

    private val _currentOrderItems = MutableLiveData<List<OrderItem>>()
    val currentOrderItems: LiveData<List<OrderItem>> = _currentOrderItems

    private val _currentOrderItemsWithProduct = MutableLiveData<List<com.example.thriftstore.data.model.OrderItemWithProduct>>()
    val currentOrderItemsWithProduct: LiveData<List<com.example.thriftstore.data.model.OrderItemWithProduct>> = _currentOrderItemsWithProduct

    fun getOrdersByUser(userId: Int): LiveData<List<Order>> {
        return repository.getOrdersByUser(userId).asLiveData()
    }

    fun getOrderItems(orderId: Int) = viewModelScope.launch {
        _currentOrderItemsWithProduct.value = repository.getOrderItemsWithProduct(orderId)
    }
    
    // Kept for backward compatibility if needed, but updated getOrderItems to use joined version

    fun updateStatus(order: Order, newStatus: String) = viewModelScope.launch {
        val updatedOrder = order.copy(
            status = newStatus,
            updated_at = System.currentTimeMillis()
        )
        repository.updateOrder(updatedOrder)
    }
}

class OrderViewModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
