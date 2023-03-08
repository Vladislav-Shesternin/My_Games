package felcon9.canonical.forester.gumanistically.webView

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import felcon9.canonical.forester.gumanistically.MainActivity
import felcon9.canonical.forester.gumanistically.databinding.FragmentWebviewBinding

@SuppressLint("StaticFieldLeak")
var webViewFragment: WebViewFragment? = null

class WebViewFragment : Fragment() {

    lateinit var binding: FragmentWebviewBinding
    lateinit var webView: WebView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewFragment = this
        onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWebviewBinding.inflate(inflater)

        webView = binding.webView.apply {
            with(settings) {
                savePassword = true
                saveFormData = true
                mixedContentMode = 0
                setSupportZoom(false)
                useWideViewPort = true
                allowFileAccess = true
                databaseEnabled = true
                useWideViewPort = true
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

            isFocusable = true
            isSaveEnabled = true
            isFocusableInTouchMode = true

            webChromeClient = WebViewChromeClient(this@WebViewFragment)
            webViewClient = WebViewClient(requireContext())

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            setDownloadListener(getDownloadListener())
            MainActivity.lottie.hideLoader()
            loadUrl(MainActivity.webViewURL)

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WebViewChromeClient.REQUEST_SELECT_FILE) {
            if (WebViewChromeClient.uploadMessage == null) return
            WebViewChromeClient.uploadMessage!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            WebViewChromeClient.uploadMessage = null
        }
    }



    private fun getDownloadListener() = DownloadListener { url, _, _, _, _ ->
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        requireActivity().startActivity(intent)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) webView.goBack()
        }
    }

}