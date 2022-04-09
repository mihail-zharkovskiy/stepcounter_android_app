package developer.mihailzharkovskiy.stepcounter.ui.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import developer.mihailzharkovskiy.stepcounter.R

abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.color.prozrachniy)
        _binding = initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.width = 350.toDp().toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
