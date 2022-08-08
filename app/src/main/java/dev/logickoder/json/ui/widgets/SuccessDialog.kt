package dev.logickoder.json.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.logickoder.json.R

@Composable
fun SuccessDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Column(
                modifier = Modifier.background(Color.White).padding(48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Image(
                        modifier = Modifier.fillMaxWidth(.5f),
                        imageVector = Icons.Default.Check,
                        colorFilter = ColorFilter.tint(Color.Green),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(id = R.string.submitted),
                        style = MaterialTheme.typography.body2,
                        fontStyle = FontStyle.Italic,
                    )
                }
            )
        }
    )
}