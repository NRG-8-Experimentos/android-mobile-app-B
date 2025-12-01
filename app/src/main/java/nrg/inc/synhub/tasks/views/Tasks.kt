package com.example.synhub.tasks.views

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.shared.components.TopBar
import com.example.synhub.shared.icons.editSVG
import com.example.synhub.shared.icons.trashSVG
import com.example.synhub.tasks.viewmodel.TaskViewModel
import nrg.inc.synhub.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tasks(nav: NavHostController) {
    var showHelpDialog by remember { mutableStateOf(false) }
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                function = {
                    nav.navigate("Home")
                },
                stringResource(id = R.string.tasks_title),
                Icons.AutoMirrored.Filled.ArrowBack,
                actions = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Box(
                            modifier = Modifier
                                .size(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(id = R.string.help),
                                tint = cTextMuted(),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        }
    ){
        innerPadding -> TaskScreen(modifier = Modifier.padding(innerPadding), nav)
        if (showHelpDialog) {
            AlertDialog(
                onDismissRequest = { showHelpDialog = false },
                title = { Text(stringResource(id = R.string.help), color = Color(0xFF1A4E85), fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(id = R.string.help_dialog_1), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_2), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_3), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(4.dp).fillMaxWidth().background(Color(0xFF4CAF50), shape = RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_4), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(4.dp).fillMaxWidth().background(Color(0xFFFDD634), shape = RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_5), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(4.dp).fillMaxWidth().background(Color(0xFFF44336), shape = RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_6), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(4.dp).fillMaxWidth().background(Color(0xFF4A90E2), shape = RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_7), textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(4.dp).fillMaxWidth().background(Color(0xFFFF832A), shape = RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(id = R.string.help_dialog_8), textAlign = TextAlign.Justify)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showHelpDialog = false }) {
                        Text(stringResource(id = R.string.close), color = Color(0xFF1A4E85))
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(modifier: Modifier, nav: NavHostController) {
    val tasksViewModel: TaskViewModel = viewModel()

    val tasks by tasksViewModel.tasks.collectAsState()

    val haveTasks = tasks.isNotEmpty()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskIdToDelete by remember { mutableStateOf<String?>(null) }

    val completedTasks = tasks.filter { it.status == "COMPLETED" }
    val expiredTasks = tasks.filter { it.status == "EXPIRED" }
    val doneTasks = tasks.filter { it.status == "DONE" }
    val inProgressTasks = tasks.filter { it.status == "IN_PROGRESS" }
    val onHoldTasks = tasks.filter { it.status == "ON_HOLD" }

    LaunchedEffect(Unit) {
        tasksViewModel.fetchGroupTasks()
    }

    //Log.d("Tasks.kt", "Tareas obtenias: ${tasks.size}")

    Box(modifier = Modifier.fillMaxSize()){
        if(!haveTasks)
        {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp)
                    .padding(horizontal = 20.dp)
            ) {
                NoTasks()
            }

        }else{
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp)
                    .padding(horizontal = 20.dp)
            ) {
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ){
                    item {
                        Text(stringResource(id = R.string.pending_tasks), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A4E85))
                    }
                    if(inProgressTasks.isEmpty()){
                        item {
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = Color(0xFF1A4E85)
                                )
                            ){
                                Text(
                                    text = stringResource(id = R.string.no_pending_tasks),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }else{
                        items(inProgressTasks){ task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                onClick = {
                                    nav.navigate("Tasks/Detail/${task.id}")
                                }
                            ){
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(10.dp)
                                        .background(cCard())
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
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
                                            fontSize = 20.sp,
                                            color = cTextPrimary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text=task.title,
                                        fontSize = 15.sp,
                                        color = cTextPrimary()
                                    )
                                    HorizontalDivider(color = Color.Black)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ){
                                        Column (
                                            modifier = Modifier
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                        ){
                                            Text(
                                                text=task.description,
                                                fontSize = 15.sp,
                                                color = cTextPrimary()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .background(
                                                color = getDividerColor(task.createdAt, task.dueDate, task.status),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        // Conversión de UTC a zona local
                                        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        val localFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val localFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val createdDate = try {
                                            java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter1)
                                        } catch (e: Exception) { task.createdAt.substring(0, 10) }
                                        val dueDate = try {
                                            java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter2)
                                        } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                                        Text(
                                            text = "$createdDate - $dueDate",
                                            fontSize = 15.sp,
                                            color = cTextPrimary()
                                        )
                                    }
                                    Column (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ){
                                        Row (
                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                                        ){
                                            ElevatedButton(
                                                colors = ButtonDefaults.buttonColors(Color(0xFFFF9800)),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier,
                                                onClick = {
                                                    nav.navigate("Tasks/Edit/${task.id}")
                                                }
                                            ) {
                                                Icon(
                                                    painter = rememberVectorPainter(image = editSVG),
                                                    contentDescription = null,
                                                    tint = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = stringResource(id = R.string.edit), fontSize = 15.sp,
                                                    color = Color.White, fontWeight = FontWeight.Bold
                                                )

                                            }
                                            ElevatedButton(
                                                colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier,
                                                onClick = {
                                                    taskIdToDelete = task.id.toString()
                                                    showDeleteDialog = true
                                                }
                                            ) {
                                                Icon(
                                                    painter = rememberVectorPainter(image = trashSVG),
                                                    contentDescription = null,
                                                    tint = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = stringResource(id = R.string.delete), fontSize = 15.sp,
                                                    color = Color.White, fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item{
                        Text(stringResource(id = R.string.expired_tasks), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A4E85))
                    }
                    if(expiredTasks.isEmpty()){
                        item {
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = Color(0xFF1A4E85)
                                )
                            ){
                                Text(
                                    text = stringResource(id = R.string.no_expired_tasks),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }else{
                        items(expiredTasks){ task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                onClick = {
                                    nav.navigate("Tasks/Detail/${task.id}")
                                }
                            ){
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(10.dp)
                                        .background(cCard())
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
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
                                            fontSize = 20.sp,
                                            color = cTextPrimary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text=task.title,
                                        fontSize = 15.sp,
                                        color = cTextPrimary()
                                    )
                                    HorizontalDivider(color = Color.Black)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ){
                                        Column (
                                            modifier = Modifier
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                        ){
                                            Text(
                                                text=task.description,
                                                fontSize = 15.sp,
                                                color = cTextPrimary()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .background(
                                                color = getDividerColor(task.createdAt, task.dueDate, task.status),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        // Conversión de UTC a zona local
                                        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        val localFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val localFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val createdDate = try {
                                            java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter1)
                                        } catch (e: Exception) { task.createdAt.substring(0, 10) }
                                        val dueDate = try {
                                            java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter2)
                                        } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                                        Text(
                                            text = "$createdDate - $dueDate",
                                            fontSize = 15.sp,
                                            color = cTextPrimary()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Text(stringResource(id = R.string.validation_tasks), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A4E85))
                    }
                    if(onHoldTasks.isEmpty()){
                        item {
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = Color(0xFF1A4E85)
                                )
                            ){
                                Text(
                                    text = stringResource(id = R.string.no_validation_tasks),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }else{
                        items(onHoldTasks){ task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                onClick = {
                                    nav.navigate("Tasks/Detail/${task.id}")
                                }
                            ){
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(10.dp)
                                        .background(cCard())
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
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
                                            fontSize = 20.sp,
                                            color = cTextPrimary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text=task.title,
                                        fontSize = 15.sp,
                                        color = cTextPrimary()
                                    )
                                    HorizontalDivider(color = Color.Black)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ){
                                        Column (
                                            modifier = Modifier
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                        ){
                                            Text(
                                                text=task.description,
                                                fontSize = 15.sp,
                                                color = cTextPrimary()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .background(
                                                color = getDividerColor(task.createdAt, task.dueDate, task.status),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        // Conversión de UTC a zona local
                                        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        val localFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val localFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val createdDate = try {
                                            java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter1)
                                        } catch (e: Exception) { task.createdAt.substring(0, 10) }
                                        val dueDate = try {
                                            java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter2)
                                        } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                                        Text(
                                            text = "$createdDate - $dueDate",
                                            fontSize = 15.sp,
                                            color = cTextPrimary()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Text(stringResource(id = R.string.mark_completed_tasks), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A4E85))
                    }
                    if(completedTasks.isEmpty()){
                        item {
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = Color(0xFF1A4E85)
                                )
                            ){
                                Text(
                                    text = stringResource(id = R.string.no_mark_completed_tasks),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }else{
                        items(completedTasks){ task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                onClick = {
                                    nav.navigate("Tasks/Detail/${task.id}")
                                }
                            ){
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(10.dp)
                                        .background(cCard())
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
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
                                            fontSize = 20.sp,
                                            color = cTextPrimary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text=task.title,
                                        fontSize = 15.sp,
                                        color = cTextPrimary()
                                    )
                                    HorizontalDivider(color = Color.Black)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ){
                                        Column (
                                            modifier = Modifier
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                        ){
                                            Text(
                                                text=task.description,
                                                fontSize = 15.sp,
                                                color = cTextPrimary()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .background(
                                                color = getDividerColor(task.createdAt, task.dueDate, task.status),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        // Conversión de UTC a zona local
                                        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        val localFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val localFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val createdDate = try {
                                            java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter1)
                                        } catch (e: Exception) { task.createdAt.substring(0, 10) }
                                        val dueDate = try {
                                            java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter2)
                                        } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                                        Text(
                                            text = "$createdDate - $dueDate",
                                            fontSize = 15.sp,
                                            color = cTextPrimary()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Text(stringResource(id = R.string.completed_tasks), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A4E85))
                    }
                    if(doneTasks.isEmpty()){
                        item {
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = Color(0xFF1A4E85)
                                )
                            ){
                                Text(
                                    text = stringResource(id = R.string.no_completed_tasks),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }else{
                        items(doneTasks){ task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                onClick = {
                                    nav.navigate("Tasks/Detail/${task.id}")
                                }
                            ){
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(10.dp)
                                        .background(cCard())
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
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
                                            fontSize = 20.sp,
                                            color = cTextPrimary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text=task.title,
                                        fontSize = 15.sp,
                                        color = cTextPrimary()
                                    )
                                    HorizontalDivider(color = Color.Black)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                clip = true
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ){
                                        Column (
                                            modifier = Modifier
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                        ){
                                            Text(
                                                text=task.description,
                                                fontSize = 15.sp,
                                                color = cTextPrimary()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .background(
                                                color = getDividerColor(task.createdAt, task.dueDate, task.status),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        // Conversión de UTC a zona local
                                        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        val localFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val localFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val createdDate = try {
                                            java.time.ZonedDateTime.parse(task.createdAt, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter1)
                                        } catch (e: Exception) { task.createdAt.substring(0, 10) }
                                        val dueDate = try {
                                            java.time.ZonedDateTime.parse(task.dueDate, utcFormatter)
                                                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                                                .format(localFormatter2)
                                        } catch (e: Exception) { task.dueDate.replace("T", " ").substring(0, 16) }
                                        Text(
                                            text = "$createdDate - $dueDate",
                                            fontSize = 15.sp,
                                            color = cTextPrimary()
                                        )
                                    }
                                    Column (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ){
                                        ElevatedButton(
                                            colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier,
                                            onClick = {
                                                taskIdToDelete = task.id.toString()
                                                showDeleteDialog = true
                                            }
                                        ) {
                                            Icon(
                                                painter = rememberVectorPainter(image = trashSVG),
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(id = R.string.delete), fontSize = 15.sp,
                                                color = Color.White, fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { nav.navigate("Tasks/Create") },
            containerColor = Color(0xFF1A4E85),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
            )
        }

        if (showDeleteDialog && taskIdToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(id = R.string.confirm_delete), fontWeight = FontWeight.Bold, ) },
                text = { Text(stringResource(id = R.string.confirm_task_delete)) },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                        onClick = {
                        tasksViewModel.deleteTask(taskIdToDelete!!.toLong())
                        showDeleteDialog = false
                        taskIdToDelete = null
                    }) {
                        Text(stringResource(id = R.string.delete), color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(Color(0xFF1A4E85)),
                        onClick = {
                        showDeleteDialog = false
                        taskIdToDelete = null
                    }) {
                        Text(stringResource(id = R.string.cancel), color = Color.White)
                    }
                }
            )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun getDividerColor(
    createdAt: String,
    dueDate: String,
    status: String
): Color {
    if (status == "COMPLETED") return Color(0xFF4CAF50) // Verde para tareas completadas
    if (status == "ON_HOLD") return Color(0xFFFF832A) // Naranja para tareas en espera
    if (status == "DONE") return Color(0xFF4A90E2) // Azul para tareas hechas

    val formatters = listOf(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )
    fun parseDateTime(dateStr: String): ZonedDateTime {
        for (formatter in formatters) {
            try {
                // Intenta parsear como ZonedDateTime
                return ZonedDateTime.parse(dateStr, formatter)
            } catch (_: Exception) {
                try {
                    // Intenta parsear como LocalDateTime y asumir UTC
                    return java.time.LocalDateTime.parse(dateStr, formatter).atZone(java.time.ZoneOffset.UTC)
                } catch (_: Exception) {}
            }
        }
        throw IllegalArgumentException("Formato de fecha no soportado: $dateStr")
    }
    val created = parseDateTime(createdAt)
    val due = parseDateTime(dueDate)
    val now = ZonedDateTime.now(java.time.ZoneOffset.UTC)

    val totalSeconds = java.time.Duration.between(created, due).seconds.toFloat().coerceAtLeast(1f)
    val secondsPassed = java.time.Duration.between(created, now).seconds.toFloat()
    val progress = (secondsPassed / totalSeconds).coerceIn(0f, 1f)

    return when {
        now.isAfter(due) -> Color(0xFFF44336)
        progress < 0.7f -> Color(0xFF4CAF50)
        else -> Color(0xFFFDD634)
    }
}

@Composable
fun NoTasks(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp))
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color(0xFF1A4E85),
                shape = RoundedCornerShape(10.dp)
            ),
            contentAlignment = Alignment.Center){
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_tasks),
                    fontSize = 20.sp,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}