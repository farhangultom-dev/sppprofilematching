package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diprojectin.models.Siswa
import com.diprojectin.sppprofilematching.R

class SiswaAdapter(private var context: Context, private var models: MutableList<Siswa>,
                   private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<SiswaAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Siswa)
    }

    fun setList(datas: List<Siswa>){
        models.clear()
        models.addAll(datas)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNisn: TextView = v.findViewById(R.id.tv_nisn)
        var tvNama: TextView = v.findViewById(R.id.tv_nama)
        var ivPhotoSiswa: ImageView = v.findViewById(R.id.iv_photo_siswa)

        fun bind(item: Siswa, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_siswa, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.tvNisn.text = model.nisn
        holder.tvNama.text = model.nama

        Glide.with(context)
            .load(model.photoProfile)
            .into(holder.ivPhotoSiswa)

        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}