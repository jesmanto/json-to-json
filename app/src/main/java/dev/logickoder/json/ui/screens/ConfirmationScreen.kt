package dev.logickoder.json.ui.screens

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.logickoder.json.R
import dev.logickoder.json.data.model.Data
import dev.logickoder.json.data.model.Item
import dev.logickoder.json.ui.widgets.DropdownField
import dev.logickoder.json.ui.widgets.RadioField
import dev.logickoder.json.ui.widgets.SuccessDialog
import dev.logickoder.json.utils.getTextFromAsset
import java.io.File

data class ItemDomain(
    val data: Item,
    val error: String? = null
)

data class ResultDomain(
    val id: Int,
    val value: String,
)

class ConfirmationScreenViewModel(val app: Application) : AndroidViewModel(app) {
    val data = mutableStateListOf<ItemDomain>().apply {
        addAll(
            Gson().fromJson<Data>(
                getTextFromAsset(app, "data.json"),
                object : TypeToken<Data>() {}.type
            ).items.map { ItemDomain(data = it) }
        )
    }

    fun update(id: Int, value: String) {
        val index = data.indexOfFirst { it.data.id == id }
        data[index] = data[index].copy(data = data[index].data.copy(value = value))
    }

    fun submit(): Boolean {
        for (i in 0 until data.size) {
            data[i] = data[i].copy(
                error = if (data[i].data.value == null) "${data[i].data.description} is required" else null
            )
        }
        return if (data.any { it.data.value == null }) {
            false
        } else {
            val result = data.map {
                ResultDomain(it.data.id, it.data.value!!)
            }
            val downloadFolder =
                File(app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString())
            Log.e("TAG", "submit: $downloadFolder")
            // save the file in downloads folder
            File(downloadFolder, "result.json").writeText(Gson().toJson(result))
            true
        }
    }
}

@Composable
fun ConfirmationScreen(viewModel: ConfirmationScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val suggestions = remember {
        IntRange(0, 10).toList()
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    val padding = 24.dp

    if (showDialog) SuccessDialog {
        showDialog = false
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = padding),
        content = {
            Spacer(modifier = Modifier.height(padding))
            viewModel.data.forEach { item ->
                if (item.data.description.equals("key", ignoreCase = true)) {
                    DropdownField(
                        modifier = Modifier.fillMaxWidth(),
                        label = item.data.description,
                        suggested = item.data.value?.toInt() ?: 0,
                        suggestions = suggestions,
                        onSuggestionSelected = {
                            viewModel.update(item.data.id, it.toString())
                        },
                    )
                } else {
                    RadioField(
                        title = item.data.description,
                        value = item.data.value?.toBooleanStrictOrNull(),
                        error = item.error,
                        onValueChanged = {
                            viewModel.update(item.data.id, it.toString())
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(padding))
            Row(
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.fillMaxWidth(.4f),
                        onClick = {
                            if (viewModel.submit()) {
                                showDialog = true
                            }
                        },
                        content = {
                            Text(stringResource(id = R.string.confirm))
                        }
                    )
                }
            )
        }
    )
}