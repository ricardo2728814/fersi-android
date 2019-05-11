package me.ricardo.romero.fersi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_product_detail.*
import me.ricardo.romero.fersi.R

class ProductDetailFragment : Fragment() {

    fun goToInterest(){
        NavHostFragment.findNavController(this).navigate(R.id.action_goToInterest)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btContact.setOnClickListener {
            goToInterest()
        }
    }
}