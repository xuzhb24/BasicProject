package com.android.frame.camera.zxing.sound

import android.content.Context
import android.media.MediaPlayer
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/11/16
 * Desc:扫描滴滴声
 */
class BeepManager(private var mContext: Context) : MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    companion object {
        private const val BEEP_VOLUME = 0.10f
    }

    private var mMediaPlayer: MediaPlayer? = null
    private var isComplete: Boolean = false

    init {
        mMediaPlayer = MediaPlayer.create(mContext.applicationContext, R.raw.beep)
        with(mMediaPlayer!!) {
            isLooping = false  //是否循环播放
//            setVolume(BEEP_VOLUME, BEEP_VOLUME)
            setOnCompletionListener(this@BeepManager)
            setOnErrorListener(this@BeepManager)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mMediaPlayer?.let {
            it.stop()
            it.release()
        }
        isComplete = true
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mMediaPlayer?.release()
        mMediaPlayer = null
        isComplete = false
        return false
    }

    //播放
    fun startRing() {
        mMediaPlayer?.start()
    }

    //结束和释放资源
    fun releaseRing() {
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}