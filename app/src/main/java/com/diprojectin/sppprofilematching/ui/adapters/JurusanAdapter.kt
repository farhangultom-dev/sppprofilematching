package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Jurusan
import com.diprojectin.sppprofilematching.R

class JurusanAdapter(private var context: Context, private var models: MutableList<Jurusan>,
                     private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<JurusanAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Jurusan)
    }

    fun setList(datas: List<Jurusan>){
        models.clear()
        models.addAll(datas)

        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNomor: TextView = v.findViewById(R.id.tv_nomor)
        var tvJurusan: TextView = v.findViewById(R.id.tv_jurusan)

        fun bind(item: Jurusan, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_jurusan, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.tvNomor.text = (position + 1).toString()
        holder.tvJurusan.text = model.nama

        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}