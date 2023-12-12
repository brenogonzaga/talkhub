package com.ifam.talkhub.ui.fragments.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ifam.talkhub.data.model.Mensagem

/**
 * Classe responsável por calcular as diferenças entre duas listas de mensagens para otimizar as
 * atualizações em um RecyclerView.
 *
 * @property oldList Lista antiga de mensagens.
 * @property newList Nova lista de mensagens.
 */
class MensagensDiffUtil(
    private val oldList: List<Mensagem?>,
    private val newList: List<Mensagem?>
) : DiffUtil.Callback() {

    /**
     * Retorna o tamanho da lista antiga.
     *
     * @return O tamanho da lista antiga.
     */
    override fun getOldListSize(): Int {
        return oldList.size
    }

    /**
     * Retorna o tamanho da nova lista.
     *
     * @return O tamanho da nova lista.
     */
    override fun getNewListSize(): Int {
        return newList.size
    }

    /**
     * Verifica se os itens nas posições correspondentes das listas antigas e novas são os mesmos.
     *
     * @param oldItemPosition Posição do item na lista antiga.
     * @param newItemPosition Posição do item na nova lista.
     * @return `true` se os itens são os mesmos, `false` caso contrário.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    /**
     * Verifica se o conteúdo dos itens nas posições correspondentes das listas antigas e novas são iguais.
     *
     * @param oldItemPosition Posição do item na lista antiga.
     * @param newItemPosition Posição do item na nova lista.
     * @return `true` se os conteúdos são iguais, `false` caso contrário.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMessage = oldList[oldItemPosition]
        val newMessage = newList[newItemPosition]

        return oldMessage?.mensagem == newMessage?.mensagem
                && oldMessage?.emissorId == newMessage?.emissorId
                && oldMessage?.mensagemTimeDate == newMessage?.mensagemTimeDate
    }
}
