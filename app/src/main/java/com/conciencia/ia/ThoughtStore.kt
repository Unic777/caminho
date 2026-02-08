package com.conciencia.ia

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class ThoughtStore(private val context: Context) {
    private val fileName = "thoughts.jsonl.gz"

    fun append(thought: Thought) {
        val updated = readAll().toMutableList()
        updated.add(thought)
        writeAll(updated)
    }

    fun readAll(): List<Thought> {
        val file = context.getFileStreamPath(fileName)
        if (!file.exists()) {
            return emptyList()
        }
        context.openFileInput(fileName).use { input ->
            GZIPInputStream(input).use { gzip ->
                BufferedReader(InputStreamReader(gzip)).useLines { lines ->
                    return lines.filter { it.isNotBlank() }.mapNotNull { deserialize(it) }.toList()
                }
            }
        }
    }

    private fun writeAll(thoughts: List<Thought>) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            GZIPOutputStream(output).bufferedWriter().use { writer ->
                thoughts.forEach { thought ->
                    writer.append(serialize(thought))
                    writer.newLine()
                }
            }
        }
    }

    private fun serialize(thought: Thought): String {
        val json = JSONObject()
        json.put("timestamp", thought.timestamp)
        json.put("role", thought.role)
        json.put("content", thought.content)
        return json.toString()
    }

    private fun deserialize(line: String): Thought? {
        return runCatching {
            val json = JSONObject(line)
            Thought(
                timestamp = json.getLong("timestamp"),
                role = json.getString("role"),
                content = json.getString("content")
            )
        }.getOrNull()
    }
}
