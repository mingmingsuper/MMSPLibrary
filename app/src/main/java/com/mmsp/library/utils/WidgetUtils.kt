package com.mmsp.library.utils

import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

class WidgetUtils {
    companion object {

        fun configWebView(webView: WebView?) {
            webView?.settings?.apply {
                cacheMode = WebSettings.LOAD_NO_CACHE
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                databaseEnabled = true
                setSupportZoom(true)
                useWideViewPort = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                allowUniversalAccessFromFileURLs = true
            }
        }
    }
}