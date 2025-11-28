package com.example.synhub.tasks.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import com.example.synhub.tasks.application.dto.TaskResponse
import nrg.inc.synhub.R
import java.time.format.DateTimeFormatter
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetail(nav: NavHostController, taskId: String?) {
    val taskViewModel: TaskViewModel = viewModel()
    val task by taskViewModel.task.collectAsState()

    LaunchedEffect(taskId) {
        taskId?.toLongOrNull()?.let { taskViewModel.fetchTaskById(it) }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                function = {
                    nav.popBackStack()
                },
                stringResource(id = R.string.task_details),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> TaskDetailScreen(
        modifier = Modifier.padding(innerPadding),
        nav, task
    )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailScreen(modifier: Modifier, nav: NavHostController, task: TaskResponse?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = 20.dp),
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
                        text = stringResource(id = R.string.label_title) + ":",
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
                        text = stringResource(id = R.string.task_details) + ":",
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
                        text = stringResource(id = R.string.label_assigned_to) + ":",
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
                        text = stringResource(id = R.string.label_assigned_on) + ":",
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
                        text = stringResource(id = R.string.label_due_on) + ":",
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

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.task_not_found),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Red
                    )
                )
            }
        }
    }
}
