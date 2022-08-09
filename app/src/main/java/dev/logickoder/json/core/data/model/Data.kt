package dev.logickoder.json.core.data.model

import com.google.gson.annotations.SerializedName
import java.util.Collections.emptyList

data class Data(
    @SerializedName("data")
    val items: MutableList<Item> = emptyList(),
    val message: String = "",
    val success: Boolean = false
)

data class Item(
    val description: String = "",
    val id: Int = 0,
    val value: String? = null,
)