package com.diprojectin.sppprofilematching.ui.siswa.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diprojectin.models.Artikel
import com.diprojectin.sppprofilematching.R

class SliderAdapter(private val items: List<Artikel>) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)

        fun bind(item: Artikel) {
            textTitle.text = item.title

            Glide.with(itemView.context)
                .load(item.imagePath)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.img_not_available)
                .into(imageView)
        }
    }
}