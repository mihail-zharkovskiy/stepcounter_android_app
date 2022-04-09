package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import developer.mihailzharkovskiy.stepcounter.databinding.DialogNastroykiBinding
import developer.mihailzharkovskiy.stepcounter.ui.util.BaseDialogFragment
import developer.mihailzharkovskiy.stepcounter.ui.util.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogSettings : BaseDialogFragment<DialogNastroykiBinding>() {

    private val viewModel: DialogSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserData()
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): DialogNastroykiBinding {
        return DialogNastroykiBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    when (state) {
                        is DialogSettingsState.YesData -> {
                            val data = state.data
                            binding.etVes.setText(data.weight)
                            binding.etRost.setText(data.height)
                            binding.etStepPlane.setText(data.stepPlane)
                            dialog?.setCancelable(true)
                        }
                        is DialogSettingsState.NoData -> {
                            requireContext().toast(state.message)
                            dialog?.setCancelable(false)
                        }
                        is DialogSettingsState.InvalidData -> {
                            requireContext().toast(state.message)
                            dialog?.setCancelable(false)
                        }
                        is DialogSettingsState.Close -> {
                            dialog?.cancel()
                        }
                    }
                }
        }

        binding.button.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val rost = binding.etRost.text.toString()
            val ves = binding.etVes.text.toString()
            val stepPlane = binding.etStepPlane.text.toString()
            viewModel.saveUserData(weight = ves, growth = rost, stepPlane = stepPlane)
        }
    }

    companion object {
        private const val TAG = "TAG_NastroykiFragment"
        fun show(manager: FragmentManager) {
            DialogSettings().show(manager, TAG)
        }
    }
}