package nrg.inc.synhub.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary      = Dark_Primary,
    secondary    = Dark_Secondary,
    background   = Dark_Background,
    surface      = Dark_Surface,
    surfaceVariant = Dark_SurfaceVar,
    onPrimary    = Dark_OnPrimary,
    onBackground = Dark_OnBackground,
    onSurface    = Dark_OnSurface,
    outline      = Dark_Outline,
)

private val LightColorScheme = lightColorScheme(
    primary      = Light_Primary,
    secondary    = Light_Secondary,
    background   = Light_Background,
    surface      = Light_Surface,
    surfaceVariant = Light_SurfaceVar,
    onPrimary    = Light_OnPrimary,
    onBackground = Light_OnBackground,
    onSurface    = Light_OnSurface,
    outline      = Light_Outline,
)

@Composable
fun SynhubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
