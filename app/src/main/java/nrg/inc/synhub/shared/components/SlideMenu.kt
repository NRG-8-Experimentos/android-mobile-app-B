package com.example.synhub.shared.components

import nrg.inc.synhub.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.synhub.shared.icons.groupSVG
import com.example.synhub.shared.icons.invitationSVG
import com.example.synhub.shared.icons.logoutSVG
import com.example.synhub.shared.icons.membersSVG
import com.example.synhub.shared.icons.reportsSVG
import com.example.synhub.shared.icons.requestSVG
import com.example.synhub.shared.icons.tasksSVG
import com.example.synhub.shared.model.client.RetrofitClient
import nrg.inc.synhub.shared.components.LanguageSwitchingButtons

@Composable
fun SlideMenu(nav:NavHostController, name: String, surname: String, imgUrl: String) {
    var gap = 15.dp

    Column (
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF1A4E85)),
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Spacer(modifier = Modifier.height(gap))

        Text(
            text= "$name $surname",
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(gap))

        Box(
            modifier = Modifier
                .size(180.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = CircleShape,
                    clip = true
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(gap))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(gap))

        Column (
            verticalArrangement = Arrangement.spacedBy(gap)
        ){
            NavigationDrawerItem(
                icon = {
                    Icon(
                        groupSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.group_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("Group")
                }
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        invitationSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.join_requests_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("Group/Invitations")
                }
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        membersSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.members_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("Group/Members")
                }
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        tasksSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.tasks_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("Tasks")
                }
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        reportsSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.statistics_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("AnalyticsAndReports")
                }
            )

            // TODO: Add a condition check if user is leader or member.

            NavigationDrawerItem(
                icon = {
                    Icon(
                        requestSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.requests_validations_title),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("GroupRequests")
                }
            )
            Spacer(modifier = Modifier.height(gap))
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(gap))

        LanguageSwitchingButtons()

        Spacer(modifier = Modifier.height(gap))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(gap))

        Column (
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            NavigationDrawerItem(
                icon = {
                    Icon(
                        logoutSVG,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.log_out),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White)
                },
                selected = false,
                onClick = {
                    nav.navigate("Login") {
                        popUpTo("Login") { inclusive = true }
                    }
                    RetrofitClient.resetToken()
                }
            )
        }
    }
}