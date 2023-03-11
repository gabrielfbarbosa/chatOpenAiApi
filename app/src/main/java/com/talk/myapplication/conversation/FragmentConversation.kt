package com.talk.myapplication.conversation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.talk.myapplication.R
import com.talk.myapplication.conversation.adapter.ChatAdapter
import com.talk.myapplication.databinding.FragmentConversationBinding
import com.talk.myapplication.service.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class FragmentConversation : Fragment() {

    private lateinit var openAIRepository: OpenAIRepository
    private lateinit var binding: FragmentConversationBinding
    private val messageList: MutableList<OpenAiPayload> = mutableListOf()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openAIRepository = OpenAIRepository(retrofit.create(OpenAIService::class.java))

        chatAdapter = ChatAdapter(messageList)
        binding.tvRecyclerView.adapter = chatAdapter
        binding.tvRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.tvRecyclerView.layoutManager?.scrollToPosition(messageList.size - 1)

        val apiKey = "YOUR_API_KEY_HERE"

        binding.fabBtn.setOnClickListener {
            binding.fabBtn.apply {
                isClickable = false
                alpha = 0.2f
            }
            val prompt = binding.etInput.text.toString()
            val userMessage = OpenAiPayload(prompt, true)
            messageList.add(userMessage)
            chatAdapter.notifyItemChanged(messageList.size - 1)
            binding.tvRecyclerView.layoutManager?.scrollToPosition(messageList.size - 1)
            binding.etInput.setText("")

            try {
                lifecycleScope.launch {
                    val response = openAIRepository.getResponse(apiKey, prompt)

                    withContext(Dispatchers.Main){
                        // Cria um objeto OpenAIChoice com a resposta da API e adiciona à lista
                        val apiMessage = response?.choices?.get(0)?.message?.let { it1 ->
                            OpenAiPayload(it1.content.trimStart(), false)
                        }
                        if (apiMessage != null) {
                            messageList.add(apiMessage)
                            binding.fabBtn.apply {
                                isClickable = true
                                alpha = 1.0f
                            }
                        }
                        chatAdapter.notifyItemChanged(messageList.size - 1)
                        binding.tvRecyclerView.scrollToPosition(messageList.size - 1)

                    }
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Toast.makeText(context, "Erro de autenticação. Faça login novamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro: " + e.message, Toast.LENGTH_SHORT).show()
                }
            }  catch (e: SocketTimeoutException) {
//                Toast.makeText(context, "Erro SocketTimeoutException ", Toast.LENGTH_SHORT).show()
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Erro de conexão")
                    .setMessage("Houve um erro de conexão. Verifique sua conexão com a internet e tente novamente.")
                    .setPositiveButton("Ok") { _, _ -> }
                    .create()
                alertDialog.show()

            } catch (e: Throwable) {
                Toast.makeText(context, "Erro: " + e.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.fabBtn.apply {
                    isClickable = true
                    alpha = 1.0f
                }
            }

//           catch (e: Exception){
//
////                Toast.makeText(context, "Erro ao se comunicar com o servidor"+ e, Toast.LENGTH_SHORT).show()
//
//                val apiMessage= OpenAiPayload("[ ERRO: $e", false)
//                messageList.add(apiMessage)
//                chatAdapter.notifyItemChanged(messageList.size - 1)
//                requireActivity().runOnUiThread {
//                    binding.tvRecyclerView.layoutManager?.scrollToPosition(messageList.size - 1)
//                }
//            }
        }
    }
}