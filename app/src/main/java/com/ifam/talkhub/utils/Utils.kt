package com.ifam.talkhub.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Função utilitária que retorna a data e hora atuais formatadas como uma string.
 *
 * @return String contendo a data e hora atuais no formato "EEE d, MMM yyyy HH:mm:ss".
 */
fun getCurrentDate(): String {
    // Obtém uma instância do calendário representando a data e hora atuais.
    val calendar = Calendar.getInstance()
    // Define o formato desejado para a representação da data e hora.
    val myFormat = SimpleDateFormat("EEE d, MMM yyyy HH:mm:ss " )
    // Retorna a data e hora formatadas como uma string.
    return myFormat.format(calendar.time)
}