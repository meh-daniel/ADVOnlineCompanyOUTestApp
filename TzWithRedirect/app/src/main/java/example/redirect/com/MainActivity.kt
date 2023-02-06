package example.redirect.com

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import example.redirect.com.work.WorkUtils
import example.redirect.com.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWork()
    }


    private lateinit var binding: ActivityMainBinding

    private fun getWork() {
        lifecycleScope.run {
            if(WorkUtils.checkFirebaseUrl(this@MainActivity.applicationContext, binding)) {
                binding.content.visibility = View.GONE
                binding.splash.visibility = View.GONE
            } else {
                binding.splash.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
            }
        }
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