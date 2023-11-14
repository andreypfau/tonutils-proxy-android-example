package org.ton.browser.app

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TonUtilsProxy

        setContent {
            UrlContent("http://foundation.ton")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        TonUtilsProxy.stopProxy()
    }
}

object TonUtilsProxy {
    const val DEFAULT_PORT = 9876.toShort()

    init {
        println("begin")
        System.loadLibrary("jniproxy")
        println("end")
    }

    external fun startProxy(port: Short)

    external fun startProxyWithConfig(port: Short, config: String)

    external fun stopProxy()
}

@Composable
fun UrlContent(url: String) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
        update = {
            if (url.startsWith("http://foundation.ton")) {
                val remappedUrl = "http://localhost:${TonUtilsProxy.DEFAULT_PORT}/" + url.substringAfter("http://foundation.ton")
                val headers = mapOf(
                    "Host" to "foundation.ton",
                )
                it.loadUrl(remappedUrl, headers)
            } else {
                it.loadUrl(url)
            }
        }
    )
}
