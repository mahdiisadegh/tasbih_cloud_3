package ir.zahra.tasbihat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TasbihatColorScheme = lightColorScheme(
    primary = WarmBrownDeep,
    onPrimary = WarmCream,
    secondary = WarmGoldSoft,
    background = WarmCream,
    surface = WarmBeige,
    onBackground = WarmBrownText,
    onSurface = WarmBrownText
)

@Composable
fun TasbihatTheme(
    // The app intentionally ignores system dark mode: a single warm,
    // spiritual palette is part of the design spec.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TasbihatColorScheme,
        typography = TasbihatTypography,
        content = content
    )
}
