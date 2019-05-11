package me.ricardo.romero.fersi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*
import me.ricardo.romero.fersi.R
import me.ricardo.romero.fersi.models.Product

class ProductAdapter(
    private var data: List<Product>
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    fun changeDataSet(lp: List<Product>){
        data = lp
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.view.tvName.text = data[position].name
        holder.view.tvPrice.text = data[position].price.toString()
    }

    class VH(val view: View) : RecyclerView.ViewHolder(view)
}