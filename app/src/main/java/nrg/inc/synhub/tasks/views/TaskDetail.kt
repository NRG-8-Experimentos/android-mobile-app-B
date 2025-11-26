package com.example.synhub.tasks.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.shared.components.TopBar
import com.example.synhub.tasks.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.synhub.requests.viewModel.RequestViewModel
import com.example.synhub.tasks.application.dto.TaskResponse
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetail(nav: NavHostController, taskId: String?) {
    val taskViewModel: TaskViewModel = viewModel()
    val requestViewModel: RequestViewModel = viewModel()
    val task by taskViewModel.task.collectAsState()
    val requests by requestViewModel.requests.collectAsState()
    val isLoadingRequests by requestViewModel.isLoading.collectAsState()

    LaunchedEffect(taskId) {
        taskId?.toLongOrNull()?.let {
            taskViewModel.fetchTaskById(it)
            requestViewModel.fetchRequestsByTaskId(it)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopBar(
                function = {
                    nav.popBackStack()
                },
                "Detalles de la tarea",
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> TaskDetailScreen(
        modifier = Modifier.padding(innerPadding),
        nav,
        task,
        requests,
        isLoadingRequests,
        onAddComment = { description ->
            taskId?.toLongOrNull()?.let { id ->
                requestViewModel.createRequest(
                    taskId = id,
                    description = description,
                    requestType = "MODIFICATION",
                    onSuccess = {
                        requestViewModel.fetchRequestsByTaskId(id)
                    },
                    onError = { error ->
                        // Handle error
                    }
                )
            }
        }
    )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailScreen(
    modifier: Modifier,
    nav: NavHostController,
    task: TaskResponse?,
    requests: List<com.example.synhub.requests.application.dto.RequestResponse>,
    isLoadingRequests: Boolean,
    onAddComment: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (task != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Título:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(color = Color.Black)
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = task.title,
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Detalles de la tarea:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(color = Color.Black)
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = task.description,
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Asignado a:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(color = Color.Black)
                    Spacer(modifier = Modifier.size(15.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = CircleShape,
                                    clip = true
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = task.member.urlImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape)
                            )
                        }
                        Text(
                            text = task.member.name + " " + task.member.surname,
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Asignado el:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(color = Color.Black)
                    Spacer(modifier = Modifier.size(15.dp))
                    val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    val localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val createdDate = try {
                        java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                            .withZoneSameInstant(java.time.ZoneId.systemDefault())
                            .format(localFormatter)
                    } catch (e: Exception) { task.createdAt.substring(0, 10) }
                    Text(
                        text = createdDate,
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Vence el:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    HorizontalDivider(color = Color.Black)
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(
                                color =

                                    getDividerColor(task.createdAt, task.dueDate, task.status),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    val localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val dueDate = try {
                        java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                            .withZoneSameInstant(java.time.ZoneId.systemDefault())
                            .format(localFormatter)
                    } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                    Text(
                        text = dueDate,
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            }

            // Comments Section
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Comentarios",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    HorizontalDivider(color = Color.Black, modifier = Modifier.padding(vertical = 10.dp))


                    // Comments List
                    if (isLoadingRequests) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF6200EE))
                        }
                    } else {
                        if (requests.isEmpty()) {
                            Text(
                                text = "No hay comentarios aún",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                requests.forEach { comment ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color.White,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // Request Type Badge
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = when(comment.requestType) {
                                                                "MODIFICATION" -> Color(0xFF2196F3)
                                                                else -> Color.Gray
                                                            },
                                                            shape = RoundedCornerShape(4.dp)
                                                        )
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = comment.requestType,
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }

                                                // Request Status Badge
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = when(comment.requestStatus) {
                                                                "PENDING" -> Color(0xFFFFA726)
                                                                "APPROVED" -> Color(0xFF66BB6A)
                                                                "REJECTED" -> Color(0xFFEF5350)
                                                                else -> Color.Gray
                                                            },
                                                            shape = RoundedCornerShape(4.dp)
                                                        )
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = comment.requestStatus,
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = comment.description,
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = Color.Black
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tarea no encontrada",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Red
                    )
                )
            }
        }
    }
}
