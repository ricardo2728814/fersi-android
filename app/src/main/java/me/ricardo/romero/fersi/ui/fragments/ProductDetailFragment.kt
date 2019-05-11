package me.ricardo.romero.fersi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_product_detail.*
import me.ricardo.romero.fersi.R
import me.ricardo.romero.fersi.ui.fragments.base.Bindable
import me.ricardo.romero.fersi.ui.vm.ProductDetailFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProductDetailFragment : Fragment(), Bindable {

    override val vm: ProductDetailFragmentViewModel by viewModel()
    lateinit var productId: String
    val disposable = CompositeDisposable()
    var rcImages: List<RequestCreator>? = null
    var currentImageIndex = 0

    override fun bindViewModel() {
        disposable.apply {
            add(
                vm.product
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        tvName.text = it.name
                        tvDescription.text = it.description
                        tvCost.text = "$${it.price}"
                        btContact.isEnabled = true
                    }
            )

            add(
                vm.images
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        rcImages = it.map { url -> Picasso.get().load(url).placeholder(R.color.primary_material_dark) }
                        rcImages!!.getOrNull(0)?.let {
                            it.into(ivImage)
                        }
                    }
            )
        }

        vm.fetchFor(productId)
    }

    fun goToInterest(id: String){
        NavHostFragment.findNavController(this).navigate(R.id.action_goToInterest, bundleOf(
            Pair("id", id)
        ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btContact.setOnClickListener {
            goToInterest(productId)
        }
        productId = arguments!!["id"] as String
        bindViewModel()
        ivImage.setOnClickListener {
            rcImages?.let {
                currentImageIndex++
                if(currentImageIndex >= it.size) currentImageIndex = 0
                it.getOrNull(currentImageIndex)?.let {
                    it.into(ivImage)
                }
            }
        }

        val text = "Toque la imagen para ver más"
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, text, duration)
        toast.show()


    }
}