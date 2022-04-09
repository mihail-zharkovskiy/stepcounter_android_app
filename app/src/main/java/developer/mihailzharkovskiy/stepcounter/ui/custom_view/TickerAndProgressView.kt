package developer.mihailzharkovskiy.stepcounter.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import developer.mihailzharkovskiy.stepcounter.R

class TickerAndProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var progressBar: DonutProgressView
    private var textTicker: TickerView
    private var tvPlaneStep: TextView

    private val section1 =
        DonutSection("section_1", ContextCompat.getColor(context, R.color.heavy_clouds), 1f)

    init {
        inflate(context, R.layout.merge_ticker_and_progress_view, this)
        progressBar = findViewById(R.id.progers_bar_donut_view)
        progressBar.cap = 100f
        progressBar.submitData(listOf(section1))
        textTicker = findViewById(R.id.tv_tiker)
        textTicker.setCharacterLists(TickerUtils.provideNumberList())
        tvPlaneStep = findViewById(R.id.tv_skolko_nuzhno_proyti)
    }

    fun renderParams(progres: Int, steps: String, stepsPlane: String) {
        progressBar.submitData(listOf(section1.copy(amount = progres.toFloat())))
        textTicker.text = steps
        tvPlaneStep.text = context.getString(R.string.plane_shagov_na_den, stepsPlane)
    }

    fun renderEmptyState() {
        textTicker.text = "0"
        progressBar.submitData(listOf(section1))
        tvPlaneStep.text = "0"
    }
}
