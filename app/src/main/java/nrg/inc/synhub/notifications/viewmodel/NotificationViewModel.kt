package nrg.inc.synhub.notifications.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synhub.shared.model.client.RetrofitClient
import kotlinx.coroutines.launch
import nrg.inc.synhub.notifications.model.Notification

class NotificationViewModel : ViewModel() {
    private val _notifications = mutableStateListOf<Notification>()
    val notifications: List<Notification> get() = _notifications

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.notificationService.getAllNotifications()
                _notifications.clear()
                _notifications.addAll(response.filter { !it.isRead })
            } catch (e: Exception) {
                Log.e("NotificationVM", "Error cargando notificaciones", e)
            }
        }
    }

    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.notificationService.markAsRead(notificationId)
                if (response.isSuccessful) {
                    _notifications.removeIf { it.id == notificationId }
                }
            } catch (e: Exception) {
                Log.e("NotificationVM", "Error marcando como leída", e)
            }
        }
    }
}