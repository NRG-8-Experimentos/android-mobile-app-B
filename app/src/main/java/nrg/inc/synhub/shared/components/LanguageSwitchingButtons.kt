package nrg.inc.synhub.shared.components


import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import nrg.inc.synhub.MainActivity
private val Context.dataStore by preferencesDataStore(name = "settings")

@Composable
fun LanguageSwitchingButtons() {
    val activity = LocalContext.current as MainActivity
    val options = listOf("ES", "EN", "DEF")
    val selectedOption = remember { mutableStateOf("DEF") }
    val context = LocalContext.current

    // Restaurar la selección guardada
    LaunchedEffect(Unit) {
        val savedOption = runBlocking {
            val preferences = context.dataStore.data.first()
            preferences[stringPreferencesKey("selected_language")] ?: "DEF"
        }
        selectedOption.value = savedOption
    }

    Row {
        options.forEach { option ->
            Button(
                onClick = {
                    selectedOption.value = option
                    activity.setAppLocale(
                        when (option) {
                            "ES" -> "es"
                            "EN" -> "en"
                            else -> null
                        }
                    )
                    // Guardar la selección
                    runBlocking {
                        context.dataStore.edit { preferences ->
                            preferences[stringPreferencesKey("selected_language")] = option
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption.value == option) Color.Gray else Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.weight(1f),
                shape = RectangleShape
            ) {
                Text(option)
            }
        }
    }
}