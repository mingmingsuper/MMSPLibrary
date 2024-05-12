package com.mmsp.library.web

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mmsp.library.R
import com.mmsp.library.databinding.ActivityWebBrowserBinding
import com.mmsp.library.uikit.BaseActivity
import com.mmsp.library.utils.WidgetUtils

class WebBrowserActivity : BaseActivity<ActivityWebBrowserBinding>() {

    companion object {
        fun browser(context: Context, url: String) {
            val intent = Intent(context, WebBrowserActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_web_browser
    }

    override fun initView() {
        initToolbar(mBinding.toolbarLayout.toolbar)
        WidgetUtils.configWebView(mBinding.webView)
        addWebViewCallback()
        mBinding.webView.loadUrl(intent.getStringExtra("url") ?: "")
    }

    override fun initData() {
    }

    private fun addWebViewCallback() {
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //如果需要处理特殊的结果，可以根据url截取特殊标识做出处理，
                // 如果做出处理后应该返回true，比如发现地址是一个PDF的地址就可以使用PDF Reader打开，
                // 或者是自定义的一个scheme去打开一个特定的Activity也可以在这里处理返回true去跳转特定的Activity
                return false
            }
        }

        mBinding.webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mBinding.webViewProgressbar.progress = newProgress
                if (newProgress == 100) {
                    mBinding.webViewProgressbar.visibility = View.INVISIBLE
                } else {
                    mBinding.webViewProgressbar.visibility = View.VISIBLE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                setTitle(title)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                //隐藏视频全屏
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                //显示全屏视频
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }
        }
    }
}