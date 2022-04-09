package developer.mihailzharkovskiy.stepcounter.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.databinding.MergeTextStepKmKkalViewBinding
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DataForTextView

class TextStepKmKkal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textStep: TextView
    private var textKm: TextView
    private var textKkal: TextView
    private val binding: MergeTextStepKmKkalViewBinding = MergeTextStepKmKkalViewBinding.bind(
        inflate(context, R.layout.merge_text_step_km_kkal_view, this))

    init {
        textStep = binding.tvShagi
        textKm = binding.tvKm
        textKkal = binding.tvKkal
    }

    fun renderPrams(data: DataForTextView) {
        textStep.text = context.getString(R.string.merge_view_steps, data.steps)
        textKm.text = context.getString(R.string.merge_view_km, data.km)
        textKkal.text = context.getString(R.string.merge_view_kkal, data.kkal)
    }

    fun renderEmptyParams() {
        textStep.text = "---"
        textKm.text = "---"
        textKkal.text = "---"
    }
}