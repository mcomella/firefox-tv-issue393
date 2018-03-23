package xyz.mcomella.webviewevaljs

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.concurrent.thread

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
                    Thread.sleep(2000)
                    this@MainActivity.runOnUiThread {
                        log("request focus")
                        texty.requestFocus()
                        thread {
                            Thread.sleep(2000)
                            this@MainActivity.runOnUiThread {
                                log("request focus back to WebView")
                                webView.requestFocus()
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


        webView.loadUrl("https://youtube.com/tv")
    }
}

private fun log(str: String) {
    Log.d("lol", str)
}
