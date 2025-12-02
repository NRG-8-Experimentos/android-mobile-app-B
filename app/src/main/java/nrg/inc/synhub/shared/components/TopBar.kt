package com.example.synhub.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import nrg.inc.synhub.notifications.viewmodel.NotificationViewModel
import nrg.inc.synhub.notifications.views.NotificationPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    function: () -> Unit,
    title: String,
    icon: ImageVector,
    actions: (@Composable () -> Unit)? = null,
    notificationViewModel: NotificationViewModel = viewModel()
){
    // Estado para controlar la visibilidad del popup flotante
    var showNotificationsPopup by remember { mutableStateOf(false) }

    // Cargar notificaciones al inicializar la pantalla que contiene este TopBar
    LaunchedEffect(Unit) {
        notificationViewModel.loadNotifications()
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = { Text(text = title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = function) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        actions = {
            // Botón de Notificaciones
            Box {
                IconButton(onClick = { showNotificationsPopup = !showNotificationsPopup }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones"
                    )

                    // Badge si hay notificaciones pendientes
                    if (notificationViewModel.notifications.isNotEmpty()) {
                        Badge(modifier = androidx.compose.ui.Modifier.align(androidx.compose.ui.Alignment.TopEnd)) {
                            Text(notificationViewModel.notifications.size.toString())
                        }
                    }
                }

                // Si la bandera está en true, mostramos el Popup flotante
                if (showNotificationsPopup) {
                    NotificationPopup(
                        viewModel = notificationViewModel,
                        onDismissRequest = { showNotificationsPopup = false } // Cerrar al tocar fuera
                    )
                }
            }

            // Acciones originales (si existen)
            actions?.invoke()
        }
    )
}