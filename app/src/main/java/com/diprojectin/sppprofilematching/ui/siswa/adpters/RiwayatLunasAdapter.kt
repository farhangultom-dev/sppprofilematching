package com.diprojectin.sppprofilematching.ui.siswa.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Lunas
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.utils.Helper.convertToIndonesianDate
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency

class RiwayatLunasAdapter(private var context: Context, private var models: MutableList<Lunas>,
                          private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RiwayatLunasAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Lunas)
    }

    fun setList(datas: List<Lunas>){
        models.clear()
        models.addAll(datas)

        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var kategori: TextView = v.findViewById(R.id.lbl_kategori)
        var totalBayar: TextView = v.findViewById(R.id.tv_total_bayar)
        var tanggalTransaksi: TextView = v.findViewById(R.id.tv_tanggal_transaksi)
        var status: TextView = v.findViewById(R.id.tv_status_pembayaran)

        fun bind(item: Lunas, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.riwayat_spp_lunas, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.kategori.text = model.namaKategori
        holder.totalBayar.text = model.totalPayment?.toInt().formatCurrency()
        holder.tanggalTransaksi.text = model.createdDate.convertToIndonesianDate()

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