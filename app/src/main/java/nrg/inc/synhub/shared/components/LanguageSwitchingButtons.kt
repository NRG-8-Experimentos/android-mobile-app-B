package nrg.inc.synhub.shared.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nrg.inc.synhub.MainActivity

@Composable
fun LanguageSwitchingButtons() {
    val activity = LocalContext.current as MainActivity

    Row{
        // Botón para Español
        Button(onClick = {
            activity.setAppLocale("es")
        }) {
            Text("ES")
        }
        Spacer(Modifier.width(8.dp))
        // Botón para Inglés
        Button(onClick = {
            activity.setAppLocale("en")
        }) {
            Text("EN")
        }
        Spacer(Modifier.width(8.dp))
        // Botón para Usar Idioma del Sistema (Default)
        Button(onClick = {
            activity.setAppLocale(null) // Pasa null para volver al idioma del sistema
        }) {
            Text("DEF")
        }
    }
}