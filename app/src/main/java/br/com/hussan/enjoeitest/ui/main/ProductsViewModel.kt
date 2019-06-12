package br.com.hussan.enjoeitest.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.hussan.enjoeitest.data.response.ProductsPagination
import br.com.hussan.enjoeitest.usecases.GetProducts

class ProductsViewModel(
    private val getProducts: GetProducts
) : ViewModel() {

    private val results = MutableLiveData<ProductsPagination>()

    fun getProducts(page: Int) = getProducts.invoke(page)
        .doOnNext {
            Log.d("h22", it.toString())
            results.postValue(it)
        }

    fun getResultProducts() = results
}

