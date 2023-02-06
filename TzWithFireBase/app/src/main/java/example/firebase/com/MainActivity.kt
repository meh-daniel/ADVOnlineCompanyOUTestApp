package example.firebase.com

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import example.firebase.com.databinding.ActivityMainBinding
import example.firebase.com.work.WorkPreferences
import example.firebase.com.work.WorkUtils

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWork()
    }


    private lateinit var binding: ActivityMainBinding

    private fun getWork() {
        WorkPreferences.initPreferences(this@MainActivity)
        if(WorkUtils.checkFirebaseUrl(this@MainActivity.applicationContext, binding)) {
            binding.content.visibility = View.GONE
            binding.splash.visibility = View.GONE
        } else {
            binding.splash.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        }
        android.os.Handler().postDelayed({
            binding.splash.visibility = View.GONE
        }, 30000)
    }

    override fun onBackPressed() {

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        binding.webView.saveState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedInstanceState?.let {
            binding.webView.restoreState(it)
        }
    }

}