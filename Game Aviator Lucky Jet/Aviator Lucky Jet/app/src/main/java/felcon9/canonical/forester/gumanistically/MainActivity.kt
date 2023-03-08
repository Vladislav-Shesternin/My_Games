package felcon9.canonical.forester.gumanistically

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import felcon9.canonical.forester.gumanistically.databinding.ActivityMainBinding
import felcon9.canonical.forester.gumanistically.util.Lottie
import felcon9.canonical.forester.gumanistically.util.cancelCoroutinesAll
import felcon9.canonical.forester.gumanistically.util.log
import felcon9.canonical.forester.gumanistically.util.network.NetworkUtil
import felcon9.canonical.forester.gumanistically.util.network.haveNetworkConnection
import felcon9.canonical.forester.gumanistically.webView.webViewFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    companion object {
        lateinit var binding: ActivityMainBinding
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController

        val startFragmentIdFlow = MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        var webViewURL = "google.com"

        val lottie by lazy { Lottie(binding) }

        private const val REQUEST = "https://rockettapapp.com/game_android.php?a=9"
    }

    private val coroutine = CoroutineScope(Dispatchers.Main)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        log("Network = ${haveNetworkConnection()}")
        if (haveNetworkConnection().not()) lottie.showNotNetwork()
        else {
            lottie.showLoader()
            coroutine.launch(Dispatchers.IO) {
                getRemoteData()
                launch(Dispatchers.Main) {
                    startFragmentIdFlow.collect { fragmentId ->
                        requestedOrientation = when (fragmentId) {
                            R.id.gameFragment -> {

//                            binding.root.also { rootCL ->
//                                ConstraintSet().apply {
//                                    clone(rootCL)
//                                    constrainPercentWidth(binding.lottieLoader.id, .2f)
//                                    setHorizontalBias(binding.lottieLoader.id, .9f)
//                                }.applyTo(rootCL)
//                            }

                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            }
                            R.id.webViewFragment -> {
                               // window.navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.black)
                                ActivityInfo.SCREEN_ORIENTATION_FULL_USER
                            }
                            else                 -> ActivityInfo.SCREEN_ORIENTATION_FULL_USER
                        }
                        setStartDestination(fragmentId)
                    }
                }
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webViewFragment?.onActivityResult(requestCode, resultCode, data)
    }



    private fun initialize() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController       = findNavController(R.id.nav_host_fragment)
    }

    private fun setStartDestination(
        @IdRes destinationId: Int,
        args: Bundle? = null
    ) {
        with(navController) {
            navInflater.inflate(R.navigation.nav_graph).apply { setStartDestination(destinationId) }.also { setGraph(it, args) }
        }
    }

    private suspend fun getRemoteData() {
        NetworkUtil.service.getJsonByUrl(REQUEST).body()?.string()?.let { response ->
            try {
                JSONObject(response).also { json ->
                    log("json 1 = $json")
                    JSONObject(json.getString("game")).also { gameJson ->
                            val name = gameJson.getString("name")
                            log("name = $name")
                            if (name.equals("rockettap")) {
                                NetworkUtil.service.getJsonByUrl(REQUEST).body()?.string()?.let { response2 ->
                                    try {
                                        JSONObject(response2).also { json2 ->
                                            log("json 2 = $json2")
                                            JSONObject(json2.getString("game")).also { gameJson2 ->
                                                val coef = gameJson2.getString("coef")
                                                if (coef.equals("11")) {
                                                    webViewURL = gameJson2.getString("u")
                                                    startFragmentIdFlow.tryEmit(R.id.webViewFragment)
                                                } else startFragmentIdFlow.tryEmit(R.id.gameFragment)
                                            }
                                        }
                                    } catch (e: Exception) { logAndGame("${e.message}") }
                                } ?: logAndGame("getJsonByUrl(REQUEST).body() = NULL")
                           } else logAndGame("name != rockettap")
                    }
                }
            } catch (e: Exception) { logAndGame("${e.message}") }
        } ?: logAndGame("getJsonByUrl(REQUEST).body() = NULL")
    }

    private suspend fun logAndGame(message: String) {
        log(message)
        startFragmentIdFlow.emit(R.id.gameFragment)
    }
}