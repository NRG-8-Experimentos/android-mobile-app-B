package com.example.synhub.requests.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.requests.application.dto.RequestResponse
import com.example.synhub.requests.viewModel.RequestViewModel
import com.example.synhub.shared.components.TopBar
import com.example.synhub.tasks.viewmodel.TaskViewModel
import nrg.inc.synhub.R
import java.time.format.DateTimeFormatter
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ValidationView(nav: NavHostController, taskId: String?, requestId: String?) {
    val requestViewModel: RequestViewModel = viewModel()
    val request by requestViewModel.request.collectAsState()

    LaunchedEffect(taskId, requestId) {
        val tId = taskId?.toLongOrNull()
        val rId = requestId?.toLongOrNull()
        if (tId != null && rId != null) {
            requestViewModel.fetchRequestById(tId, rId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                function = {
                    nav.popBackStack()
                },
                title = stringResource(id = R.string.validation_top_bar_title),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) {
        innerPadding -> ValidationDetails(
            modifier = Modifier.padding(innerPadding),nav, request)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ValidationDetails(
    modifier: Modifier,
    nav: NavHostController,
    request: RequestResponse?,
    // The models are used to update info
    requestViewModel: RequestViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel()) {

    val statusColor = when (request?.requestType) {
        "SUBMISSION" -> Color(0xFF4CAF50) // Green
        "MODIFICATION" -> Color(0xFFFF832A)   // Amber
        "EXPIRED" -> Color(0xFFFF5252)   // Red
        else -> Color(0xFFE0E0E0)        // Default gray
    }

    fun createdDate(timestamp: String?): String? {
        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val createdDate = try {
            java.time.ZonedDateTime.parse(timestamp, utcFormatter)
                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                .format(localFormatter)
        } catch (e: Exception) {
            timestamp?.substring(0, 10)
        }
        return createdDate;
    }

    fun dueDate(timestamp: String?): String? {
        val utcFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dueDate = try {
            java.time.ZonedDateTime.parse(timestamp, utcFormatter)
                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                .format(localFormatter)
        } catch (e: Exception) {
            timestamp?.replace("T", " ")?.substring(0, 16)

        }
        return dueDate;
    }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .padding(top = 90.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                    model = request?.task?.member?.urlImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = request?.task?.member?.name + " " + request?.task?.member?.surname,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp, color = cTextPrimary())
        }
        Card(
            modifier = Modifier
                .padding(vertical = 15.dp),
            elevation = CardDefaults
                .cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(160.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A4E85)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Text(text = request?.task?.title.toString(), color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(thickness = 2.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = stringResource(id = R.string.comment) + ": ${request?.task?.description}", color = Color.White)
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = when (request?.task?.status) {
                            "COMPLETED" -> stringResource(id = R.string.validation_time_development)
                            else -> stringResource(id = R.string.validation_time_development_assigned)
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    when (request?.task?.status) {
                        "COMPLETED" -> createdDate(request.task.createdAt)
                        else -> createdDate(request?.task?.createdAt) + " - " + dueDate(request?.task?.dueDate)
                    }?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusColor)
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nav.navigate("Validation/Edit/${request?.task?.id}/${request?.id}")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF832A)
            ),
            elevation = ButtonDefaults
                .buttonElevation(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null)
            Spacer(
                modifier = Modifier
                .width(8.dp))
            Text(stringResource(id = R.string.reschedule))
        }
        if (request?.requestType == "SUBMISSION") {
            Button(
                onClick = {
                    request.task.id.let { taskId ->
                        requestViewModel.updateRequestStatus(taskId, request.id, "APPROVED")
                        taskViewModel.updateTaskStatus(taskId, "DONE")
                        nav.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                elevation = ButtonDefaults
                    .buttonElevation(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null)
                Spacer(
                    modifier = Modifier
                        .width(8.dp))
                Text(stringResource(id = R.string.mark_completed))
            }
        }
        if (request?.requestType == "MODIFICATION") {
            Button(
                onClick = {
                    request.task.id.let { taskId ->
                        requestViewModel.updateRequestStatus(taskId, request.id, "REJECTED")
                        taskViewModel.updateTaskStatus(taskId, "IN_PROGRESS")
                        nav.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252)
                ),
                elevation = ButtonDefaults
                    .buttonElevation(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null)
                Spacer(
                    modifier = Modifier
                        .width(8.dp))
                Text(stringResource(id = R.string.deny))
            }
        }

    }

}