package com.diprojectin.sppprofilematching.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.diprojectin.sppprofilematching.R

class DialogUtils(val context: Context) {

    fun build(): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)

        return dialog
    }
}