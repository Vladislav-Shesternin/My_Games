package togle.plinko.mega.sigma.dominicanos.utils

import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import togle.plinko.mega.sigma.dominicanos.appContext
import java.util.*

object OneSignalUtil {

    private const val ONESIGNAL_APP_ID = "63287695-b9a3-49f5-a1ef-dc63204dad1c"

    fun initialize() {
        log("OneSignal initialize")

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(appContext)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        CoroutineScope(Dispatchers.IO).launch {
            DataStoreManager.UUID.update { uuid ->
                (uuid ?: UUID.randomUUID().toString()).also {
                    log("OneSignal uuid = $it")
                    OneSignal.setExternalUserId(it)
                }
            }
        }
    }

}