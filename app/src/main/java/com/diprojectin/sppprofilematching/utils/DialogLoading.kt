package com.diprojectin.sppprofilematching.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.diprojectin.sppprofilematching.R

class DialogLoading(val context: Context,
                    val message: String,
                    val setCancelable: Boolean) {

    fun build(): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(setCancelable)

        val txtMessage = dialog.findViewById<TextView>(R.id.tv_message)
        txtMessage.text = message

        return dialog
    }
}