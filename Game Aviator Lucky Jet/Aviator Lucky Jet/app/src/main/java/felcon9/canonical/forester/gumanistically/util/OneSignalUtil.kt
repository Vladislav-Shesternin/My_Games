package felcon9.canonical.forester.gumanistically.util

import com.onesignal.OneSignal
import felcon9.canonical.forester.gumanistically.appContext

object OneSignalUtil {

    private const val ONESIGNAL_APP_ID = "7a3363f8-331d-4525-ab72-8b2ddff16c3f"

    fun initialize() {
        log("OneSignal initialize")

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(appContext)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}