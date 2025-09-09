package com.mopanesystems.ziposmobile.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.data.model.Product
import com.mopanesystems.ziposmobile.databinding.ItemProductBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ProductAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.name
                textViewSku.text = "SKU: ${product.sku}"
                textViewPrice.text = currencyFormatter.format(product.price)
                textViewStock.text = "Stock: ${product.stockQuantity}"
                
                // Set stock color based on quantity
                when {
                    product.stockQuantity <= 0 -> {
                        textViewStock.setTextColor(android.graphics.Color.RED)
                    }
                    product.stockQuantity <= product.minStockLevel -> {
                        textViewStock.setTextColor(android.graphics.Color.parseColor("#FFA500")) // Orange
                    }
                    else -> {
                        textViewStock.setTextColor(android.graphics.Color.GREEN)
                    }
                }
                
                root.setOnClickListener {
                    onItemClick(product)
                }
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
