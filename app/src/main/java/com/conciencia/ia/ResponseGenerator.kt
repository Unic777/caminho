package com.conciencia.ia

class ResponseGenerator {
    fun generate(
        mode: AssistantMode,
        history: List<Thought>,
        userInput: String
    ): String {
        val memories = history.takeLast(6).joinToString(" ") { it.content }
        return when (mode) {
            AssistantMode.ONLINE -> {
                "[Online GPT] Vou considerar: $memories. Sua dúvida: $userInput"
            }
            AssistantMode.OFFLINE -> {
                "[Offline GGUF] Vou considerar: $memories. Sua dúvida: $userInput"
            }
        }
    }
}
