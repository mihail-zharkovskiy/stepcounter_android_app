package developer.mihailzharkovskiy.stepcounter.ui.screens.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        lifecycleScope.launch {
            delay(100)
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }
    }
}