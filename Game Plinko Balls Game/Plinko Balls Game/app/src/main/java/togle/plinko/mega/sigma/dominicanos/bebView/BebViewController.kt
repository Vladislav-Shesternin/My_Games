package togle.plinko.mega.sigma.dominicanos.bebView

import android.content.Intent
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import togle.plinko.mega.sigma.dominicanos.MainActivity

class BebViewController(
    val activity: MainActivity,
    val bebView: WebView
) {

    companion object {
        val bebUrlFlow = MutableStateFlow<String?>(null)
    }

    private val coroutine = CoroutineScope(Dispatchers.Main)


    fun initialize() {
        bebView.apply {
            with(settings) {
                savePassword = true
                saveFormData = true
                mixedContentMode = 0
                setSupportZoom(false)
                useWideViewPort = true
                allowFileAccess = true
                databaseEnabled = true
                useWideViewPort = true
                setAppCacheEnabled(true)
                domStorageEnabled = true
                javaScriptEnabled = true
                allowContentAccess = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
                allowFileAccessFromFileURLs = true
                cacheMode = WebSettings.LOAD_DEFAULT
                allowUniversalAccessFromFileURLs = true
                javaScriptCanOpenWindowsAutomatically = true

                setEnableSmoothTransition(true)
                pluginState = WebSettings.PluginState.ON
                setRenderPriority(WebSettings.RenderPriority.HIGH)
                userAgentString = "$userAgentString MobileAppClient/Android/0.9"
            }

            isFocusable            = true
            isSaveEnabled          = true
            isFocusableInTouchMode = true

            webChromeClient = BebViewChromeClient(activity)
            webViewClient   = BebViewClient(activity)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            setDownloadListener(getDownloadListener())

            coroutine.launch { bebUrlFlow.collect { it?.let { url -> loadUrl(url) } } }
        }
    }

    private fun getDownloadListener() = DownloadListener { url, _, _, _, _ ->
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        activity.startActivity(intent)
    }

}