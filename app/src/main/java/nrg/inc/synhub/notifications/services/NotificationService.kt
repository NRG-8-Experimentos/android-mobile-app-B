package nrg.inc.synhub.notifications.services

import nrg.inc.synhub.notifications.model.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationService {

    @GET("api/v1/member/notifications")
    suspend fun getAllNotifications(): List<Notification>

    @PATCH("api/v1/member/notifications/{id}/mark-read")
    suspend fun markAsRead(@Path("id") notificationId: Long): Response<Unit>
}