package dev.logickoder.json.presentation.confirmation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.logickoder.json.R
import dev.logickoder.json.core.base.BaseListAdapter
import dev.logickoder.json.databinding.FragmentConfirmationBinding
import dev.logickoder.json.utils.viewBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ConfirmationFragment : Fragment(R.layout.fragment_confirmation) {

    private val binding by viewBinding(FragmentConfirmationBinding::bind)
    private val viewModel by viewModels<ConfirmationScreenViewModel>()
    private val adapter = BaseListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirmFc.setOnClickListener {
            if (viewModel.submit()) {
                Dialog(it.context).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(true)
                    setContentView(R.layout.partial_success)
                }.show()
            }
            buildList()
        }
        binding.itemsFc.adapter = adapter
        buildList()
    }

    private fun buildList() {
        val items = buildList {
            viewModel.key?.let { key ->
                add(
                    DropdownView(
                        items = IntRange(0, 10).map { it.toString() },
                        error = key.error,
                        onItemSelected = {
                            viewModel.update(key.data.id, it)
                        }
                    )
                )
            }
            addAll(
                viewModel.data.map { item ->
                    ItemView(
                        item = item,
                        onRadioClicked = {
                            viewModel.update(item.data.id, it.toString())
                        }
                    )
                }
            )
        }
        adapter.submitList(items)
    }
}