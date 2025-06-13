package com.adrian.muscleforge.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import com.adrian.muscleforge.R

object DialogHelper {

    fun showDialogConfirm(
        context: Context,
        onResult: (Boolean) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnAccept: Button = dialogView.findViewById(R.id.btnAccept)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        btnAccept.setOnClickListener {
            onResult(true)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            onResult(false)
            dialog.dismiss()
        }
        dialog.show()
    }

}