package nrg.inc.synhub.notifications.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import nrg.inc.synhub.notifications.model.Notification
import nrg.inc.synhub.notifications.viewmodel.NotificationViewModel

@Composable
fun NotificationPopup(
    viewModel: NotificationViewModel,
    onDismissRequest: () -> Unit
) {
    val notifications = viewModel.notifications

    // Popup flotante
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .padding(top = 50.dp, end = 8.dp) // Ajuste para que no tape el TopBar
                .width(320.dp)
                .heightIn(max = 400.dp) // Altura máxima para scrollear si hay muchas
                .shadow(8.dp, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text("No tienes nuevas notificaciones", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClose = { viewModel.markAsRead(notification.id) }
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icono (Lado Izquierdo)
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = Color(0xFF6366F1), // Tu color 'indigo'
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Texto (Centro)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = notification.createdAt.take(10) + " " + notification.createdAt.substring(11, 16), // Formato simple fecha
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Botón Cerrar (Derecha)
        IconButton(
            onClick = onClose,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Marcar leída",
                tint = Color.Gray
            )
        }
    }
}