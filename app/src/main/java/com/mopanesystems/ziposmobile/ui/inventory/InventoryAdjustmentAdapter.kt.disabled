package com.mopanesystems.ziposmobile.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.R
import com.mopanesystems.ziposmobile.data.model.InventoryAdjustment
import com.mopanesystems.ziposmobile.data.model.AdjustmentReason
import java.time.format.DateTimeFormatter

class InventoryAdjustmentAdapter : RecyclerView.Adapter<InventoryAdjustmentAdapter.AdjustmentViewHolder>() {

    private var adjustments = listOf<InventoryAdjustment>()

    fun submitList(newAdjustments: List<InventoryAdjustment>) {
        adjustments = newAdjustments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdjustmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory_adjustment, parent, false)
        return AdjustmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdjustmentViewHolder, position: Int) {
        holder.bind(adjustments[position])
    }

    override fun getItemCount(): Int = adjustments.size

    inner class AdjustmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.tvProductName)
        private val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val reason: TextView = itemView.findViewById(R.id.tvReason)
        private val adjustedBy: TextView = itemView.findViewById(R.id.tvAdjustedBy)
        private val date: TextView = itemView.findViewById(R.id.tvDate)
        private val notes: TextView = itemView.findViewById(R.id.tvNotes)

        fun bind(adjustment: InventoryAdjustment) {
            productName.text = "Product ID: ${adjustment.productId}" // TODO: Get actual product name
            reason.text = adjustment.reason.name.replace("_", " ")
            adjustedBy.text = "Adjusted by: User ${adjustment.adjustedBy}"
            date.text = adjustment.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            
            // Format quantity with + or - sign
            val quantityText = if (adjustment.quantity > 0) "+${adjustment.quantity}" else adjustment.quantity.toString()
            quantity.text = quantityText
            
            // Set quantity color based on positive or negative
            quantity.setTextColor(
                if (adjustment.quantity > 0) {
                    itemView.context.getColor(android.R.color.holo_green_dark)
                } else {
                    itemView.context.getColor(android.R.color.holo_red_dark)
                }
            )

            // Show notes if available
            adjustment.notes?.let {
                notes.text = "Notes: $it"
                notes.visibility = View.VISIBLE
            } ?: run {
                notes.visibility = View.GONE
            }
        }
    }
}
