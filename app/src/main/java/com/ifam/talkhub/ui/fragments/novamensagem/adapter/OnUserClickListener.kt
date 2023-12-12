package com.ifam.talkhub.ui.fragments.novamensagem.adapter

import com.ifam.talkhub.data.model.User

/**
 * Interface para lidar com cliques em usuários na lista de novas mensagens.
 */
interface OnUserClickListener {

    /**
     * Método chamado quando um usuário na lista é clicado.
     *
     * @param user O usuário que foi clicado.
     */
    fun onUserClickListener(user: User)
}
