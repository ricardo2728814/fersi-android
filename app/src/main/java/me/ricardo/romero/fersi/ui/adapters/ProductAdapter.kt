package me.ricardo.romero.fersi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_product.view.*
import me.ricardo.romero.fersi.R
import me.ricardo.romero.fersi.models.Product

class ProductAdapter(
    private var data: List<Product>
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    val itemClicks = PublishSubject.create<Product>()

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
        holder.view.tvPrice.text = "$"+data[position].price.toString()
        data[position].mainImage?.let {
            Picasso.get().load(it).into( holder.view.ivImage )
        }
        holder.view.setOnClickListener {
            itemClicks.onNext( data[position] )
        }
    }

    class VH(val view: View) : RecyclerView.ViewHolder(view)
}