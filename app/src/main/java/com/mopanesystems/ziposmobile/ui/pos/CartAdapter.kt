package com.mopanesystems.ziposmobile.ui.pos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.databinding.ItemCartBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class CartAdapter(
    private val onRemoveClick: (String) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        fun bind(item: CartItem) {
            binding.apply {
                textViewProductName.text = item.product.name
                textViewQuantity.text = "Qty: ${item.quantity}"
                textViewUnitPrice.text = currencyFormatter.format(item.unitPrice)
                textViewTotalPrice.text = currencyFormatter.format(item.totalPrice)
                
                buttonRemove.setOnClickListener {
                    onRemoveClick(item.product.id)
                }
                
                buttonIncrease.setOnClickListener {
                    // TODO: Implement quantity increase
                }
                
                buttonDecrease.setOnClickListener {
                    // TODO: Implement quantity decrease
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
