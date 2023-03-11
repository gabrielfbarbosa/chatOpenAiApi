package com.talk.myapplication.conversation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.talk.myapplication.R
import com.talk.myapplication.service.OpenAIChoice
import com.talk.myapplication.service.OpenAiPayload

class ChatAdapter(private val message: List<OpenAiPayload>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    companion object{
        private const val USER_MESSAGE = 0
        private const val API_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (message[position].isUserMessage) USER_MESSAGE else API_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = if (viewType == USER_MESSAGE){
            layoutInflater.inflate(R.layout.user_message, parent, false)
        } else {
            layoutInflater.inflate(R.layout.api_message, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(message[position])
    }

    override fun getItemCount(): Int {
        return message.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(message: OpenAiPayload) {
            val messageTextView: TextView = itemView.findViewById(R.id.box_message)
            messageTextView.text = message.prompt
        }
    }
}