package com.ifam.talkhub.ui.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.ifam.talkhub.databinding.BarraDeCarregamentoBinding

/**
 * Classe que define um AlertDialog para exibir uma barra de carregamento durante operações assíncronas.
 *
 * @property context O contexto no qual o AlertDialog será exibido.
 */
class LoadingAlertDialog(private val context: Context) {

    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var alert: AlertDialog

    /**
     * Método para exibir o AlertDialog de barra de carregamento.
     */
    fun showLoadingAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
        val bind: BarraDeCarregamentoBinding = BarraDeCarregamentoBinding.inflate(
            LayoutInflater.from(context)
        )
        alertDialog.setView(bind.root)
        alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.show()
    }

    /**
     * Método para ocultar o AlertDialog de barra de carregamento.
     */
    fun hideLoadingAlertDialog() {
        alert.dismiss()
    }
}
