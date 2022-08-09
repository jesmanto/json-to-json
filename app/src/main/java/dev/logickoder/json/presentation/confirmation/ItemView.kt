package dev.logickoder.json.presentation.confirmation

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.logickoder.json.R
import dev.logickoder.json.core.base.BaseItem
import dev.logickoder.json.databinding.PartialItemFieldBinding

class ItemView(
    item: ItemDomain,
    val onRadioClicked: (Boolean) -> Unit,
) : BaseItem<ItemDomain, PartialItemFieldBinding>(
    item, R.layout.partial_item_field, item
) {
    override fun inflate(parent: ViewGroup) = PartialItemFieldBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    )

    override fun bind(binding: PartialItemFieldBinding) {
        binding.txtPif.text = item.data.description
        binding.txtErrorPif.text = item.error
        item.data.value?.let {
            binding.groupPif.check(
                if (it.toBooleanStrict()) R.id.radio_yes_pif else R.id.radio_no_pif
            )
        }
        binding.groupPif.setOnCheckedChangeListener { _, checkedId ->
            onRadioClicked(checkedId == R.id.radio_yes_pif)
        }
    }
}