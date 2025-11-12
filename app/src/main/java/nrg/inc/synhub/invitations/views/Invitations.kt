package com.example.synhub.invitations.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.synhub.invitations.viewmodel.InvitationViewModel
import com.example.synhub.shared.components.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nrg.inc.synhub.R

@Composable
fun Invitations(nav: NavHostController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopBar(
                function = {
                    nav.navigate("Home")
                },
                stringResource(id = R.string.join_requests_title),
                Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ){
            innerPadding -> InvitationsScreen(modifier = Modifier.padding(innerPadding),
        nav)
    }
}

@Composable
fun InvitationsScreen(modifier: Modifier, nav: NavHostController) {
    val invitationsViewmodel: InvitationViewModel = viewModel()
    val invitations by invitationsViewmodel.groupInvitations.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        invitationsViewmodel.fetchGroupInvitations()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = 20.dp)
    ){
        if (invitations.isNotEmpty()) {
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                items(invitations) { invitation ->
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
                            containerColor = Color(0xFFF5F5F5)
                        ),
                    ){
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(10.dp)
                        ){
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
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ){
                                Row (
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ){
                                    ElevatedButton(
                                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier,
                                        onClick = {
                                            coroutineScope.launch {
                                                invitationsViewmodel.processInvitation(invitation.id, true)
                                                delay(200)
                                                invitationsViewmodel.fetchGroupInvitations()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(id = R.string.accept), fontSize = 15.sp,
                                            color = Color.White, fontWeight = FontWeight.Bold
                                        )

                                    }
                                    ElevatedButton(
                                        colors = ButtonDefaults.buttonColors(Color(0xFFF44336)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier,
                                        onClick = {
                                            coroutineScope.launch {
                                                invitationsViewmodel.processInvitation(invitation.id, false)
                                                delay(200)
                                                invitationsViewmodel.fetchGroupInvitations()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(id = R.string.reject), fontSize = 15.sp,
                                            color = Color.White, fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            NoInvitations()
        }
    }
}
@Composable
fun NoInvitations(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp))
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(
                Color(0xFF1A4E85),
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
                    text = stringResource(id = R.string.no_invitations),
                    fontSize = 20.sp,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}