package bagus2x.sosmed.presentation.common.components

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RequiresPermission(
    modifier: Modifier = Modifier,
    permissions: List<String>,
    deniedContent: (@Composable () -> Unit)? = null,
    grantedContent: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var resultState by remember { mutableStateOf<Map<String, Boolean>?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        resultState = result
    }

    val allGranted = if (resultState != null) {
        resultState!!.values.all { it }
    } else {
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    AnimatedContent(targetState = allGranted) { granted ->
        if (granted) {
            grantedContent()
        } else {
            if (deniedContent != null) {
                deniedContent()
            } else {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { launcher.launch(permissions.toTypedArray()) }) {
                        Text(text = "Request Permissions")
                    }
                }
            }
        }
    }
}
