package com.example.thriftstore.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thriftstore.data.model.CartItem
import com.example.thriftstore.data.model.CartItemWithProduct
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.model.OrderItem
import com.example.thriftstore.data.repository.CartRepository
import com.example.thriftstore.data.repository.CatalogRepository
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val allProducts: LiveData<List<Katalog>> = catalogRepository.allKatalog.asLiveData()

    fun getCartItems(userId: Int): LiveData<List<CartItemWithProduct>> = cartRepository.getCartItems(userId).asLiveData()

    fun addToCart(userId: Int, product: Katalog, quantity: Int) = viewModelScope.launch {
        val item = CartItem(user_id = userId, katalog_id = product.id, quantity = quantity)
        cartRepository.addToCart(item)
    }
    
    fun removeFromCart(item: CartItem) = viewModelScope.launch {
        cartRepository.removeFromCart(item)
    }

    fun checkout(userId: Int, cartItems: List<CartItem>, products: List<Katalog>, note: String? = null) = viewModelScope.launch {
        var total = 0.0
        val orderItems = mutableListOf<OrderItem>()
        
        cartItems.forEach { cartItem ->
            val product = products.find { it.id == cartItem.katalog_id }
            if (product != null) {
                total += product.price * cartItem.quantity
            }
        }
        
        val orderId = cartRepository.checkout(userId, cartItems, total, note)
        
        cartItems.forEach { cartItem ->
            val product = products.find { it.id == cartItem.katalog_id }
            if (product != null) {
                orderItems.add(OrderItem(order_id = orderId, katalog_id = product.id, quantity = cartItem.quantity, price_at_purchase = product.price))
            }
        }
        cartRepository.createOrderItems(orderItems)
    }
    
     suspend fun getProduct(id: Int): Katalog? {
         return catalogRepository.getKatalogById(id)
     }
}

class CustomerViewModelFactory(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(catalogRepository, cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
