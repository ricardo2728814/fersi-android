package me.ricardo.romero.fersi.services

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import me.ricardo.romero.fersi.models.Product
import java.lang.Error

class ProductService(
    val fireStoreService: FireStoreService,
    val fireStorageService: FireStorageService
) {
    private val TAG = "ProductService"
    private val PRODUCT_COLLECTION = "products"

    fun getProducts(): Single<List<Product>> {
        return Single.create { emitter ->
            fireStoreService.getDB()
                .collection(PRODUCT_COLLECTION)
                .get()
                .addOnSuccessListener {
                    val products = it.map {
                        val price = it.data["price"] as Number
                        Product(
                            it.id,
                            it.data["name"] as String,
                            it.data["images"] as ArrayList<String>,
                            price.toDouble(),
                            it.data["category"] as String
                        )
                    }
                    emitter.onSuccess(products)
                }
        }
    }

    fun getProductsWithImages(): Single<List<Product>> {
        return getProducts().flatMap { getImages(it) }
    }

    private fun getImages(products: List<Product>): Single<List<Product>> {
        val productsImagesZip = ArrayList<Single>
        products.forEachIndexed { index, product ->
            val requests = ArrayList<Single<String>>(product.images.size)
            product.images.forEach { imageName ->
                requests.add(getSingleImageURL(imageName))
            }
            Single.zip(requests) {
                it.forEach { s ->
                    Log.d("TEST", s as String)
                }
            }
        }
        return Single.just(products)
    }

    private fun getSingleImageURL(imageName: String): Single<String> {
        return Single.create { emitter ->
            fireStorageService
                .getStorage()
                .reference
                .child("products")
                .child(imageName)
                .downloadUrl
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(it.result!!.toString())
                    } else {
                        emitter.onError(Error())
                    }
                }
        }
    }

}