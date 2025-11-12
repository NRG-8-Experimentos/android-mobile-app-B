package com.example.synhub.groups.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.synhub.groups.viewmodel.GroupViewModel
import com.example.synhub.groups.application.dto.GroupMember
import com.example.synhub.shared.components.TopBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nrg.inc.synhub.R

@Composable
fun Group(nav: NavHostController) {
    val groupViewModel: GroupViewModel = viewModel()
    val group by groupViewModel.group.collectAsState()
    LaunchedEffect(Unit) {
        groupViewModel.fetchLeaderGroup()
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopBar(
                function = {
                    nav.navigate("Home")
                },
                group?.name ?: stringResource(id = R.string.group_title),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> GroupScreen(modifier = Modifier.padding(innerPadding),
        nav)
    }
}

@Composable
fun GroupScreen(modifier: Modifier, nav: NavHostController) {
    val groupViewModel: GroupViewModel = viewModel()

    val group by groupViewModel.group.collectAsState()
    val haveGroup by groupViewModel.haveGroup.collectAsState()
    val members by groupViewModel.members.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    val memberToDelete = remember { mutableStateOf<GroupMember?>(null) } // Reemplaza 'membersType' por el tipo real de tus miembros

    LaunchedEffect(Unit) {
        groupViewModel.fetchLeaderGroup()
        groupViewModel.fetchGroupMembers()
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp)
                .padding(horizontal = 15.dp)
        ) {
            if(!haveGroup){
                NoGroup(nav)
            } else {
                Column (
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = cardColors(containerColor = Color(0xFF4A90E2)),
                                modifier = Modifier
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        clip = true
                                    ),
                            ) {
                                Text(
                                    text = ("#" + group?.code),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Box(
                                modifier = Modifier.width(10.dp)
                            )
                            val clipboardManager = LocalClipboardManager.current
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(group?.code ?: ""))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = stringResource(id = R.string.group_copy_code_description),
                                    tint = Color(0xFF4A90E2)
                                )
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = cardColors(containerColor = Color(0xFF1A4E85))
                    ) {
                        Text(
                            text = group?.description ?: "",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Column (
                    modifier = Modifier.padding(bottom = 26.dp),
                ){
                    Text(
                        stringResource(id = R.string.members_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A4E85),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        color = Color(0xFF1A4E85),
                        thickness = 1.dp
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(10.dp),
                                clip = true
                            ),
                        shape = RoundedCornerShape(10.dp),
                        colors = cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = cardColors(containerColor = Color.White)
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(5.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(members) { member ->
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
                                        Column (
                                            modifier = Modifier.weight(1f)
                                        ){
                                            Text(
                                                text = "${member.name} ${member.surname}",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = member.username,
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                memberToDelete.value = member
                                                showDialog.value = true
                                            }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDialog.value && memberToDelete.value != null) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(stringResource(id = R.string.confirm_delete), fontWeight = FontWeight.Bold) },
                text = { Text(stringResource(id = R.string.member_details_confirm_delete_member_text, memberToDelete.value!!.name, memberToDelete.value!!.surname, memberToDelete.value!!.username)) },
                confirmButton = {
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                        onClick = {
                        coroutineScope.launch {
                            groupViewModel.deleteGroupMember(memberToDelete.value!!.id)
                            delay(200)
                            groupViewModel.fetchGroupMembers()
                            showDialog.value = false
                            memberToDelete.value = null
                        }
                    }) {
                        Text(stringResource(id = R.string.delete), color = Color.White)
                    }
                },
                dismissButton = {
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(Color(0xFF1A4E85)),
                        onClick = {
                        showDialog.value = false
                        memberToDelete.value = null
                    }) {
                        Text(stringResource(id = R.string.cancel), color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
fun NoGroup(nav: NavHostController){

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(top = 10.dp)
                .background(
                    Color(0xFF1A4E85),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_group),
                    fontSize = 25.sp,
                    color = Color(0xFFFFFFFF)
                )
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(Color(0xFF4A90E2)),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        nav.navigate("Group/CreateGroup")
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.create_group), fontSize = 20.sp,
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}