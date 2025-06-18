package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Kelas
import com.diprojectin.sppprofilematching.R

class KelasAdapter(private var context: Context, private var models: MutableList<Kelas>,
                   private var modelsDisticted: MutableList<Kelas>,
                   private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<KelasAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Kelas)
    }

    fun setList(datas: List<Kelas>){
        models.clear()
        models.addAll(datas)

        modelsDisticted.clear()
        modelsDisticted.addAll(datas.distinctBy { it.grade })
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNomor: TextView = v.findViewById(R.id.tv_nomor)
        var tvKelas: TextView = v.findViewById(R.id.tv_kelas)
        var tvNamaKelas: TextView = v.findViewById(R.id.tv_nama_kelas)

        fun bind(item: Kelas, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_kelas, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelsDisticted[position]
        holder.tvNomor.text = (position + 1).toString()
        holder.tvKelas.text = model.grade

        var namaKelasValue = ""
        val namaKelas = models.filter { it.grade == model.grade }

        for (i in namaKelas) {
            if (i.id == namaKelas.last().id){
                namaKelasValue += "${i.nama}"
            }else{
                namaKelasValue += "${i.nama},"
            }
        }

        holder.tvNamaKelas.text = namaKelasValue

        holder.bind(modelsDisticted[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return modelsDisticted.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}