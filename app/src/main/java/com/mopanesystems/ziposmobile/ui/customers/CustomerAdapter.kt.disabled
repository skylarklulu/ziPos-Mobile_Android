package com.mopanesystems.ziposmobile.ui.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.data.model.Customer
import com.mopanesystems.ziposmobile.databinding.ItemCustomerBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class CustomerAdapter(
    private val onItemClick: (Customer) -> Unit
) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        fun bind(customer: Customer) {
            binding.apply {
                textViewCustomerName.text = customer.fullName
                textViewCustomerPhone.text = customer.phone ?: "No phone"
                textViewCustomerEmail.text = customer.email ?: "No email"
                textViewCustomerBalance.text = currencyFormatter.format(customer.balance)
                textViewCustomerLoyalty.text = "Points: ${customer.loyaltyPoints}"
                
                // Set risk indicator
                when {
                    customer.riskScore >= 80 -> {
                        textViewRiskIndicator.text = "HIGH RISK"
                        textViewRiskIndicator.setTextColor(android.graphics.Color.RED)
                    }
                    customer.riskScore >= 50 -> {
                        textViewRiskIndicator.text = "MEDIUM RISK"
                        textViewRiskIndicator.setTextColor(android.graphics.Color.parseColor("#FFA500")) // Orange
                    }
                    else -> {
                        textViewRiskIndicator.text = "LOW RISK"
                        textViewRiskIndicator.setTextColor(android.graphics.Color.GREEN)
                    }
                }
                
                root.setOnClickListener {
                    onItemClick(customer)
                }
            }
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}
