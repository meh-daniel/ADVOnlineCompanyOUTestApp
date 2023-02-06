package example.firebase.com.work

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import example.firebase.com.databinding.ActivityMainBinding
import example.firebase.com.work.WorkCheckes.checkIsEmu
import example.firebase.com.work.WorkPreferences.getUrl
import example.firebase.com.work.WorkPreferences.saveUrl

object WorkUtils {

    private fun setWebView(
        binding: ActivityMainBinding,
        url: String,
    ) {
        with(binding.webView) {
            settings.apply {
                javaScriptEnabled = true
                allowContentAccess = true
                mixedContentMode = 0
                domStorageEnabled = true
                useWideViewPort = true
                databaseEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                cacheMode = WebSettings.LOAD_DEFAULT
            }
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
                    this.goBack() // Navigate back to previous web page if there is one
                    this.scrollTo(0, 0) // Scroll webview back to top of previous page
                }
                true
            }
            webViewClient = object : WorkData(
                binding
            ) {}
            webChromeClient = object : WebChromeClient() {}
            loadUrl(url)
        }
    }

    fun isSimSupport(context: Context) = TelephonyManager.SIM_STATE_ABSENT !=
            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simState


    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun checkFirebaseUrl(
        context: Context,
        binding: ActivityMainBinding
    ): Boolean {
        return if (
            WorkCheckes.isNetworkAvailable(context)
            && checkIsEmu()
            && isSimSupport(context)
        ) {
            val savedUrl = getUrl()
            Log.d("workProject", "savedUrl ${savedUrl}")
            if (savedUrl.isEmpty()) {
                var webUrl = ""
                val remoteConfig = Firebase.remoteConfig.apply {
                    setConfigSettingsAsync(remoteConfigSettings {
                        fetchTimeoutInSeconds = 60
                    })
                }
                remoteConfig.fetchAndActivate().addOnSuccessListener {
                    webUrl = remoteConfig.getString("url")
                    Log.d("workProject", "get remote url ${webUrl}")
                }
                android.os.Handler().postDelayed({
                    if(webUrl.isNotEmpty()) {
                        saveUrl(webUrl)
                        setWebView(binding, webUrl)
                        true
                    } else {
                        binding.webView.visibility = View.GONE
                        binding.content.visibility = View.VISIBLE
                        binding.splash.visibility = View.GONE
                        false
                    }
                }, 3600)
            } else {
                setWebView(binding, savedUrl)
                true
            }
        } else false
    }

}
