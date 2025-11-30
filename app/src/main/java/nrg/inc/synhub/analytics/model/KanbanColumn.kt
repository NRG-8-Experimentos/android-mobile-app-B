package com.example.synhub.analytics.model

import com.example.synhub.tasks.application.dto.TaskResponse

data class KanbanColumn(
    val status: String,
    val title: String,
    val tasks: List<TaskResponse>,
    val color: String,
    val icon: String
)

