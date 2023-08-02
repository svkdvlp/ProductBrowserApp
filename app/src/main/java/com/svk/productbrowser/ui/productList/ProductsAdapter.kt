package com.svk.productbrowser.ui.productList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.svk.productbrowser.databinding.LayoutProductRowBinding
import com.svk.productbrowser.domain.ProductModel

/**
 * ProductsAdapter : Product list UI helper
 */
class ProductsAdapter(
    private val products: ArrayList<ProductModel>
):RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {
    private lateinit var binding: LayoutProductRowBinding
    var onItemClick: ((ProductModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = LayoutProductRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount():Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAndAddAll(productsToAdd: List<ProductModel>) {
        products.clear()
        notifyDataSetChanged()
        products.addAll(productsToAdd)
        notifyItemRangeInserted(0, productsToAdd.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        products.clear()
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: LayoutProductRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: ProductModel) {
            binding.product = productModel
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(products[adapterPosition])
            }
        }
    }
}