package com.example.synhub.tasks.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.synhub.groups.viewmodel.GroupViewModel
import com.example.synhub.shared.components.TopBar
import com.example.synhub.shared.icons.abcSVG
import com.example.synhub.shared.icons.calendarSVG
import com.example.synhub.shared.icons.keyboardSVG
import com.example.synhub.shared.icons.logoutSVG
import com.example.synhub.shared.icons.personSVG
import com.example.synhub.shared.icons.saveSVG
import com.example.synhub.tasks.application.dto.TaskRequest
import com.example.synhub.tasks.viewmodel.TaskViewModel
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import android.app.TimePickerDialog
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import nrg.inc.synhub.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.text.format
import kotlin.text.set
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTask(nav: NavHostController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                function = {
                    nav.popBackStack()
                },
                stringResource(id = R.string.create_task_title),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> CreateTaskScreen(modifier = Modifier.padding(innerPadding),
        nav)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(modifier: Modifier = Modifier, nav: NavHostController
) {

    val groupViewModel:GroupViewModel = viewModel()
    val taskViewModel:TaskViewModel = viewModel()

    val members by groupViewModel.members.collectAsState()

    var txtTitle by remember { mutableStateOf("") }
    var txtDescription by remember { mutableStateOf("") }
    var txtMemberId by remember { mutableStateOf<Long?>(null) }
    var txtDueDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        groupViewModel.fetchGroupMembers()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = txtTitle,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.label_task_title)) },
            placeholder = { Text(text = stringResource(id = R.string.placeholder_title)) },
            leadingIcon = {
                Icon(
                    imageVector = abcSVG,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
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
            label = { Text(text = stringResource(id = R.string.label_description)) },
            placeholder = { Text(text = stringResource(id = R.string.placeholder_description)) },
            leadingIcon = {
                Icon(
                    imageVector = keyboardSVG,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = cSurface(),
                unfocusedContainerColor = cSurface(),
                cursorColor = Color.Cyan
            ),
            onValueChange = {txtDescription=it}
        )

        // Selector de integrante
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
                label = { Text(text = stringResource(id = R.string.label_member)) },
                placeholder = { Text(text = stringResource(id = R.string.placeholder_member)) },
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
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(text = member.name + " " + member.surname) },
                        onClick = {
                            txtMemberId = member.id
                            expanded = false
                        }
                    )
                }
            }
        }

        // Selector de fecha tipo modal usando DatePickerModal y TimePickerDialog
        var showModal by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val formatterUtc = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
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
                        val zonedDateTime = java.time.Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC"))
                        val year = zonedDateTime.year
                        val month = zonedDateTime.monthValue
                        val day = zonedDateTime.dayOfMonth
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val localDateTime = LocalDateTime.of(year, month, day, hour, minute)
                                val zonedLocal = localDateTime.atZone(ZoneId.systemDefault())
                                val zonedUtc = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"))
                                dueDateUtc = zonedUtc.format(formatterUtc)
                                txtDueDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
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
                    taskViewModel.createTask(txtMemberId ?: 0L,
                        TaskRequest(
                            title = txtTitle,
                            description = txtDescription,
                            dueDate = dueDateUtc,
                        )
                    )
                    nav.popBackStack()
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.date_picker_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.date_picker_cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}