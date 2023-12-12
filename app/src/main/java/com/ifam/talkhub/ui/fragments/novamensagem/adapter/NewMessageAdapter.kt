package com.ifam.talkhub.ui.fragments.novamensagem.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ifam.talkhub.data.model.User
import com.ifam.talkhub.databinding.UsuarioTelaNovamensagemBinding
import javax.inject.Inject

/**
 * Adaptador para a lista de usuários na tela de nova mensagem.
 */
class NewMessageAdapter @Inject constructor(
    private val context: Application
) : ListAdapter<User, NewMessageAdapter.ViewHolder>(UsersDiffCallBack()) {

    // Interface para lidar com cliques nos usuários
    var onClickListener: OnUserClickListener? = null

    /**
     * Define a lista de usuários no adaptador.
     *
     * @param usersList A lista de usuários a ser definida.
     */
    fun setData(usersList: ArrayList<User>) {
        this.submitList(usersList)
    }

    /**
     * ViewHolder para a lista de usuários.
     */
    inner class ViewHolder(private val binding: UsuarioTelaNovamensagemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Associa os dados do usuário ao layout da tela.
         *
         * @param user O usuário a ser exibido.
         */
        fun bind(user: User) {
            if (user.perfilImgUrl != null) {
                binding.circleImgUser.load(user.perfilImgUrl)
            }
            binding.nomeDoUsuarioTextview.text = user.username

            // Configura o clique no usuário
            binding.constraintRoot.setOnClickListener {
                onClickListener?.onUserClickListener(user)
            }
        }
    }

    /**
     * Cria e retorna uma instância de ViewHolder inflando o layout do item de usuário.
     *
     * @param parent O ViewGroup no qual o ViewHolder será inflado.
     * @param viewType O tipo de visualização do item (não utilizado neste exemplo).
     * @return Um ViewHolder que contém o layout do item de usuário.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UsuarioTelaNovamensagemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * Vincula os dados de um item de usuário ao ViewHolder.
     *
     * @param holder O ViewHolder a ser atualizado.
     * @param position A posição do item na lista.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

/**
 * Classe para calcular as diferenças entre as listas de usuários.
 */
class UsersDiffCallBack : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
