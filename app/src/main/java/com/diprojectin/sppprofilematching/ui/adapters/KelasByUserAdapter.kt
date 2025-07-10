package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.KelasByUser
import com.diprojectin.sppprofilematching.R

class KelasByUserAdapter(private var context: Context, private var models: MutableList<KelasByUser>,
                         private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<KelasByUserAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: KelasByUser)
    }

    fun setList(datas: List<KelasByUser>){
        models.clear()
        models.addAll(datas)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvGrade: TextView = v.findViewById(R.id.tv_grade)
        var tvKelas: TextView = v.findViewById(R.id.tv_kelas)

        fun bind(item: KelasByUser, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_kelas_by_user, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]

        when(model.grade){
            "X" -> holder.tvGrade.text = "Kelas 1"
            "XI" -> holder.tvGrade.text = "Kelas 2"
            "XII" -> holder.tvGrade.text = "Kelas 3"
        }

        holder.tvKelas.text = "${model.grade} ${model.namaKelas} ${model.jurusan}"


        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}