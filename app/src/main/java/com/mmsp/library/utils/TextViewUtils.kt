package com.mmsp.library.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import com.mmsp.library.web.WebBrowserActivity
import java.util.Locale

/**
 * TextView 功能扩展
 *
 */
class TextViewKit {

    companion object {

        /**
         * 识别TextView的text中的链接并处理点击事件
         *
         * @param context
         * @param textview
         */
        fun interceptHyperlink(context: Context, textview: TextView) {
            textview.movementMethod = LinkMovementMethod.getInstance()
            val text = textview.text
            if (text is Spannable) {
                val end = text.length
                val spannable = textview.text as Spannable
                val urlSpans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
                if (urlSpans.isEmpty()) {
                    return
                }
                val spannableStringBuilder = SpannableStringBuilder()
                for (uri in urlSpans) {
                    val url: String = uri.url
                    if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0) {
                        val customUrlSpan = CustomUrlSpan(context, url)
                        spannableStringBuilder.setSpan(
                            customUrlSpan,
                            spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                textview.text = spannableStringBuilder
            }
        }

    }
}

class CustomUrlSpan(private val context: Context, private val url: String) :
    ClickableSpan() {
    override fun onClick(widget: View) {
        if (url.lowercase(Locale.getDefault()).endsWith(".pdf")) {
            //TODO:在这里可以处理查看PDF的操作
            return
        }
        WebBrowserActivity.browser(context, url)
    }
}