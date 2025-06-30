package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Kelas
import com.diprojectin.sppprofilematching.R

class DetailKelasAdapter(private var context: Context, private var models: MutableList<Kelas>,
                         private val onItemClickListener: OnItemClickListener,
                         private val onTextChangedListener: OnTextChangedListener,private val isEdit: Int)
    : RecyclerView.Adapter<DetailKelasAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Kelas, adapterPosition: Int)
    }

    interface OnTextChangedListener {
        fun onTextChanged(text: String,item: Kelas)
    }

    fun setList(datas: List<Kelas>){
        models.clear()
        models.addAll(datas)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvGrade: TextView = v.findViewById(R.id.tv_grade)
        var tvKelas: TextView = v.findViewById(R.id.tv_nama_kelas)
        var btnDelete: ImageButton = v.findViewById(R.id.btn_delete)
        var etKelas: EditText = v.findViewById(R.id.et_nama_kelas)

        fun bind(item: Kelas, onItemClickListener: OnItemClickListener, OnTextChangedListener: OnTextChangedListener) {
            btnDelete.setOnClickListener {
                onItemClickListener.onItemClick(item,adapterPosition)
            }

            etKelas.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    OnTextChangedListener.onTextChanged(s.toString(),item)
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kelas_detail, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.tvGrade.text = model.grade
        holder.tvKelas.text = model.nama

        if(isEdit == 1){
            holder.etKelas.visibility = View.VISIBLE
            holder.tvKelas.visibility = View.GONE
            holder.btnDelete.visibility = View.GONE
        }

        holder.bind(models[position], onItemClickListener,onTextChangedListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}