package me.ricardo.romero.fersi.ui.vm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import me.ricardo.romero.fersi.models.Product
import me.ricardo.romero.fersi.services.ProductService

class ProductInterestFragmentViewModel(val productService: ProductService) : ViewModel() {
    val product = BehaviorSubject.create<Product>()
    val disposable = CompositeDisposable()
    val onMessageSent = PublishSubject.create<Unit>()

    fun fetchProduct(id: String) {
        disposable.apply {
            add(
                productService
                    .getProduct(id)
                    .subscribe { p ->
                        product.onNext(p)
                    }
            )
        }
    }

    fun submitMessage( msg: String ){
        val sub = productService.postMessage(
            product.value!!,
            msg
        ).subscribe { success ->
            onMessageSent.onNext(Unit)
        }
        disposable.add(sub)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}