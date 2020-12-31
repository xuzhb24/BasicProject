package com.android.frame.http.AATest

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.android.frame.mvc.WebviewActivity

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WangYiNewsWebviewActivity : WebviewActivity() {

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent()
            intent.setClass(context, WangYiNewsWebviewActivity::class.java)
            intent.putExtra("EXTRA_TITLE", title)
            intent.putExtra("EXTRA_URL", url)
            context.startActivity(intent)
        }
    }

    override fun goPageBack() {
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!!.canGoBack()) {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}