package com.offlinetrainer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.offlinetrainer.ui.FloatingMenuService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("OFFLINE TRAINER", style = MaterialTheme.typography.headlineMedium)
                        Text("Floating overlay controller", style = MaterialTheme.typography.bodyLarge)
                        Text("This build provides the floating control panel and configuration only. It does not inject into or modify another app's process.")
                        Button(onClick = {
                            if (!Settings.canDrawOverlays(this@MainActivity)) {
                                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                            } else startService(Intent(this@MainActivity, FloatingMenuService::class.java))
                        }) { Text("Enable / Launch Floating Menu") }
                        Text("Game profiles: Murim Life Simulator and Ancient Life Simulator. Actual game modifications require a verified, game-specific mod interface or source-level integration.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
