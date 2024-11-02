package ma.ensa.project

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val logo = findViewById<ImageView>(R.id.splash_logo)

        val scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1.5f)
        val scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1.5f)
        scaleX.duration = 3000
        scaleY.duration = 3000

        val translationY = ObjectAnimator.ofFloat(logo, "translationY", 1000f)
        translationY.duration = 2000

        val fadeOut = ObjectAnimator.ofFloat(logo, "alpha", 0f)
        fadeOut.duration = 6000


        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleX, scaleY, translationY, fadeOut)
        animatorSet.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)

    }
}