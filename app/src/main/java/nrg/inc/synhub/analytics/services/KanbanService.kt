package com.example.synhub.analytics.services

import com.example.synhub.analytics.model.KanbanColumn
import com.example.synhub.tasks.application.dto.TaskResponse

class KanbanService {
    companion object {
        private val columnConfig = mapOf(
            "ON_HOLD" to Triple("Pendientes", "#f59e42", "pause_circle_outline"),
            "IN_PROGRESS" to Triple("En Progreso", "#3b82f6", "autorenew"),
            "COMPLETED" to Triple("Completadas", "#22c55e", "check_circle"),
            "DONE" to Triple("Terminadas", "#14b8a6", "done_all"),
            "EXPIRED" to Triple("Atrasadas", "#ef4444", "error_outline")
        )
    }

    fun organizeTasksIntoColumns(tasks: List<TaskResponse>): List<KanbanColumn> {
        return columnConfig.map { (status, config) ->
            val columnTasks = tasks.filter { it.status == status }
            KanbanColumn(
                status = status,
                title = config.first,
                tasks = columnTasks,
                color = config.second,
                icon = config.third
            )
        }
    }

    fun getTaskCountByStatus(tasks: List<TaskResponse>, status: String): Int {
        return tasks.count { it.status == status }
    }

    fun getTasksByStatus(tasks: List<TaskResponse>, status: String): List<TaskResponse> {
        return tasks.filter { it.status == status }
    }

    fun getTasksByMember(tasks: List<TaskResponse>, memberId: Long): List<TaskResponse> {
        return tasks.filter { it.member.id == memberId }
    }
}
