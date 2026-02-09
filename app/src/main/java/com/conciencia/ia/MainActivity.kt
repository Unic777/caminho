package com.conciencia.ia

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.conciencia.ia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var thoughtStore: ThoughtStore
    private val responseGenerator = ResponseGenerator()

    private val pickModelLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(
                uri,
                IntentFlags.persistableRead
            )
            saveModelUri(uri)
            updateModelPath(uri)
        }
    }

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

        binding.selectModelButton.setOnClickListener {
            pickModelLauncher.launch(arrayOf("application/octet-stream", "*/*"))
        }

        loadModelUri()?.let { updateModelPath(it) }
        binding.apiKeyInput.setText(loadApiKey().orEmpty())

        binding.saveApiKeyButton.setOnClickListener {
            val apiKey = binding.apiKeyInput.text?.toString()?.trim().orEmpty()
            saveApiKey(apiKey)
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

    private fun updateModelPath(uri: Uri) {
        binding.modelPath.text = uri.toString()
    }

    private fun saveModelUri(uri: Uri) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_MODEL_URI, uri.toString())
            .apply()
    }

    private fun loadModelUri(): Uri? {
        val stored = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_MODEL_URI, null)
        return stored?.let { Uri.parse(it) }
    }

    private fun saveApiKey(apiKey: String) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_API_KEY, apiKey)
            .apply()
    }

    private fun loadApiKey(): String? {
        return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_API_KEY, null)
    }

    private fun renderConversation(history: List<Thought>) {
        val conversation = history.joinToString("\n\n") { thought ->
            val label = if (thought.role == "user") "Você" else "Concienc IA"
            "$label: ${thought.content}"
        }
        binding.conversation.text = conversation
    }

    private object IntentFlags {
        const val persistableRead =
            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
    }

    companion object {
        private const val PREFS_NAME = "conciencia_prefs"
        private const val KEY_MODEL_URI = "model_uri"
        private const val KEY_API_KEY = "api_key"
    }
}
