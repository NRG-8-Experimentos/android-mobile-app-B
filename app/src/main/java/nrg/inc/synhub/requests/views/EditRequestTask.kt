package com.example.synhub.requests.views

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.synhub.groups.viewmodel.GroupViewModel
import com.example.synhub.requests.viewModel.RequestViewModel
import com.example.synhub.shared.components.TopBar
import com.example.synhub.shared.icons.abcSVG
import com.example.synhub.shared.icons.calendarSVG
import com.example.synhub.shared.icons.keyboardSVG
import com.example.synhub.shared.icons.logoutSVG
import com.example.synhub.shared.icons.personSVG
import com.example.synhub.shared.icons.saveSVG
import com.example.synhub.shared.model.client.RetrofitClient
import com.example.synhub.tasks.application.dto.EditTaskRequest
import com.example.synhub.tasks.application.dto.TaskResponse
import com.example.synhub.tasks.viewmodel.TaskViewModel
import com.example.synhub.tasks.views.DatePickerModal
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nrg.inc.synhub.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditRequestTask(nav: NavHostController, taskId: String?, requestId: String?) {
    val taskViewModel: TaskViewModel = viewModel()
    val task by taskViewModel.task.collectAsState()

    LaunchedEffect(taskId, requestId) {
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
                stringResource(id = R.string.edit_task_title),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> EditRequestTaskScreen(modifier = Modifier.padding(innerPadding),
        nav, task, requestId)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRequestTaskScreen(modifier: Modifier = Modifier, nav: NavHostController, task: TaskResponse?, requestId: String?)
{
    val groupViewModel:GroupViewModel = viewModel()
    val taskViewModel:TaskViewModel = viewModel()
    val requestViewModel: RequestViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val members by groupViewModel.members.collectAsState()

    var txtTitle by remember { mutableStateOf("") }
    var txtDescription by remember { mutableStateOf("") }
    var txtMemberId by remember { mutableStateOf<Long?>(null) }
    var txtDueDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        groupViewModel.fetchGroupMembers()
    }

    LaunchedEffect(task) {
        if (task != null) {
            txtMemberId = task.member?.id
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (task != null) {
            OutlinedTextField(
                value = txtTitle,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.label_task_title)) },
                placeholder = { Text(text = task.title) },
                leadingIcon = {
                    Icon(
                        imageVector = abcSVG,
                        tint = Color.Gray,
                        contentDescription = ""
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = cSurface(),
                    unfocusedContainerColor = cSurface(),
                    cursorColor = Color.Cyan
                ),
                onValueChange = {txtTitle=it}
            )

            OutlinedTextField(
                value = txtDescription,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.description_label)) },
                placeholder = { Text(text = task.description) },
                leadingIcon = {
                    Icon(
                        imageVector = keyboardSVG,
                        tint = Color.Gray,
                        contentDescription = ""
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = cSurface(),
                    unfocusedContainerColor = cSurface(),
                    cursorColor = Color.Cyan
                ),
                onValueChange = {txtDescription=it}
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = txtMemberId?.let { id ->
                        members.find { it.id == id }?.let { it.name + " " + it.surname } ?: ""
                    } ?: "",
                    onValueChange = {}, // No editable manualmente
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text(text = stringResource(id = R.string.member_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.member_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = personSVG,
                            tint = Color.Gray,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cSurface(),
                        unfocusedContainerColor = cSurface(),
                        cursorColor = Color.Cyan
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    members.forEach { member ->
                        DropdownMenuItem(
                            text = { Text(text = member.name + " " + member.surname) },
                            onClick = {
                                txtMemberId = member.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            var showModal by remember { mutableStateOf(false) }
            var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
            val context = LocalContext.current
            val formatterUtc = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val calendar = remember { Calendar.getInstance() }
            var dueDateUtc by remember { mutableStateOf("") }

            OutlinedTextField(
                value = txtDueDate.format(formatterLocal),
                onValueChange = {},
                label = { Text(stringResource(id = R.string.label_due_date)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_due_date)) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showModal = true }) {
                        Icon(
                            imageVector = calendarSVG,
                            contentDescription = stringResource(id = R.string.cd_select_date)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showModal = true }
            )

            if (showModal) {
                DatePickerModal(
                    onDateSelected = { millis ->
                        if (millis != null) {
                            // Interpretar millis como UTC para evitar desfase de día
                            val zonedDateTime =
                                Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC"))
                            val year = zonedDateTime.year
                            val month = zonedDateTime.monthValue
                            val day = zonedDateTime.dayOfMonth
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val localDateTime =
                                        LocalDateTime.of(year, month, day, hour, minute)
                                    val zonedLocal = localDateTime.atZone(ZoneId.systemDefault())
                                    val zonedUtc = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"))
                                    dueDateUtc = zonedUtc.format(formatterUtc)
                                    txtDueDate =
                                        localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                    showModal = false
                                },
                                0,
                                0,
                                true
                            ).show()
                        } else {
                            showModal = false
                        }
                    },
                    onDismiss = { showModal = false }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)){
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier,
                    onClick = {
                        if (requestId != null) {
                            coroutineScope.launch {
                                Log.d("EditRequestTask", "Calling deleteRequest")
                                val updateTaskResponse = RetrofitClient.tasksWebService.updateTask(
                                    task.id,
                                    EditTaskRequest(
                                        txtTitle,
                                        txtDescription,
                                        dueDateUtc,
                                        txtMemberId ?: 0L
                                    )
                                )
                                if (updateTaskResponse.isSuccessful) {
                                    val updateStatusResponse = RetrofitClient.tasksWebService.updateTaskStatus(task.id, "IN_PROGRESS")
                                    if (updateStatusResponse.isSuccessful) {
                                        val deleteRequestResponse = RetrofitClient.requestsWebService.deleteRequest(task.id, requestId.toLong())
                                        if (deleteRequestResponse.isSuccessful) {
                                            nav.navigate("GroupRequests") {
                                                popUpTo("Home") { inclusive = false }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d("EditRequestTask", "RequestId is null, not calling deleteRequest")
                        }

                    }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = saveSVG),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.save), fontSize = 20.sp,
                        color = Color.White, fontWeight = FontWeight.Bold
                    )

                }
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier,
                    onClick = {
                        nav.popBackStack()
                    }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = logoutSVG),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.cancel), fontSize = 20.sp,
                        color = Color.White, fontWeight = FontWeight.Bold
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