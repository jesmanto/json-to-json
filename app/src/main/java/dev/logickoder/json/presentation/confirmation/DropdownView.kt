package dev.logickoder.json.presentation.confirmation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import dev.logickoder.json.R
import dev.logickoder.json.core.base.BaseItem
import dev.logickoder.json.databinding.PartialDropdownBinding

class DropdownAdapter<T>(context: Context, private val items: List<T>) :
    ArrayAdapter<T>(context, R.layout.partial_dropdown_item, items) {

    private val noOpFilter = object : Filter() {
        private val noOpResult = FilterResults()
        override fun performFiltering(constraint: CharSequence?) = noOpResult
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
    }

    override fun getFilter() = noOpFilter
}

class DropdownView(
    val items: List<String>,
    val error: String?,
    val onItemSelected: (String) -> Unit,
) : BaseItem<String, PartialDropdownBinding>(
    items.first(), R.layout.partial_dropdown_item, items
) {
    override fun inflate(parent: ViewGroup): PartialDropdownBinding {
        return PartialDropdownBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
    }

    override fun bind(binding: PartialDropdownBinding) {
        (binding.inputPd.editText as? AutoCompleteTextView)?.apply {
            val adapter = DropdownAdapter(binding.root.context, items)
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                onItemSelected(adapter.getItem(position)!!)
            }
            setText(context.getString(R.string.key))
        }
        binding.txtErrorPd.text = error
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is DropdownView) false else other.error == error
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + items.hashCode()
        return result
    }
}