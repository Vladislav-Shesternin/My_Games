package felcon9.canonical.forester.gumanistically.webView

import android.content.ActivityNotFoundException
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class WebViewChromeClient(private val fragment: WebViewFragment) : WebChromeClient() {

    companion object {
        const val REQUEST_SELECT_FILE = 1000

        var uploadMessage: ValueCallback<Array<Uri>>? = null
    }



    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        uploadMessage = filePathCallback
        val intent = fileChooserParams!!.createIntent()
        try {
            fragment.startActivityForResult(intent, REQUEST_SELECT_FILE)
        } catch (e: ActivityNotFoundException) {
            uploadMessage = null
            return false
        }
        return true
    }

}

