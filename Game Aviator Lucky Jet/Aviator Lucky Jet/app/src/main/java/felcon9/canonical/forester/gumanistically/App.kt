package felcon9.canonical.forester.gumanistically

import android.app.Application
import android.content.Context
import felcon9.canonical.forester.gumanistically.util.OneSignalUtil

lateinit var appContext: Context

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        OneSignalUtil.initialize()
    }

}
