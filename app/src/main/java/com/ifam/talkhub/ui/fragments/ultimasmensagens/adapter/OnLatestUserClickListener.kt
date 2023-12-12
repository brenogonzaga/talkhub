package com.ifam.talkhub.ui.fragments.ultimasmensagens.adapter

import com.ifam.talkhub.data.model.UserUltimaMensagem

/**
 * Interface que define um ouvinte para cliques nos itens da lista de usuários e últimas mensagens.
 */
interface OnLatestUserClickListener {
    /**
     * Chamado quando um usuário é clicado na lista de usuários e últimas mensagens.
     *
     * @param userUltimaMensagem Objeto representando o usuário e sua última mensagem.
     */
    fun onLatestUserClickListener(userUltimaMensagem: UserUltimaMensagem)
}
