package com.diprojectin.sppprofilematching.ui.siswa.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diprojectin.models.Angsuran
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.utils.Helper.convertToIndonesianDate
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency

class RiwayatAngsuranAdapter(private var context: Context, private var models: MutableList<Angsuran>,private var isPembayranKe: Boolean,
                             private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RiwayatAngsuranAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Angsuran)
    }

    fun setList(datas: List<Angsuran>){
        models.clear()
        models.addAll(datas)

        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var kategori: TextView = v.findViewById(R.id.lbl_kategori)
        var totalBayar: TextView = v.findViewById(R.id.tv_total_bayar)
        var tanggalTransaksi: TextView = v.findViewById(R.id.tv_tanggal_transaksi)
        var pembayaranKe: TextView = v.findViewById(R.id.tv_pembayaran_ke)
        var trPembayaranKe: View = v.findViewById(R.id.tr_pembayaran_ke)

        fun bind(item: Angsuran, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.riwayat_spp, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = models[position]
        holder.kategori.text = model.namaKategori
        holder.totalBayar.text = model.totalPayment?.toInt().formatCurrency()
        holder.tanggalTransaksi.text = model.createdDate.convertToIndonesianDate()

        if (isPembayranKe){
            holder.trPembayaranKe.visibility = View.VISIBLE
            holder.pembayaranKe.text = model.pembayaranKe.toString()
        }else{
            holder.trPembayaranKe.visibility = View.GONE
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