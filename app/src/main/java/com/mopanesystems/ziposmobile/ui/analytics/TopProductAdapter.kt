package com.mopanesystems.ziposmobile.ui.analytics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.R
import com.mopanesystems.ziposmobile.data.model.ProductPerformance
import java.text.NumberFormat
import java.util.*

class TopProductAdapter : RecyclerView.Adapter<TopProductAdapter.TopProductViewHolder>() {

    private var products = listOf<ProductPerformance>()

    fun submitList(newProducts: List<ProductPerformance>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_product, parent, false)
        return TopProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {
        holder.bind(products[position], position + 1)
    }

    override fun getItemCount(): Int = products.size

    inner class TopProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rank: TextView = itemView.findViewById(R.id.tvRank)
        private val productName: TextView = itemView.findViewById(R.id.tvProductName)
        private val productSku: TextView = itemView.findViewById(R.id.tvProductSku)
        private val quantitySold: TextView = itemView.findViewById(R.id.tvQuantitySold)
        private val revenue: TextView = itemView.findViewById(R.id.tvRevenue)

        fun bind(product: ProductPerformance, rankPosition: Int) {
            rank.text = rankPosition.toString()
            productName.text = "Product ID: ${product.productId}" // TODO: Get actual product name
            productSku.text = "SKU: ${product.productId}" // TODO: Get actual SKU
            quantitySold.text = "${product.quantitySold} sold"
            revenue.text = formatCurrency(product.revenue)

            // Set rank background color based on position
            when (rankPosition) {
                1 -> rank.setBackgroundColor(itemView.context.getColor(android.R.color.holo_orange_dark))
                2 -> rank.setBackgroundColor(itemView.context.getColor(android.R.color.darker_gray))
                3 -> rank.setBackgroundColor(itemView.context.getColor(android.R.color.holo_blue_dark))
                else -> rank.setBackgroundColor(itemView.context.getColor(android.R.color.holo_blue_dark))
            }
        }

        private fun formatCurrency(amount: java.math.BigDecimal): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            return formatter.format(amount)
        }
    }
}
