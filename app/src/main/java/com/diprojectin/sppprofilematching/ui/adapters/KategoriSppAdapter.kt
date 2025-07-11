package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Spp
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency

class KategoriSppAdapter(private var context: Context, private var models: MutableList<Spp>,
                         private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<KategoriSppAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Spp)
    }

    fun setList(datas: List<Spp>){
        models.clear()
        models.addAll(datas)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNomor: TextView = v.findViewById(R.id.tv_nomor)
        var tvKategori: TextView = v.findViewById(R.id.tv_kelas)
        var tvNominal: TextView = v.findViewById(R.id.tv_nama_kelas)

        fun bind(item: Spp, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_spp, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.tvNomor.text = (position + 1).toString()
        holder.tvKategori.text = model.nama
        holder.tvNominal.text = model.nilaiSpp?.toInt().formatCurrency()

        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}