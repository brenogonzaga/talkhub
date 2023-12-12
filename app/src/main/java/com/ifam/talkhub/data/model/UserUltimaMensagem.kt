package com.ifam.talkhub.data.model

/**
 * Representa uma combinação de usuário e sua última mensagem no sistema de mensagens do TalkHub.
 *
 * @property mensagem A última mensagem enviada pelo usuário.
 * @property user O usuário associado à última mensagem.
 */
data class UserUltimaMensagem (
    var mensagem: Mensagem = Mensagem("", "", "", ""),
    var user: User = User("", "", "", null, null)
)
