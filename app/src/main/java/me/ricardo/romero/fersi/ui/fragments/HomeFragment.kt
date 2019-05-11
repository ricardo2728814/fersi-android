package me.ricardo.romero.fersi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import me.ricardo.romero.fersi.R
import me.ricardo.romero.fersi.models.Product
import me.ricardo.romero.fersi.ui.adapters.ProductAdapter
import me.ricardo.romero.fersi.ui.fragments.base.Bindable
import me.ricardo.romero.fersi.ui.vm.HomeFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), Bindable {
    override val vm: HomeFragmentViewModel by viewModel()
    lateinit var productAdapter: ProductAdapter
    val disposables = CompositeDisposable()

    fun goToDetail() {
        NavHostFragment.findNavController(this).navigate(R.id.action_goToProductDetail)
    }

    override fun bindViewModel() {
        disposables.apply {
            add(
                vm.products
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        productAdapter.changeDataSet(it)
                    }
            )
        }

    }

    override fun onStop() {
        super.onStop()
        disposables.dispose()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rcvTable.layoutManager = LinearLayoutManager(context)
        productAdapter = ProductAdapter(
            emptyList()
        )
        rcvTable.adapter = productAdapter

        bindViewModel()
    }
}