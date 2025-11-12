package com.example.synhub.analytics.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.groups.viewmodel.GroupViewModel
import com.example.synhub.groups.viewmodel.MemberViewModel
import com.example.synhub.analytics.viewmodel.AnalyticsState
import com.example.synhub.analytics.viewmodel.AnalyticsViewModel
import com.example.synhub.shared.components.SlideMenu
import com.example.synhub.shared.components.TopBar
import kotlinx.coroutines.delay
import com.example.synhub.groups.model.response.MembersWebService
import com.example.synhub.shared.model.client.RetrofitClient
import com.example.synhub.tasks.application.dto.TaskResponse
import kotlinx.coroutines.launch
import com.example.synhub.analytics.model.response.AnalyticsWebService
import nrg.inc.synhub.R

// Colores adaptados de Tasks
private val BluePrimary = Color(0xFF1A4E85)
private val CardBackground = Color(0xFFF5F5F5)
private val CardHeaderBackground = Color(0xFF1A4E85)
private val CardHeaderText = Color.White
private val AccentOrange = Color(0xFFFF9800)
private val AccentRed = Color(0xFFF44336)
private val AccentBlue = Color(0xFF4A90E2)
private val AccentGreen = Color(0xFF4CAF50)
private val AccentYellow = Color(0xFFFDD634)
private val AccentPending = Color(0xFFFF832A)
private val CardBorder = Color(0xFFE0E0E0)
private val CardLight = Color(0xFFFFFFFF)

@Composable
fun FriendlyNames(): Map<String, String> {
    return mapOf(
        "IN_PROGRESS" to stringResource(id = R.string.in_progress),
        "COMPLETED" to stringResource(id = R.string.completed),
        "total" to stringResource(id = R.string.total),
        "rescheduled" to stringResource(id = R.string.reschedule),
        "completedTasks" to stringResource(id = R.string.completed_tasks),
        "taskCount" to stringResource(id = R.string.completed_tasks_amount)
    )
}

@Composable
fun getFriendlyName(key: String): String {
    val friendlyNames = FriendlyNames()
    return friendlyNames[key] ?: key.replaceFirstChar { it.uppercase() }
}

fun formatDetailValue(value: Any?): String =
    when (value) {
        is Double -> if (value % 1.0 == 0.0) value.toInt().toString() else "%.2f".format(value)
        is Float -> if (value % 1.0f == 0.0f) value.toInt().toString() else "%.2f".format(value)
        else -> value?.toString() ?: ""
    }

@Composable
fun formatDuration(ms: Long?): String {
    if (ms == null || ms <= 0) return stringResource(id = R.string.no_time_registered)
    val seconds = ms / 1000
    val days = seconds / (24 * 3600)
    val hours = (seconds % (24 * 3600)) / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    val parts = mutableListOf<String>()
    if (days > 0) parts.add("$days día${if (days > 1) "s" else ""}")
    if (hours > 0) parts.add("$hours h")
    if (minutes > 0) parts.add("$minutes min")
    if (secs > 0 || parts.isEmpty()) parts.add("$secs s")
    return parts.joinToString(" ")
}

@Composable
fun formatDaysToDuration(days: Double?): String {
    if (days == null || days <= 0.0) return stringResource(id = R.string.no_time_registered)
    val ms = (days * 24 * 60 * 60 * 1000).toLong()
    return formatDuration(ms)
}

fun formatSummary(summary: String?): String {
    return ""
}

@Composable
fun MetricCard(
    title: String,
    content: String,
    additionalContent: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 6.dp,
        color = CardBackground
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = title,
                color = BluePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                color = CardBorder,
                thickness = 1.dp
            )
            Text(
                text = content,
                color = Color(0xFF333333),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            additionalContent?.let {
                Spacer(modifier = Modifier.padding(top = 8.dp))
                it()
            }
        }
    }
}

