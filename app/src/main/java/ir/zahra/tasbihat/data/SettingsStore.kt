package ir.zahra.tasbihat.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Persists the two independent audio toggles (music / dhikr) so the app
 * remembers the user's choice the next time it is opened.
 */
class SettingsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var musicEnabled: Boolean
        get() = prefs.getBoolean(KEY_MUSIC, true)
        set(value) = prefs.edit().putBoolean(KEY_MUSIC, value).apply()

    var dhikrEnabled: Boolean
        get() = prefs.getBoolean(KEY_DHIKR, true)
        set(value) = prefs.edit().putBoolean(KEY_DHIKR, value).apply()

    companion object {
        private const val PREFS_NAME = "tasbihat_settings"
        private const val KEY_MUSIC = "music_enabled"
        private const val KEY_DHIKR = "dhikr_enabled"
    }
}
