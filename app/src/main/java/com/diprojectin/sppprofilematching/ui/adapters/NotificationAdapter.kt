package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Notification
import com.diprojectin.sppprofilematching.R

class NotificationAdapter(private var context: Context, private var models: MutableList<Notification>,
                          private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Notification)
    }

    fun setList(datas: List<Notification>){
        models.clear()
        models.addAll(datas)

        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvJudul: TextView = v.findViewById(R.id.tv_judul)
        var tvDeskripsi: TextView = v.findViewById(R.id.tv_deskripsi)
        var tvTanggal: TextView = v.findViewById(R.id.tv_date)

        fun bind(item: Notification, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notification, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.tvJudul.text = model.title
        holder.tvDeskripsi.text = model.deskripsi
        holder.tvTanggal.text = "Dibuat: "+model.createdDate?.take(10)

        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}