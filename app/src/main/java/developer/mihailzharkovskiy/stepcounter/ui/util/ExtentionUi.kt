package developer.mihailzharkovskiy.stepcounter.ui.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast

fun Context.toast(message: String, gravity: Int = Gravity.BOTTOM): Toast {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(gravity, 0, 0)
    toast.show()
    return toast
}

fun Number.toDp() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)
