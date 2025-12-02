package nrg.inc.synhub.notifications.model

data class Notification(
    val id: Long,
    val message: String,
    val relatedTaskId: Long,
    val isRead: Boolean,
    val createdAt: String
)