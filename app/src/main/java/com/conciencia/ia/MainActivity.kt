package com.conciencia.ia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.conciencia.ia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var thoughtStore: ThoughtStore
    private val responseGenerator = ResponseGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thoughtStore = ThoughtStore(this)
        val history = thoughtStore.readAll().toMutableList()
        renderConversation(history)

        binding.summary.text = if (history.isEmpty()) {
            "Nenhum pensamento salvo ainda."
        } else {
            "${history.size} pensamentos carregados para contexto."
        }

        binding.sendButton.setOnClickListener {
            val input = binding.input.text?.toString()?.trim().orEmpty()
            if (input.isBlank()) {
                return@setOnClickListener
            }
            val mode = if (binding.modeSwitch.isChecked) {
                AssistantMode.ONLINE
            } else {
                AssistantMode.OFFLINE
            }
            val userThought = Thought(
                timestamp = System.currentTimeMillis(),
                role = "user",
                content = input
            )
            history.add(userThought)
            thoughtStore.append(userThought)

            val responseText = responseGenerator.generate(mode, history, input)
            val assistantThought = Thought(
                timestamp = System.currentTimeMillis(),
                role = "assistant",
                content = responseText
            )
            history.add(assistantThought)
            thoughtStore.append(assistantThought)

            binding.input.setText("")
            binding.summary.text = "${history.size} pensamentos carregados para contexto."
            renderConversation(history)
        }
    }

    private fun renderConversation(history: List<Thought>) {
        val conversation = history.joinToString("\n\n") { thought ->
            val label = if (thought.role == "user") "Você" else "Concienc IA"
            "$label: ${thought.content}"
        }
        binding.conversation.text = conversation
    }
}
