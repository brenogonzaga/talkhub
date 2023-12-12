package com.ifam.talkhub.ui.fragments.ultimasmensagens.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.data.model.UserUltimaMensagem
import com.ifam.talkhub.databinding.UltimasConversasBinding
import javax.inject.Inject

/**
 * Adaptador para exibir a lista de usuários e suas últimas mensagens em uma RecyclerView.
 *
 * @property context Uma instância da classe `Application`.
 */
class LatestUserAndMessagesAdapter @Inject constructor(
    private val context: Application
) : ListAdapter<UserUltimaMensagem, LatestUserAndMessagesAdapter.ViewHolder>(
    LatestUserAndMessagesDiffUtil()
) {
    // Usuário atualmente autenticado no Firebase.
    private val currentUser = Firebase.auth.currentUser

    // Ouvinte para cliques nos itens da lista.
    var onUserClickListener: OnLatestUserClickListener? = null

    /**
     * Define os dados da lista de usuários e suas últimas mensagens.
     *
     * @param latestUsersList Lista de usuários e últimas mensagens.
     */
    fun setData(latestUsersList: ArrayList<UserUltimaMensagem?>) {
        this.submitList(latestUsersList)
    }

    /**
     * ViewHolder para cada item na RecyclerView.
     *
     * @param binding Uma instância da classe `UltimasConversasBinding` gerada automaticamente pelo ViewBinding.
     */
    inner class ViewHolder(private val binding: UltimasConversasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Liga os dados do usuário e sua última mensagem à View.
         *
         * @param userUltimaMensagem Objeto representando o usuário e sua última mensagem.
         */
        fun bind(userUltimaMensagem: UserUltimaMensagem) {
            // Carrega a imagem do perfil do usuário, se disponível.
            if (userUltimaMensagem.user.perfilImgUrl != null) {
                binding.imagemUsuarioCirimgview.load(userUltimaMensagem.user.perfilImgUrl)
            }

            // Define o texto da última mensagem, indicando se é do usuário atual.
            if (userUltimaMensagem.mensagem.emissorId == currentUser?.uid) {
                val message = "Você: " + userUltimaMensagem.mensagem.mensagem
                binding.ultimaMensagemTextview.text = message
            } else {
                binding.ultimaMensagemTextview.text = userUltimaMensagem.mensagem.mensagem
            }

            // Define o nome do usuário.
            binding.nomeUsuarioTextview.text = userUltimaMensagem.user.username

            // Formata e define a data da última mensagem.
            val date = userUltimaMensagem.mensagem.mensagemTimeDate.subSequence(3, 6)
            val date2 = userUltimaMensagem.mensagem.mensagemTimeDate.subSequence(7, 10)
            val date3 = userUltimaMensagem.mensagem.mensagemTimeDate.subSequence(16, 21)
            val messageData = "$date $date2 $date3"
            binding.dataMensagemTextview.text = messageData

            // Define o comportamento ao clicar no item da lista.
            binding.root.setOnClickListener {
                onUserClickListener?.onLatestUserClickListener(userUltimaMensagem)
            }
        }
    }

    /**
     * Cria um novo ViewHolder.
     *
     * @param parent O ViewGroup no qual o ViewHolder será inflado.
     * @param viewType O tipo de view a ser criado.
     * @return Um novo ViewHolder contendo a instância do ViewBinding.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UltimasConversasBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * Atualiza os dados de um ViewHolder existente.
     *
     * @param holder O ViewHolder a ser atualizado.
     * @param position A posição do item na lista de dados.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

/**
 * Classe de utilitário para calcular as diferenças entre itens da lista para otimização da RecyclerView.
 */
class LatestUserAndMessagesDiffUtil : DiffUtil.ItemCallback<UserUltimaMensagem>() {

    /**
     * Verifica se os itens são os mesmos, comparando as propriedades relevantes.
     *
     * @param oldItem O item na lista antiga.
     * @param newItem O item na nova lista.
     * @return `true` se os itens são os mesmos, `false` caso contrário.
     */
    override fun areItemsTheSame(oldItem: UserUltimaMensagem, newItem: UserUltimaMensagem): Boolean {
        return oldItem.mensagem.mensagemTimeDate == newItem.mensagem.mensagemTimeDate
    }

    /**
     * Verifica se o conteúdo dos itens é o mesmo.
     *
     * @param oldItem O item na lista antiga.
     * @param newItem O item na nova lista.
     * @return `true` se o conteúdo dos itens é o mesmo, `false` caso contrário.
     */
    override fun areContentsTheSame(
        oldItem: UserUltimaMensagem,
        newItem: UserUltimaMensagem
    ): Boolean {
        return oldItem == newItem
    }
}
