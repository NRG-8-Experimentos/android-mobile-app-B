package com.example.synhub.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import nrg.inc.synhub.ui.theme.*

@Composable fun cPrimary()         = MaterialTheme.colorScheme.primary
@Composable fun cSecondary()       = MaterialTheme.colorScheme.secondary
@Composable fun cBG()              = MaterialTheme.colorScheme.background
@Composable fun cSurface()         = MaterialTheme.colorScheme.surface
@Composable fun cCard()            = MaterialTheme.colorScheme.surfaceVariant
@Composable fun cOnSurface()       = MaterialTheme.colorScheme.onSurface
@Composable fun cOnPrimary()       = MaterialTheme.colorScheme.onPrimary
@Composable fun cOutline()         = MaterialTheme.colorScheme.outline

@Composable fun cTextPrimary()     = MaterialTheme.colorScheme.onSurface
@Composable fun cTextMuted(): Color =
    if (isSystemInDarkTheme()) Color(0xFF9CA3AF) else Color.Gray

val cGreen   = Accent_Green
val cAmber   = Accent_Amber
val cOrange  = Accent_Orange
val cRed     = Accent_Red
val cInfoBlu = Accent_InfoBlue
