package example.firebase.com.work

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.DialogInterface
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import example.firebase.com.work.WorkPreferences.getUrl
import example.firebase.com.databinding.ActivityMainBinding

open class WorkData(
    private val binding: ActivityMainBinding
): WebViewClient() {
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        var message = "Ошибка SSL сертификата."
        if (error!!.primaryError == SslError.SSL_UNTRUSTED) message = "Центру сертификации не доверяют."
        else if (error.primaryError == SslError.SSL_EXPIRED) message = "Срок действия сертификата истек."
        else if (error.primaryError == SslError.SSL_IDMISMATCH) message = "Несоответствие имени хоста сертификата."
        else if (error.primaryError == SslError.SSL_NOTYETVALID) message = "Сертификат еще не действителен."
        message += " Продолжить?"
        AlertDialog
            .Builder(view!!.context)
            .setTitle(message)
            .setTitle("SSL Certificate Error")
            .setMessage(message)
            .setPositiveButton("продолжить") { dialog: DialogInterface?, which: Int -> handler?.proceed() }
            .setNegativeButton("закрыть") { dialog: DialogInterface?, which: Int -> handler?.cancel() }
            .create()
            .show()
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        CookieManager.getInstance().flush()
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        CookieManager.getInstance().flush()
        view?.loadUrl(url.toString())
        return false
    }

    @SuppressLint("NewApi")
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        CookieManager.getInstance().setAcceptThirdPartyCookies(view, true)
        Log.d("workProject", "onPageFinished host of getUrl = ${Uri.parse(getUrl()).host}")
        Log.d("workProject", "onPageFinished host  current url = ${Uri.parse(url).host}")
        if (
            Uri.parse(getUrl()).host == "" ||
            Uri.parse(url).host == "" ||
            Uri.parse(getUrl()).host == "null" ||
            Uri.parse(getUrl()).host == null
        ) {
            return
        }
        if (
            (Uri.parse(getUrl()).host == Uri.parse(url).host) ||
            (Uri.parse(getUrl()).query != Uri.parse(url).query)
        ) {
            binding.webView.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
            binding.splash.visibility = View.GONE
        }
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        CookieManager.getInstance().flush()
    }


}