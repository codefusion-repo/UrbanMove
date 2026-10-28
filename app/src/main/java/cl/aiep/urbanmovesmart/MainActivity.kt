package cl.aiep.urbanmovesmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import cl.aiep.urbanmovesmart.ui.UrbanMoveApp
import cl.aiep.urbanmovesmart.ui.theme.UrbanMoveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { UrbanMoveTheme { UrbanMoveApp() } }
    }
}
