package com.mopanesystems.ziposmobile.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mopanesystems.ziposmobile.R
import com.mopanesystems.ziposmobile.data.model.StockAlert
import com.mopanesystems.ziposmobile.data.model.StockAlertType
import java.time.format.DateTimeFormatter

class StockAlertAdapter(
    private val onAcknowledgeClick: (StockAlert) -> Unit
) : RecyclerView.Adapter<StockAlertAdapter.StockAlertViewHolder>() {

    private var alerts = listOf<StockAlert>()

    fun submitList(newAlerts: List<StockAlert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_alert, parent, false)
        return StockAlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockAlertViewHolder, position: Int) {
        holder.bind(alerts[position])
    }

    override fun getItemCount(): Int = alerts.size

    inner class StockAlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.tvProductName)
        private val alertType: TextView = itemView.findViewById(R.id.tvAlertType)
        private val stockInfo: TextView = itemView.findViewById(R.id.tvStockInfo)
        private val alertIcon: ImageView = itemView.findViewById(R.id.ivAlertIcon)
        private val acknowledgeButton: com.google.android.material.button.MaterialButton = 
            itemView.findViewById(R.id.btnAcknowledge)

        fun bind(alert: StockAlert) {
            productName.text = "Product ID: ${alert.productId}" // TODO: Get actual product name
            alertType.text = alert.alertType.name.replace("_", " ")
            stockInfo.text = "Current: ${alert.currentStock} | Min: ${alert.minThreshold}"
            
            // Set appropriate icon and color based on alert type
            when (alert.alertType) {
                StockAlertType.LOW_STOCK -> {
                    alertIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                    alertType.setTextColor(itemView.context.getColor(android.R.color.holo_orange_dark))
                }
                StockAlertType.OUT_OF_STOCK -> {
                    alertIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                    alertType.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                }
                StockAlertType.OVERSTOCK -> {
                    alertIcon.setImageResource(android.R.drawable.ic_dialog_info)
                    alertType.setTextColor(itemView.context.getColor(android.R.color.holo_blue_dark))
                }
                StockAlertType.EXPIRING_SOON -> {
                    alertIcon.setImageResource(android.R.drawable.ic_menu_recent_history)
                    alertType.setTextColor(itemView.context.getColor(android.R.color.holo_purple))
                }
            }

            acknowledgeButton.setOnClickListener {
                onAcknowledgeClick(alert)
            }
        }
    }
}
