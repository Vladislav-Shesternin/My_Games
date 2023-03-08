package togle.plinko.mega.sigma.dominicanos

import android.app.Application
import android.content.Context
import togle.plinko.mega.sigma.dominicanos.utils.OneSignalUtil

lateinit var appContext: Context private set

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        OneSignalUtil.initialize()
    }

}