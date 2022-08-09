package dev.logickoder.json.presentation.confirmation

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.logickoder.json.core.data.model.Data
import dev.logickoder.json.core.data.model.Item
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
    var key: ItemDomain?
        private set
    var data: List<ItemDomain>
        private set

    init {
        val items = Gson().fromJson<Data>(
            getTextFromAsset(app, "data.json"),
            object : TypeToken<Data>() {}.type
        ).items.map { ItemDomain(data = it) }
        key = items.firstOrNull { it.data.description.equals("key", ignoreCase = true) }
        data = items.filterNot { it.data.description.equals("key", ignoreCase = true) }.sortedBy {
            it.data.description
        }
        Log.e("TAG", items.toString())
    }

    fun update(id: Int, value: String) {
        if (id == key?.data?.id) {
            key = key?.copy(
                data = key!!.data.copy(value = value)
            )
        } else {
            val index = data.indexOfFirst { it.data.id == id }
            data = data.mapIndexed { i, itemDomain ->
                if (i == index) {
                    itemDomain.copy(data = itemDomain.data.copy(value = value))
                } else itemDomain
            }
        }
    }

    fun submit(): Boolean {
        data = data.map { itemDomain ->
            itemDomain.copy(
                error = if (itemDomain.data.value == null) {
                    "${itemDomain.data.description} is required"
                } else null
            )
        }
        key = key?.copy(
            error = if (key!!.data.value == null) {
                "${key!!.data.description} is required"
            } else null
        )
        return if (data.any { it.data.value == null } || key?.data?.value == null) {
            false
        } else {
            val result = data.map {
                ResultDomain(it.data.id, it.data.value!!)
            }
            val downloadFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            // save the file in downloads folder
            File(downloadFolder, "result.json").writeText(Gson().toJson(result))
            true
        }
    }
}