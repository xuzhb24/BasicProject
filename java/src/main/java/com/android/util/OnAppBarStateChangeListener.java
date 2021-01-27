package com.android.util;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by xuzhb on 2021/1/27
 * Desc:监听AppBarLayout的展开与折叠
 */
public abstract class OnAppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    //调用示例
//    appBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangeListener() {
//        @Override
//        public void onStateChanged(AppBarLayout appBarLayout, State state) {
//
//        }
//    });

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

}
