package developer.mihailzharkovskiy.stepcounter.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.ui.util.extensions.toDp

class ProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(5, 100)
            invalidate()
        }

    private var progressColor: Int = ContextCompat.getColor(context, R.color.snow)
    private var progressBkgColor: Int = ContextCompat.getColor(context, R.color.heavy_clouds)
    private var cornerRadius = resources.getDimension(R.dimen.corner_radius_no_custom_view)
    private val padding = 2.toDp()

    private val rectBackground by lazy { RectF(0f, 0f, width.toFloat(), height.toFloat()) }
    private var paint = Paint().apply { Paint.Style.FILL_AND_STROKE }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawProgress(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.color = progressBkgColor
        canvas.drawRoundRect(
            rectBackground,
            cornerRadius,
            cornerRadius,
            paint
        )
    }

    private fun drawProgress(canvas: Canvas) {
        paint.color = progressColor
        val rightX = 0f + ((progress * width) / 100).toFloat()

        canvas.drawRoundRect(
            RectF(padding, padding, rightX - padding, height - padding),
            cornerRadius,
            cornerRadius,
            paint
        )
    }
}