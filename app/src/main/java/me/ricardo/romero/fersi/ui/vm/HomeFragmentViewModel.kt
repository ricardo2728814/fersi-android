package me.ricardo.romero.fersi.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import me.ricardo.romero.fersi.models.Product
import me.ricardo.romero.fersi.services.ProductService

class HomeFragmentViewModel(
    val productService: ProductService
) : ViewModel() {
    val products = BehaviorSubject.create<List<Product>>()
    val disposables = CompositeDisposable()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        disposables.apply {
            add(
                productService.getProductsWithImages().subscribe { ps ->
                    products.onNext(ps)
                    Log.d("TEST", ps.size.toString())
                }
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}