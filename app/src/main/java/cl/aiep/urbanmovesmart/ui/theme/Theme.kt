package cl.aiep.urbanmovesmart.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Ink = Color(0xFF163642)
val Teal = Color(0xFF146C73)
val Sea = Color(0xFF27A49A)
val Mint = Color(0xFFDFF5EF)
val Lime = Color(0xFFDDF5A1)
val CanvasBg = Color(0xFFF3F8F8)
val Muted = Color(0xFF647C84)

private val palette = lightColorScheme(
    primary = Teal, onPrimary = Color.White, primaryContainer = Mint,
    onPrimaryContainer = Ink, secondary = Sea, background = CanvasBg,
    onBackground = Ink, surface = Color.White, onSurface = Ink,
    surfaceVariant = Color(0xFFE5F0F0), outline = Color(0xFFB6CBCF),
    error = Color(0xFFB93447)
)

@Composable
fun UrbanMoveTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = palette, content = content)
}
