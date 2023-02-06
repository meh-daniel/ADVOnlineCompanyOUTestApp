package example.redirect.com

import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "" //ключ вансигнал
const val LINK = "http://google.com"

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}