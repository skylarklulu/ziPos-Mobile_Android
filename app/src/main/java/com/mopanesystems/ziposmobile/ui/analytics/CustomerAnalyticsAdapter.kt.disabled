package com.mopanesystems.ziposmobile.ui.analytics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.R
import com.mopanesystems.ziposmobile.data.model.CustomerAnalytics
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

class CustomerAnalyticsAdapter : RecyclerView.Adapter<CustomerAnalyticsAdapter.CustomerAnalyticsViewHolder>() {

    private var customers = listOf<CustomerAnalytics>()

    fun submitList(newCustomers: List<CustomerAnalytics>) {
        customers = newCustomers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAnalyticsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_analytics, parent, false)
        return CustomerAnalyticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerAnalyticsViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount(): Int = customers.size

    inner class CustomerAnalyticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val customerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val loyaltyTier: TextView = itemView.findViewById(R.id.tvLoyaltyTier)
        private val totalSpent: TextView = itemView.findViewById(R.id.tvTotalSpent)
        private val transactionCount: TextView = itemView.findViewById(R.id.tvTransactionCount)
        private val avgTransaction: TextView = itemView.findViewById(R.id.tvAvgTransaction)
        private val lastPurchase: TextView = itemView.findViewById(R.id.tvLastPurchase)

        fun bind(customer: CustomerAnalytics) {
            customerName.text = "Customer ID: ${customer.customerId}" // TODO: Get actual customer name
            totalSpent.text = formatCurrency(customer.totalSpent)
            transactionCount.text = customer.totalTransactions.toString()
            avgTransaction.text = formatCurrency(customer.averageTransactionValue)

            // Set loyalty tier
            customer.loyaltyTier?.let { tier ->
                loyaltyTier.text = tier
                loyaltyTier.visibility = View.VISIBLE
                
                // Set tier background color
                when (tier.lowercase()) {
                    "gold" -> loyaltyTier.setBackgroundColor(itemView.context.getColor(android.R.color.holo_orange_dark))
                    "silver" -> loyaltyTier.setBackgroundColor(itemView.context.getColor(android.R.color.darker_gray))
                    "platinum" -> loyaltyTier.setBackgroundColor(itemView.context.getColor(android.R.color.holo_purple))
                    else -> loyaltyTier.setBackgroundColor(itemView.context.getColor(android.R.color.holo_blue_dark))
                }
            } ?: run {
                loyaltyTier.visibility = View.GONE
            }

            // Set last purchase date
            customer.lastPurchaseDate?.let { lastPurchaseDate ->
                val daysAgo = java.time.temporal.ChronoUnit.DAYS.between(lastPurchaseDate, java.time.LocalDateTime.now())
                lastPurchase.text = when {
                    daysAgo == 0L -> "Last purchase: Today"
                    daysAgo == 1L -> "Last purchase: Yesterday"
                    daysAgo < 7L -> "Last purchase: $daysAgo days ago"
                    daysAgo < 30L -> "Last purchase: ${daysAgo / 7} weeks ago"
                    else -> "Last purchase: ${daysAgo / 30} months ago"
                }
            } ?: run {
                lastPurchase.text = "No recent purchases"
            }
        }

        private fun formatCurrency(amount: java.math.BigDecimal): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            return formatter.format(amount)
        }
    }
}