@Composable
fun EnhancedBarChart(distribution: Map<String, Any>?) {
    if (distribution.isNullOrEmpty()) return

    val parsed = distribution.mapNotNull { (_, v) ->
        if (v is Map<*, *>) {
            val name = v["memberName"]?.toString() ?: return@mapNotNull null
            val count = (v["taskCount"] as? Number)?.toInt() ?: 0
            name to count
        } else null
    }

    if (parsed.isEmpty()) return

    val max = parsed.maxOf { it.second }.takeIf { it > 0 } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(Color(0xFFF7F7F7), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        parsed.forEach { (member, count) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = member,
                    color = BluePrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(80.dp)
                )
                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .weight(1f)
                        .background(AccentYellow, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = count / max.toFloat())
                            .height(18.dp)
                            .background(AccentBlue, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$count " + stringResource(id = R.string.lc_tasks),
                    color = AccentBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun TaskTimesList(memberId: Long?) {
    val memberApi = RetrofitClient.membersWebService as MembersWebService
    var tasks by remember { mutableStateOf<List<TaskResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(memberId) {
        loading = true
        tasks = if (memberId != null) {
            val response = memberApi.getMemberTasks(memberId)
            (response.body() as? List<*>)?.filterIsInstance<TaskResponse>() ?: emptyList()
        } else emptyList()
        loading = false
    }

    if (loading) {
        Text(stringResource(id = R.string.loading_tasks), color = Color.Gray, fontSize = 14.sp)
        return
    }

    if (tasks.isEmpty()) {
        Text(stringResource(id = R.string.no_available), color = Color.Gray, fontSize = 14.sp)
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tasks.forEach { task ->
            val taskId = task.id
            val title = task.title
            val status = task.status
            var timePassed by remember { mutableStateOf<Long?>(null) }

            LaunchedEffect(taskId, status) {
                if (taskId != null) {
                    while (true) {
                        val analyticsApi = RetrofitClient.analyticsWebService as com.example.synhub.analytics.model.response.AnalyticsWebService
                        val resp = analyticsApi.getTaskTimePassed(taskId)
                        timePassed = resp.body()?.timePassed
                        if (status != "IN_PROGRESS") break
                        delay(1000)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDuration(timePassed),
                    color = AccentBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: @Composable (() -> Unit)? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
    ) {
        icon?.let {
            Box(modifier = Modifier.padding(end = 8.dp)) { it() }
        }
        Text(
            text = title,
            color = BluePrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

fun formatIntValue(value: Any?): String =
    when (value) {
        is Number -> value.toInt().toString()
        else -> value?.toString() ?: ""
    }

@Composable
fun MetricRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) BluePrimary else Color(0xFF333333),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) BluePrimary else Color(0xFF333333),
            fontSize = 16.sp
        )
    }
}

@Composable
fun AnalyticsOverviewSection(analyticsState: AnalyticsState) {
    val overview = analyticsState.taskOverview
    val completed = formatIntValue(overview?.details?.get("COMPLETED"))
    val inProgress = formatIntValue(overview?.details?.get("IN_PROGRESS"))
    val total = overview?.details?.values
        ?.filterIsInstance<Number>()
        ?.sumOf { it.toInt() }
        ?.toString() ?: "0"
    val rescheduled = formatIntValue(analyticsState.rescheduledTasks?.details?.get("rescheduled"))

    val completedTasksValue = overview?.details?.get("completedTasks")
        ?: analyticsState.rescheduledTasks?.details?.get("completedTasks")
    val completedTasks = formatIntValue(completedTasksValue)

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardLight, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SectionTitle(stringResource(id = R.string.general_summary), icon = {
                Icon(Icons.Filled.Info, contentDescription = null, tint = BluePrimary)
            })
            MetricRow(stringResource(id = R.string.completed_tasks), completed, highlight = true)
            MetricRow(stringResource(id = R.string.pending_tasks), inProgress)
            MetricRow(stringResource(id = R.string.total_tasks), total)
            Spacer(modifier = Modifier.height(8.dp))
            MetricRow(stringResource(id = R.string.reprogramed_tasks), rescheduled)
        }
    }
}

@Composable
fun AnalyticsDistributionSection(
    analyticsState: AnalyticsState,
    members: List<com.example.synhub.groups.application.dto.MemberResponse> = emptyList()
) {
    val dist = analyticsState.taskDistribution?.details
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardLight, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SectionTitle(stringResource(id = R.string.tasks_distribution), icon = {
                Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = BluePrimary)
            })
            if (dist.isNullOrEmpty()) {
                Text(stringResource(id = R.string.no_available), color = Color.Gray)
            } else {
                val parsed = dist.mapNotNull { (_, v) ->
                    if (v is Map<*, *>) {
                        val name = v["memberName"]?.toString() ?: return@mapNotNull null
                        val count = (v["taskCount"] as? Number)?.toInt() ?: 0
                        name to count
                    } else null
                }
                if (parsed.isEmpty()) return@Column
                val max = parsed.maxOf { it.second }.takeIf { it > 0 } ?: 1

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(CardLight, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    parsed.forEach { (memberName, count) ->
                        val member = members.find { "${it.name} ${it.surname}" == memberName }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            if (member != null && !member.imgUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = member.imgUrl,
                                    contentDescription = "Foto de $memberName",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = memberName,
                                color = BluePrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(80.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .height(18.dp)
                                    .weight(1f)
                                    .background(AccentYellow, RoundedCornerShape(4.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = count / max.toFloat())
                                        .height(18.dp)
                                        .background(AccentBlue, RoundedCornerShape(4.dp))
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "$count "+ stringResource(id = R.string.lc_tasks),
                                color = AccentBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsCompletionTimeSection(
    analyticsState: AnalyticsState,
    members: List<com.example.synhub.groups.application.dto.MemberResponse> = emptyList()
) {
    val avg = analyticsState.avgCompletionTime
    val avgDays = avg?.value
    val formatted = formatDaysToDuration(avgDays)

    val analyticsApi = RetrofitClient.analyticsWebService as AnalyticsWebService
    val coroutineScope = rememberCoroutineScope()
    var memberAvgTimes by remember { mutableStateOf<Map<Long, Double?>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(members) {
        loading = true
        val times = mutableMapOf<Long, Double?>()
        members.forEach { member ->
            coroutineScope.launch {
                try {
                    // Cambia la llamada al endpoint correcto para obtener el promedio por miembro
                    val resp = analyticsApi.getAvgCompletionTimeForMember(member.id)
                    times[member.id] = resp.body()?.value
                } catch (_: Exception) {
                    times[member.id] = null
                }
                memberAvgTimes = times.toMap()
            }
        }
        loading = false
    }

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardLight, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SectionTitle(stringResource(id = R.string.analytics_completion_time_section_title) + ":", icon = {
                Icon(Icons.Filled.DateRange, contentDescription = null, tint = BluePrimary)
            })
            if (formatted.isNotBlank() && formatted != stringResource(id = R.string.no_time_registered)) {
                MetricRow(stringResource(id = R.string.analytics_avg_completion_time_label) + ":", formatted, highlight = true)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(stringResource(id = R.string.analytics_avg_time_per_member_label) + ":", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            if (loading && members.isNotEmpty()) {
                Text(stringResource(id = R.string.loading_time_member) + "...", color = Color.Gray, fontSize = 14.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    members.forEach { member ->
                        val name = "${member.name} ${member.surname}"
                        val avgDaysMember = memberAvgTimes[member.id]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!member.imgUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = member.imgUrl,
                                    contentDescription = "Foto de $name",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = name,
                                fontSize = 15.sp,
                                color = Color(0xFF333333),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatDaysToDuration(avgDaysMember),
                                fontWeight = FontWeight.Medium,
                                color = AccentBlue,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsRescheduledSection(
    analyticsState: AnalyticsState,
    members: List<com.example.synhub.groups.application.dto.MemberResponse> = emptyList()
) {
    val rescheduled = analyticsState.rescheduledTasks
    val totalRescheduled = (rescheduled?.details?.get("rescheduled") as? Number)?.toInt() ?: 0
    val total = formatIntValue(rescheduled?.details?.get("total"))
    val rescheduledMemberIds = rescheduled?.rescheduledMemberIds ?: emptyList()

    val memberCount = rescheduledMemberIds.size.takeIf { it > 0 } ?: 1
    val perMember = if (memberCount > 0) totalRescheduled / memberCount else 0
    val remainder = if (memberCount > 0) totalRescheduled % memberCount else 0

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardLight, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SectionTitle(stringResource(id = R.string.reprogramed_tasks), icon = {
                Icon(Icons.Filled.Build, contentDescription = null, tint = BluePrimary)
            })
            MetricRow(stringResource(id = R.string.total_reprogramed) + ":", totalRescheduled.toString(), highlight = true)
            MetricRow(stringResource(id = R.string.total_tasks) + ":", total)
            Spacer(modifier = Modifier.height(12.dp))
            Text(stringResource(id = R.string.members_who_reprogram) + ":", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            if (rescheduledMemberIds.isEmpty()) {
                Text(stringResource(id = R.string.no_members_reprogramed), color = Color.Gray, fontSize = 14.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    members.filter { it.id in rescheduledMemberIds }.forEachIndexed { idx, member ->
                        val name = "${member.name} ${member.surname}"
                        val count = perMember + if (idx < remainder) 1 else 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!member.imgUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = member.imgUrl,
                                    contentDescription = "Foto de $name",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = name,
                                fontSize = 15.sp,
                                color = Color(0xFF333333),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$count",
                                fontWeight = FontWeight.Medium,
                                color = AccentOrange,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.Build,
                                contentDescription = "Reprogramó tarea",
                                tint = AccentOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsAndReports(
    nav: NavHostController,
    token: String,
    name: String = "",
    surname: String = "",
    imgUrl: String = "",
    analyticsViewModel: AnalyticsViewModel = viewModel(),
    groupViewModel: GroupViewModel = viewModel(),
    memberViewModel: MemberViewModel = viewModel()
) {
    val haveGroup by groupViewModel.haveGroup.collectAsState()
    val group by groupViewModel.group.collectAsState()
    val members by memberViewModel.members.collectAsState()
    val haveMembers by memberViewModel.haveMembers.collectAsState()
    val analyticsState by analyticsViewModel.analyticsState.collectAsState()
    val loading by analyticsViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        groupViewModel.fetchLeaderGroup()
        memberViewModel.fetchGroupMembers()
        analyticsViewModel.fetchAnalyticsData(token)
    }

    val slideMenuState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = slideMenuState,
        drawerContent = {
            ModalDrawerSheet {
                SlideMenu(
                    nav,
                    name = name,
                    surname = surname,
                    imgUrl = imgUrl
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White), // Cambiado a fondo blanco
            containerColor = Color.White, // Cambiado a fondo blanco
            topBar = {
                TopBar(
                    function = {
                        nav.popBackStack()
                    },
                    stringResource(id = R.string.statistics_title),
                    Icons.AutoMirrored.Filled.ArrowBack
                )
            },
            content = { innerPadding ->
                if (loading) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item { MetricCard(title = stringResource(id = R.string.loading_metrics) + "...", content =  stringResource(id = R.string.please_wait) + "...") }
                    }
                } else if (!haveGroup) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item { MetricCard(title = stringResource(id = R.string.no_group), content = stringResource(id = R.string.no_group)) }
                    }
                } else if (!haveMembers) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item { MetricCard(title = stringResource(id = R.string.no_members), content = stringResource(id = R.string.no_members)) }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item {
                            AnalyticsOverviewSection(analyticsState)
                        }
                        item {
                            AnalyticsDistributionSection(analyticsState, members)
                        }
                        item {
                            AnalyticsCompletionTimeSection(analyticsState, members)
                        }
                        item {
                            AnalyticsRescheduledSection(analyticsState, members)
                        }
                    }
                }
            }
        )
    }
}
