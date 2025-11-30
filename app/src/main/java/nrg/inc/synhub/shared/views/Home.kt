package com.example.synhub.shared.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.groups.viewmodel.GroupViewModel
import com.example.synhub.groups.views.NoGroup
import com.example.synhub.groups.views.NoMembers
import com.example.synhub.invitations.viewmodel.InvitationViewModel
import com.example.synhub.invitations.views.NoInvitations
import com.example.synhub.requests.viewModel.RequestViewModel
import com.example.synhub.requests.views.NoRequests
import com.example.synhub.shared.components.SlideMenu
import com.example.synhub.shared.components.TopBar
import com.example.synhub.shared.viewmodel.HomeViewModel
import com.example.synhub.tasks.viewmodel.TaskViewModel
import com.example.synhub.tasks.views.NoTasks
import kotlinx.coroutines.launch
import nrg.inc.synhub.R
import com.example.synhub.shared.theme.*
import androidx.compose.material3.MaterialTheme

@Composable
fun Home(nav: NavHostController, homeViewModel: HomeViewModel = HomeViewModel()) {

    LaunchedEffect(Unit) {
        homeViewModel.fetchLeaderDetails()
    }

    val leader by homeViewModel.leader.collectAsState()

    val slideMenuState =rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = slideMenuState,
        drawerContent = {
            ModalDrawerSheet {
                SlideMenu(nav, leader?.name ?: "", leader?.surname ?: "", leader?.imgUrl ?: "")
            }
        }
    ) {
        Scaffold (
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    function = {
                        scope.launch {
                            slideMenuState.apply {
                                if(isClosed)
                                    open()
                                else
                                    close()
                            }
                        }
                    },
                    stringResource(id = R.string.main_title),
                    Icons.Default.Menu
                )
            }
        ){
                innerPadding -> HomeScreen(modifier = Modifier.padding(innerPadding),
            nav)
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier, nav: NavHostController) {
    val groupViewModel: GroupViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()
    val invitationViewModel: InvitationViewModel = viewModel()
    val requestViewModel: RequestViewModel = viewModel()

    LaunchedEffect(Unit) {
        groupViewModel.fetchLeaderGroup()
        groupViewModel.fetchGroupMembers()
        taskViewModel.fetchGroupTasks()
        invitationViewModel.fetchGroupInvitations()
        requestViewModel.fetchGroupRequests()
    }

    val group by groupViewModel.group.collectAsState()
    val haveGroup by groupViewModel.haveGroup.collectAsState()
    val members by groupViewModel.members.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()
    val invitations by invitationViewModel.groupInvitations.collectAsState()
    val requests by requestViewModel.requests.collectAsState()
    val visibleRequests = requests.filter { it.requestStatus == "PENDING" }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 120.dp),
    ){
        if(haveGroup) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ){
                item{
                    Column{
                            Text(
                                text = stringResource(id = R.string.members_title) + ":",
                                fontSize = 20.sp,
                                color = Color(0xFF1A4E85),
                                fontWeight = FontWeight.Bold
                            )
                            HorizontalDivider(
                                color = Color(0xFF1A4E85),
                                thickness = 3.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(
                                    containerColor = cCard()
                                ),
                                elevation = CardDefaults.cardElevation(5.dp),
                                onClick = {
                                    nav.navigate("Group/Members")
                                }
                            ){
                                if(members.isEmpty()) {
                                    NoMembers(nav, group?.code ?: "")
                                }else{
                                    LazyColumn (
                                        verticalArrangement = Arrangement.spacedBy(20.dp),
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .heightIn(max = 300.dp)
                                    ){
                                        items(members) {
                                                member ->
                                            Card (
                                                colors = cardColors(
                                                    containerColor = Color(0xFF1A4E85)
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ){
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .shadow(
                                                                elevation = 5.dp,
                                                                shape = CircleShape,
                                                                clip = true
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        AsyncImage(
                                                            model = member.imgUrl,
                                                            contentDescription = null,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .matchParentSize()
                                                                .clip(CircleShape)
                                                        )
                                                    }
                                                    Column(
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text(
                                                            text = "${member.name} ${member.surname}",
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.White
                                                        )
                                                        Text(
                                                            text = member.username,
                                                            fontSize = 14.sp,
                                                            color = Color(0xFFD4D4D4)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
                item {
                    Column {
                        Text(
                            text = stringResource(id = R.string.tasks_title) + ":",
                            fontSize = 20.sp,
                            color = Color(0xFF1A4E85),
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            color = Color(0xFF1A4E85),
                            thickness = 3.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = cardColors(
                                containerColor = cCard()
                            ),
                            elevation = CardDefaults.cardElevation(5.dp),
                            onClick = {
                                nav.navigate("Tasks")
                            }
                        ){
                            if(tasks.isEmpty()){
                                NoTasks()
                            }else{
                                LazyColumn (
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .heightIn(max = 300.dp)
                                ){
                                    items(tasks) { task ->
                                        Card (
                                            colors = cardColors(
                                                containerColor = Color(0xFF1A4E85)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ){
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {
                                                Text(
                                                    text = "${task.member.name} ${task.member.surname}",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = cSurface()
                                                )
                                                HorizontalDivider(color = Color.White, thickness = 2.dp)
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Card (
                                                    colors = cardColors(
                                                        containerColor = cSurface()
                                                    ),
                                                ){
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(10.dp)
                                                    ){
                                                        Text(
                                                            text = task.title,
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                        Text(
                                                            text = task.description,
                                                            fontSize = 16.sp,
                                                        )
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                item {
                    Column{
                        Text(
                            text = stringResource(id = R.string.requests_validations_title) + ":",
                            fontSize = 20.sp,
                            color = Color(0xFF1A4E85),
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            color = Color(0xFF1A4E85),
                            thickness = 3.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = cardColors(
                                containerColor = cCard()
                            ),
                            elevation = CardDefaults.cardElevation(5.dp),
                            onClick = {
                                nav.navigate("GroupRequests")
                            }
                        ){
                            if (visibleRequests.isEmpty()) {
                                NoRequests()
                            } else {
                                LazyColumn (
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .heightIn(max = 200.dp)
                                ){
                                    items(requests) { request ->
                                        Card(
                                            colors = cardColors(
                                                containerColor = Color(0xFF1A4E85)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {
                                                Text("${request.task.member.name} ${request.task.member.surname}",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Card(
                                                    colors = cardColors(
                                                        containerColor = cSurface()
                                                    ),
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(10.dp)
                                                    ){
                                                        Text(
                                                            text = request.task.title,
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                        Text(
                                                            text = "  ${request.description}",
                                                            fontSize = 16.sp,
                                                        )
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Column{
                        Text(
                            text = stringResource(id = R.string.join_requests_title) + ":",
                            fontSize = 20.sp,
                            color = Color(0xFF1A4E85),
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            color = Color(0xFF1A4E85),
                            thickness = 3.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = cardColors(
                                containerColor = cCard()
                            ),
                            elevation = CardDefaults.cardElevation(5.dp),
                            onClick = {
                                nav.navigate("Group/Invitations")
                            }
                        ){
                            if(invitations.isEmpty()){
                                NoInvitations()
                            }else{
                                LazyColumn (
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .heightIn(max = 300.dp)
                                ){
                                    items(invitations) { invitation ->
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
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
                                                    containerColor = Color(0xFF1A4E85)
                                                ),
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(
                                                        10.dp
                                                    )
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .shadow(
                                                                elevation = 5.dp,
                                                                shape = CircleShape,
                                                                clip = true
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        AsyncImage(
                                                            model = invitation.member.imgUrl,
                                                            contentDescription = null,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .matchParentSize()
                                                                .clip(CircleShape)
                                                        )
                                                    }
                                                    Column(
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text(
                                                            text = "${invitation.member.name} ${invitation.member.surname}",
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.White
                                                        )
                                                        Text(
                                                            text = invitation.member.username,
                                                            fontSize = 14.sp,
                                                            color = Color(0xFFD4D4D4)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        } else {
            NoGroup(nav)
        }
    }
}