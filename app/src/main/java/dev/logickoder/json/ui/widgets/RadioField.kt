package dev.logickoder.json.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RadioField(
    title: String,
    value: Boolean?,
    error: String?,
    onValueChanged: (Boolean) -> Unit
) {
    Column(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        modifier = Modifier.weight(2f),
                        text = title,
                        style = MaterialTheme.typography.body1,
                    )
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = value == true,
                        onClick = {
                            onValueChanged(true)
                        }
                    )
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = value == false,
                        onClick = {
                            onValueChanged(false)
                        }
                    )
                }
            )
            if (error != null) Text(
                text = error,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error),
            )
        }
    )
}