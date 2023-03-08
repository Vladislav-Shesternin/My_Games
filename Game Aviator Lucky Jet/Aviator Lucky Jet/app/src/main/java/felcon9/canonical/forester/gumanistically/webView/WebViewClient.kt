package felcon9.canonical.forester.gumanistically.webView

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import felcon9.canonical.forester.gumanistically.MainActivity
import felcon9.canonical.forester.gumanistically.R

class WebViewClient(val context: Context): WebViewClient() {

    private lateinit var intent: Intent

    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        when {
            url.startsWith("mailto") -> {
                intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                intent.putExtra(Intent.EXTRA_EMAIL, url.replace("mailto:", ""))
                context.startActivity(Intent.createChooser(intent, "Send email"))
            }
            url.startsWith("tel:") -> {
                intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
            url.startsWith("https://diia.app") -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.data?.authority.toString()
                view?.context?.startActivity(intent)
            }
            url.startsWith("https://t.me/joinchat") -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view?.context?.startActivity(intent)
            }

            url.startsWith("tg:") -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view?.context?.startActivity(intent)
            }



            Uri.parse(url).host == "localhost" -> {
                MainActivity.navController.navigate(R.id.gameFragment)
            }


            else -> if (url.startsWith("http://") || url.startsWith("https://")) return false
        }
        return true
    }

}