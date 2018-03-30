package xyz.mcomella.webviewevaljs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

private const val LOGTAG = "lol"

class MainActivity : AppCompatActivity() {

    var calledOnce = false

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (calledOnce) return
                calledOnce = true

                log("onPageFinished")
                webView.evaluateJavascript("document.getElementsByTagName('a')[25].focus();", null)
                log("unfocus webView")
                texty.requestFocus()
                thread {
                    Thread.sleep(1000)
                    this@MainActivity.runOnUiThread {
                        webView.evaluateJavascript("console.log('$LOGTAG: ' + document.activeElement);", null)
                        log("request focus back to WebView")
                        webView.requestFocus()
                        webView.evaluateJavascript("console.log('$LOGTAG: ' + document.activeElement);", null)
                    }
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
        }

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        webView.loadUrl("https://google.com")
//        webView.loadUrl("https://youtube.com/tv")
    }
}

private fun log(str: String) {
    Log.d(LOGTAG, str)
}
