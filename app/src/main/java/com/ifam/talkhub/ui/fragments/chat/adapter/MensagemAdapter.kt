package com.ifam.talkhub.ui.fragments.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ifam.talkhub.R
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.data.model.Mensagem

/**
 * Adaptador para exibir mensagens em uma conversa no RecyclerView.
 *
 * @param context Contexto da aplicação.
 * @param receptorId ID do receptor para distinguir entre mensagens enviadas e recebidas.
 */
class MensagemAdapter(
    val context: Context,
    private val receptorId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mensagensList = ArrayList<Mensagem?>()
    private var mensagensDiffUtil: MensagensDiffUtil? = null

    /**
     * Define a lista de mensagens no adaptador e atualiza a exibição usando o DiffUtil.
     *
     * @param newMessagesList Nova lista de mensagens.
     */
    fun setMensagensListToAdapter(newMessagesList: ArrayList<Mensagem?>) {
        mensagensDiffUtil = MensagensDiffUtil(
            mensagensList,
            newMessagesList
        )
        val diffResult = DiffUtil.calculateDiff(mensagensDiffUtil!!)
        this.mensagensList = newMessagesList
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Cria e retorna um ViewHolder com base no tipo de mensagem a ser exibida.
     *
     * @param parent Grupo no qual o ViewHolder será exibido.
     * @param viewType Tipo de mensagem (enviada ou recebida).
     * @return Um ViewHolder correspondente ao tipo de mensagem.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.SEND_MESSAGE) {
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.enviar_mensagem,
                parent,
                false
            )
            SendViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.receber_mensagem,
                parent,
                false
            )
            ReceiveViewHolder(view)
        }
    }

    /**
     * Retorna o número total de mensagens na lista de mensagens.
     *
     * @return O número total de mensagens na lista.
     */
    override fun getItemCount(): Int {
        return mensagensList.size
    }

    /**
     * Associa os dados de uma mensagem ao ViewHolder, dependendo do tipo de mensagem.
     *
     * @param holder O ViewHolder a ser atualizado.
     * @param position A posição da mensagem na lista.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = mensagensList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = currentMessage?.mensagem
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage?.mensagem
        }
    }

    /**
     * Retorna o tipo de visualização da mensagem com base no ID do emissor.
     *
     * @param position A posição da mensagem na lista.
     * @return O tipo de visualização da mensagem (enviada ou recebida).
     */
    override fun getItemViewType(position: Int): Int {
        val currentMessage = mensagensList[position]
        return if (receptorId == currentMessage!!.emissorId) {
            Constants.RECEIVE_MESSAGE
        } else {
            Constants.SEND_MESSAGE
        }
    }

    /**
     * ViewHolder para mensagens enviadas.
     *
     * @param itemView View da mensagem enviada.
     */
    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.caixa_flutuante_enviarmensagem_textview)
    }

    /**
     * ViewHolder para mensagens recebidas.
     *
     * @param itemView View da mensagem recebida.
     */
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.recebe_mensagem_textview)
    }
}
