package com.ifam.talkhub.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Representa um usuário no sistema de mensagens do TalkHub.
 *
 * @property id O identificador único do usuário.
 * @property username O nome de usuário do usuário.
 * @property email O endereço de e-mail do usuário.
 * @property perfilImgUrl A URL da imagem de perfil do usuário (pode ser nulo se não houver imagem).
 * @property perfilImgId O identificador único da imagem de perfil do usuário (pode ser nulo se não houver imagem).
 */
@Parcelize
data class User (
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var perfilImgUrl: String? = null,
    var perfilImgId: String? = null
) : Parcelable
