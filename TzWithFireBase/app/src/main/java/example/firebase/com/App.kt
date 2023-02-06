package example.firebase.com

import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "" //ключ вансигнал

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}