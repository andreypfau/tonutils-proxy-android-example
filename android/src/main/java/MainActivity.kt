package io.github.andreypfau.tonutilsproxy.example

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TonUtilsProxy.startProxy(TonUtilsProxy.DEFAULT_PORT).also { result ->
            check(result == "OK")
        }

        setContent {
            UrlContent("http://foundation.ton")
        }
    }
}

object TonUtilsProxy {
    const val DEFAULT_PORT = 8080.toShort()

    init {
        System.loadLibrary("jniproxy")
    }

    external fun startProxy(port: Short): String

    external fun startProxyWithConfig(port: Short, config: String)

    external fun stopProxy()
}

@Composable
fun UrlContent(url: String) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    val host = request.url.host ?: return null
                    if (host.endsWith(".ton") || host.endsWith(".adnl")) {
                        val headers = request.requestHeaders
                        headers["Host"] = host

                        val connection = URL(request.url.toString()).openConnection(
                            Proxy(
                                Proxy.Type.HTTP,
                                java.net.InetSocketAddress(
                                    "127.0.0.1",
                                    TonUtilsProxy.DEFAULT_PORT.toInt()
                                )
                            )
                        ) as HttpURLConnection
                        connection.requestMethod = request.method
                        headers.forEach { (key, value) ->
                            connection.setRequestProperty(key, value)
                        }

                        try {
                            return WebResourceResponse(
                                connection.contentType,
                                connection.contentEncoding,
                                connection.inputStream
                            )
                        } catch (e: IOException) {
                            return WebResourceResponse(
                                connection.contentType,
                                connection.contentEncoding,
                                connection.responseCode,
                                connection.responseMessage,
                                connection.headerFields.mapNotNull {
                                    it.key to (it.value.firstOrNull() ?: return null)
                                }.toMap(),
                                connection.errorStream,
                            )
                        }
                    }

                    return super.shouldInterceptRequest(view, request)
                }
            }
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
        update = {
            it.loadUrl(url)
        }
    )
}
