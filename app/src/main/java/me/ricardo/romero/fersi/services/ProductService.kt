package me.ricardo.romero.fersi.services

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import me.ricardo.romero.fersi.models.Message
import me.ricardo.romero.fersi.models.Product
import java.lang.Error

class ProductService(
    val fireStoreService: FireStoreService,
    val fireStorageService: FireStorageService
) {
    private val TAG = "ProductService"
    private val PRODUCT_COLLECTION = "products"

    fun getProduct(id: String): Single<Product> {
        return Single.create { emitter ->
            fireStoreService
                .getDB()
                .collection(PRODUCT_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener {
                    it.data?.let { data ->
                        val price = data["price"] as Number
                        emitter.onSuccess(
                            Product(
                                it.id,
                                data["name"] as String,
                                data["description"] as String,
                                data["images"] as ArrayList<String>,
                                price.toDouble(),
                                data["category"] as String
                            )
                        )
                    }

                }
        }
    }

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
                            it.data["description"] as String,
                            it.data["images"] as ArrayList<String>,
                            price.toDouble(),
                            it.data["category"] as String
                        )
                    }
                    emitter.onSuccess(products)
                }
        }
    }

    fun getProductsWithMainImage(): Single<List<Product>> {
        return getProducts().flatMap { fetchMainImages(it) }
    }

    private fun fetchMainImages(products: List<Product>): Single<List<Product>> {
        val singles = ArrayList<Single<String>>(products.size)
        products.forEach {
            singles.add(getSingleImageURL(it.images[0]))
        }
        return Single.zip<String, List<Product>>(singles) { imagesByProduct ->
            products.forEachIndexed { index, _ ->
                products[index].mainImage = imagesByProduct[index] as String
            }
            products
        }
    }

    fun getImagesForProduct(product: Product): Single<List<String>> {
        val singles = ArrayList<Single<String>>()
        product.images.forEach {
            singles.add(getSingleImageURL(it))
        }
        return Single.zip<Any, List<String>>(singles) {
            it.asList() as List<String>
        }
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
                        emitter.onSuccess("")
                    }
                }
        }
    }

    fun postMessage(product: Product, msg: String): Single<Boolean> {
        return Single.create { emitter ->
            fireStoreService
                .getDB()
                .collection("messages")
                .add(
                    Message(
                        msg,
                        fireStoreService
                            .getDB()
                            .collection(PRODUCT_COLLECTION)
                            .document(product.id)
                    )
                )
                .addOnCompleteListener {
                    emitter.onSuccess(it.isSuccessful)
                }
        }

    }

}