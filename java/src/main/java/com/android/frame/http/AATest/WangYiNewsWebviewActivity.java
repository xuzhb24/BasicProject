package com.android.frame.http.AATest;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import com.android.frame.WebView.CommonWebviewActivity;

/**
 * Create by xuzhb on 2020/1/21
 * Desc
 */
public class WangYiNewsWebviewActivity extends CommonWebviewActivity {

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, WangYiNewsWebviewActivity.class);
        intent.putExtra("EXTRA_TITLE", title);
        intent.putExtra("EXTRA_URL", url);
        context.startActivity(intent);
    }

    @Override
    protected void goPageBack() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
