package com.example.synhub.shared.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nrg.inc.synhub.MainActivity
import nrg.inc.synhub.R

@Composable
fun AppearanceSwitcher() {
    val ctx = LocalContext.current
    var mode by remember {
        mutableStateOf(AppCompatDelegate.getDefaultNightMode())
    }

    fun set(modeNew: Int){
        mode = modeNew
        (ctx as? MainActivity)?.setAppAppearance(modeNew)
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.appearance),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                onClick = { set(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) },
                label = { Text(text = stringResource(id = R.string.appearance_system)) }
            )
            FilterChip(
                selected = mode == AppCompatDelegate.MODE_NIGHT_NO,
                onClick = { set(AppCompatDelegate.MODE_NIGHT_NO) },
                label = { Text(text = stringResource(id = R.string.appearance_light)) }
            )
            FilterChip(
                selected = mode == AppCompatDelegate.MODE_NIGHT_YES,
                onClick = { set(AppCompatDelegate.MODE_NIGHT_YES) },
                label = { Text(text = stringResource(id = R.string.appearance_dark)) }
            )
        }
    }
}
