package com.example.synhub.shared.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun getDividerColor(
    createdAt: String,
    dueDate: String,
    status: String
): Color {
    if (status == "COMPLETED") return Color(0xFF4CAF50)
    if (status == "ON_HOLD") return Color(0xFFFF832A)
    if (status == "DONE") return Color(0xFF4A90E2)

    val formatters = listOf(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )

    fun parseDateTime(dateStr: String): ZonedDateTime {
        for (formatter in formatters) {
            try {
                return ZonedDateTime.parse(dateStr, formatter)
            } catch (_: Exception) {
                try {
                    return LocalDateTime.parse(dateStr, formatter).atZone(ZoneOffset.UTC)
                } catch (_: Exception) {}
            }
        }
        throw IllegalArgumentException("Formato de fecha no soportado: $dateStr")
    }

    val created = parseDateTime(createdAt)
    val due = parseDateTime(dueDate)
    val now = ZonedDateTime.now(ZoneOffset.UTC)

    val totalSeconds = Duration.between(created, due).seconds.toFloat().coerceAtLeast(1f)
    val secondsPassed = Duration.between(created, now).seconds.toFloat()
    val progress = (secondsPassed / totalSeconds).coerceIn(0f, 1f)

    return when {
        now.isAfter(due) -> Color(0xFFF44336)
        progress < 0.7f -> Color(0xFF4CAF50)
        else -> Color(0xFFFDD634)
    }
}

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return "Sin fecha"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
        val date = parser.parse(dateString)
        date?.let { formatter.format(it) } ?: "Fecha inválida"
    } catch (e: Exception) {
        "Fecha inválida"
    }
}

