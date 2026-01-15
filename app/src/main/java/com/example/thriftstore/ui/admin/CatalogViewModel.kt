package com.example.thriftstore.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.repository.CatalogRepository
import kotlinx.coroutines.launch

class CatalogViewModel(private val repository: CatalogRepository) : ViewModel() {

    val allKatalog: LiveData<List<Katalog>> = repository.allKatalog.asLiveData()

    fun insert(katalog: Katalog) = viewModelScope.launch {
        repository.insertKatalog(katalog)
    }

    fun delete(katalog: Katalog) = viewModelScope.launch {
        repository.deleteKatalog(katalog)
    }

    fun update(katalog: Katalog) = viewModelScope.launch {
        repository.updateKatalog(katalog)
    }
}

class CatalogViewModelFactory(private val repository: CatalogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
