package example.firebase.com.work

import android.content.Context
import android.content.SharedPreferences

object WorkPreferences {

    private lateinit var mPreferences: SharedPreferences

    private const val APP_PREFERENCES = "sharedPreferences"
    private const val URL_KEY = "urlKey"

    fun initPreferences(context: Context) {
        mPreferences = context.getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE)
    }
    fun saveUrl(url: String) {
        mPreferences.edit()
            .putString(URL_KEY, url)
            .apply()
    }
    fun getUrl(): String {
        return mPreferences.getString(URL_KEY, "") ?: ""
    }
}