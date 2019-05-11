package me.ricardo.romero.fersi.ui.vm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import me.ricardo.romero.fersi.models.Product
import me.ricardo.romero.fersi.services.ProductService

class ProductDetailFragmentViewModel(val productService: ProductService) : ViewModel() {
    private lateinit var productId: String
    val images = BehaviorSubject.create<List<String>>()
    val product = BehaviorSubject.create<Product>()
    val disposables = CompositeDisposable()

    fun fetchFor(productId: String) {
        this.productId = productId
        fetchImages()
    }

    private fun fetchImages() {
        disposables.apply {
            add(
                productService
                    .getProduct(productId)
                    .flatMap {
                        product.onNext(it)
                        productService.getImagesForProduct(it)
                    }
                    .subscribe { productImages ->
                        images.onNext(productImages)
                    }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}