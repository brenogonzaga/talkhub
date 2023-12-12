package com.ifam.talkhub.data.model

/**
 * Representa uma mensagem no sistema de mensagens do TalkHub.
 *
 * @property mensagem O conteúdo da mensagem.
 * @property emissorId O identificador único do emissor da mensagem.
 * @property receptorId O identificador único do receptor da mensagem.
 * @property mensagemTimeDate A data e hora em que a mensagem foi enviada, no formato de string.
 */
data class Mensagem (
    var mensagem: String = "",
    var emissorId: String = "",
    var receptorId: String = "",
    var mensagemTimeDate: String = ""
)
