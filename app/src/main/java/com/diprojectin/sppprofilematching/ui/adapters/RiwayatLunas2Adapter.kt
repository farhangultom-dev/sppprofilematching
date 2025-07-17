package com.diprojectin.sppprofilematching.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Angsuran
import com.diprojectin.models.Lunas
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.utils.Helper.convertToIndonesianDate
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency

class RiwayatLunas2Adapter(private var context: Context, private var models: MutableList<Lunas>,
                           private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RiwayatLunas2Adapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Lunas)
    }

    fun setList(datas: List<Lunas>){
        models.clear()
        models.addAll(datas)

        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var namaSiswa: TextView = v.findViewById(R.id.tv_nama_siswa)
        var nisnKelas: TextView = v.findViewById(R.id.tv_nisn_kelas)
        var jenisSpp: TextView = v.findViewById(R.id.tv_jenis_spp)
        var nominal: TextView = v.findViewById(R.id.tv_nominal)
        var status: TextView = v.findViewById(R.id.tv_status)

        fun bind(item: Lunas, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_spp_admin_lunas, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.namaSiswa.text = model.namaSiswa
        holder.nisnKelas.text = "${model.nisn} • ${model.grade} ${model.jurusan} ${model.grade}"
        holder.jenisSpp.text = model.namaKategori
        holder.nominal.text = model.totalPayment?.toInt().formatCurrency()
        holder.status.text = when(model.statusPayment){
            "PENDING" -> "Menunggu Pembayaran"
            "SUCCESS" -> "Sudah Dibayar"
            "CANCELLED" -> "Dibatalkan"
            else -> "Menunggu Pembayaran"
        }

        when(model.statusPayment){
            "SUCCESS" -> holder.status.setTextColor(Color.parseColor("#38D79F"))
            "CANCELLED" -> holder.status.setTextColor(Color.RED)
            else -> "Menunggu Pembayaran"
        }

        holder.bind(models[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}