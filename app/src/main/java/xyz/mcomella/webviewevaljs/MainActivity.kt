package xyz.mcomella.webviewevaljs

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (calledOnce) return
                calledOnce = true

                log("onPageFinished")
                thread {
                    Thread.sleep(5000)
                    this@MainActivity.runOnUiThread {
                        log("unfocus webView")
                        texty.requestFocus()
                        thread {
                            Thread.sleep(1000)
                            this@MainActivity.runOnUiThread {
                                webView.evaluateJavascript("console.log('$LOGTAG: ' + document.activeElement);", null)
                                log("request focus back to WebView")
                                webView.requestFocus()
                                webView.evaluateJavascript("console.log('$LOGTAG: ' + document.activeElement);", null)
                                // Whatever DOMElement was focused is no longer focused.
                                // This can be verified with the console log statements or remote
                                // dev tools, i.e. document.activeElement.
                            }
                        }
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
