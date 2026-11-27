package com.offlinetrainer.ui

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private data class ToggleFeature(val title: String, val state: MutableState<Boolean>)

class FloatingMenuService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var root: ComposeView
    private lateinit var params: WindowManager.LayoutParams
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (::root.isInitialized) return START_STICKY
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val type = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE
        params = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, type, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.START; x = 24; y = 120; softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE }
        root = ComposeView(this).apply { setContent { FloatingPanel(onClose = { stopSelf() }) } }
        wm.addView(root, params)
        return START_STICKY
    }

    override fun onDestroy() { if (::root.isInitialized) wm.removeView(root); super.onDestroy() }

    @Composable private fun FloatingPanel(onClose: () -> Unit) {
        var minimized by remember { mutableStateOf(false) }
        var x by remember { mutableFloatStateOf(0f) }
        var y by remember { mutableFloatStateOf(0f) }
        var selectedGame by remember { mutableStateOf("Murim Life Simulator") }
        var multiplier by remember { mutableStateOf("2") }
        var adjustable by remember { mutableStateOf("100") }
        var increase by remember { mutableStateOf("1000") }
        val names = listOf("UNLIMITED ALL", "ADS BLOCK", "GOD MODE", "ESP", "INFINITE HEALTH", "INFINITE STAMINA", "UNLIMITED CURRENCY", "UNLOCK ALL")
        val toggles = remember { names.associateWith { mutableStateOf(false) } }
        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp), shadowElevation = 12.dp, modifier = Modifier.width(280.dp)) {
            Column(Modifier.padding(12.dp)) {
                Row(Modifier.fillMaxWidth().pointerInput(Unit) { detectDragGestures { change, drag -> change.consume(); x += drag.x; y += drag.y; params.x = (params.x + drag.x).roundToInt(); params.y = (params.y + drag.y).roundToInt(); wm.updateViewLayout(root, params) } }, verticalAlignment = Alignment.CenterVertically) {
                    Text("OFFLINE TRAINER", modifier = Modifier.weight(1f), fontSize = 14.sp)
                    TextButton(onClick = { minimized = !minimized }) { Text(if (minimized) "OPEN" else "−") }
                    TextButton(onClick = onClose) { Text("×") }
                }
                if (!minimized) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selectedGame) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("Murim Life Simulator", "Ancient Life Simulator", "Generic profile").forEach { game -> DropdownMenuItem(text = { Text(game) }, onClick = { selectedGame = game; expanded = false }) }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(Modifier.heightIn(max = 260.dp)) {
                        items(names) { name -> Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) { Text(name, Modifier.weight(1f), fontSize = 12.sp); Switch(checked = toggles.getValue(name).value, onCheckedChange = { toggles.getValue(name).value = it }) } }
                    }
                    HorizontalDivider()
                    NumberSetting("MULTIPLIER", multiplier) { multiplier = it }
                    NumberSetting("ADJUSTABLE ALL", adjustable) { adjustable = it }
                    NumberSetting("INCREASE / ADDING", increase) { increase = it }
                    Text("UI/configuration prototype only. No game process is modified.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }

    @Composable private fun NumberSetting(label: String, value: String, onValue: (String) -> Unit) {
        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(label, fontSize = 11.sp)
            BasicTextField(value = value, onValueChange = { next -> onValue(next.filter { it.isDigit() || it == '.' }.take(12)) }, singleLine = true, textStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(10.dp))
        }
    }
}
