package me.ricardo.romero.fersi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_product_interest.*
import me.ricardo.romero.fersi.R
import me.ricardo.romero.fersi.ui.fragments.base.Bindable
import me.ricardo.romero.fersi.ui.vm.ProductInterestFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class ProductInterestFragment : Fragment(), Bindable {

    override val vm: ProductInterestFragmentViewModel by viewModel()
    lateinit var productId: String
    val disposable = CompositeDisposable()

    override fun bindViewModel() {
        disposable.apply {
            add(
                vm.product.subscribe {
                    tvName.text = it.name
                    btSend.isEnabled = true
                }
            )

            add(
                vm.onMessageSent.subscribe {
                    goBack()
                }
            )
        }
        vm.fetchProduct(productId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_interest, container, false)
    }

    fun goBack() {
        sentToast()
        val sub = Observable.just(Unit)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribe {
                NavHostFragment.findNavController(this).popBackStack()
            }
        disposable.add(sub)
    }

    fun sentToast() {
        val text = "El mensaje ha sido enviado!"
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        productId = arguments!!["id"] as String
        bindViewModel()

        btSend.setOnClickListener {
            btSend.isEnabled = false
            vm.submitMessage(etMessage.text.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposable.dispose()
    }

}