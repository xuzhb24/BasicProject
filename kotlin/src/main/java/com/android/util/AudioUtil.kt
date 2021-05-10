package com.android.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.base.BaseApplication

/**
 * Created by xuzhb on 2021/5/6
 * Desc:音频管理工具类，调整音量和控制响铃模式
 * 声音分类
 * AudioManager.STREAM_VOICE_CALL：通话声音
 * AudioManager.STREAM_SYSTEM：系统声音，包括按键声音等
 * AudioManager.STREAM_RING：来电响铃
 * AudioManager.STREAM_MUSIC：媒体声音(包括音乐、视频、游戏声音)
 * AudioManager.STREAM_ALARM：闹钟声音
 * AudioManager.STREAM_NOTIFICATION：通知声音
 * 声音模式分类
 * AudioManager.RINGER_MODE_NORMAL：正常模式，所有声音都正常，包括系统声音、来电响铃、媒体声音、闹钟、通知声音都有
 * AudioManager.RINGER_MODE_SILENT：静音模式，该模式下，来电响铃、通知、系统声音和振动都没有，闹钟、通话声音保持，大部分手机媒体声音依然有，
 * 但是小米和少部分oppo手机在设置静音的同时会将媒体声音自动调整为0，此时没有媒体声音
 * AudioManager.RINGER_MODE_VIBRATE：振动模式，该模式下，来电、通知保持振动没有声音，但是媒体、闹钟依然有声音，不过小米手机只有正常模式和静音模式，没有振动模式
 * 需要权限<uses-permission android:name="android.permission.ACCESS_NOTIFICATION_POLICY" />
 */
object AudioUtil {

    /**
     * 获取指定声音流最大音量大小
     *
     * @param streamType 流类型
     *                   AudioManager.STREAM_VOICE_CALL：通话声音
     *                   AudioManager.STREAM_SYSTEM：系统声音，包括按键声音等
     *                   AudioManager.STREAM_RING：来电响铃
     *                   AudioManager.STREAM_MUSIC：媒体声音(包括音乐、视频、游戏声音)
     *                   AudioManager.STREAM_ALARM：闹钟声音
     *                   AudioManager.STREAM_NOTIFICATION：通知声音
     */
    fun getStreamMaxVolume(streamType: Int): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.getStreamMaxVolume(streamType)
            }
        }
        return 0
    }

    /**
     * 获取指定声音流最小音量大小
     *
     * @param streamType 流类型
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun getStreamMinVolume(streamType: Int): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.getStreamMinVolume(streamType)
            }
        }
        return 0
    }

    /**
     * 获取指定声音流音量大小
     *
     * @param streamType 流类型
     */
    fun getStreamVolume(streamType: Int): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.getStreamVolume(streamType)
            }
        }
        return 0
    }

    /**
     * 设置指定声音流音量大小
     *
     * @param streamType 流类型
     * @param index      音量大小
     */
    fun setStreamVolume(streamType: Int, index: Int): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.setStreamVolume(streamType, index, 0)
                return true
            }
        }
        return false
    }

    /**
     * 控制手机音量, 调小一个单位
     */
    fun adjustVolumeLower() = adjustVolume(AudioManager.ADJUST_LOWER)

    /**
     * 控制手机音量, 调大一个单位
     */
    fun adjustVolumeRaise() = adjustVolume(AudioManager.ADJUST_RAISE)

    /**
     * 控制手机音量, 调大或者调小一个单位
     *
     * @param direction 音量方向(调大、调小)
     *                  AudioManager.ADJUST_LOWER 可调小一个单位
     *                  AudioManager.ADJUST_RAISE 可调大一个单位
     */
    fun adjustVolume(direction: Int): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.adjustVolume(direction, 0)
                return true
            }
        }
        return false
    }

    /**
     * 控制指定声音流音量, 调小一个单位
     *
     * @param streamType 流类型
     */
    fun adjustStreamVolumeLower(streamType: Int) =
        adjustStreamVolume(streamType, AudioManager.ADJUST_LOWER)

    /**
     * 控制指定声音流音量, 调大一个单位
     *
     * @param streamType 流类型
     */
    fun adjustStreamVolumeRaise(streamType: Int) =
        adjustStreamVolume(streamType, AudioManager.ADJUST_RAISE)

    /**
     * 控制指定声音流音量, 调大或者调小一个单位
     *
     * @param streamType 流类型
     * @param direction  音量方向(调大、调小)
     *                   AudioManager.ADJUST_LOWER 可调小一个单位
     *                   AudioManager.ADJUST_RAISE 可调大一个单位
     */
    fun adjustStreamVolume(streamType: Int, direction: Int): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.adjustStreamVolume(streamType, direction, 0)
                return true
            }
        }
        return false
    }

    /**
     * 设置媒体声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteByMusic(state: Boolean) =
        setStreamMute(AudioManager.STREAM_MUSIC, state)

    /**
     * 设置通话声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteByVoiceCall(state: Boolean) =
        setStreamMute(AudioManager.STREAM_VOICE_CALL, state)

    /**
     * 设置系统声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteBySystem(state: Boolean) =
        setStreamMute(AudioManager.STREAM_SYSTEM, state)

    /**
     * 设置来电响铃静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteByRing(state: Boolean) =
        setStreamMute(AudioManager.STREAM_RING, state)

    /**
     * 设置闹钟声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteByAlarm(state: Boolean) =
        setStreamMute(AudioManager.STREAM_ALARM, state)

    /**
     * 设置通知声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    fun setStreamMuteByNotification(state: Boolean) =
        setStreamMute(AudioManager.STREAM_NOTIFICATION, state)

    /**
     * 设置指定声音流静音状态
     *
     * @param streamType 流类型
     * @param state      true：静音，false：非静音
     */
    fun setStreamMute(streamType: Int, state: Boolean): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.setStreamMute(streamType, state)
                return true
            }
        }
        return false
    }

    /**
     * 获取当前的音频模式，返回值有下述几种模式:
     * AudioManager.MODE_NORMAL：普通
     * AudioManager.MODE_RINGTONE：铃声
     * AudioManager.MODE_IN_CALL：打电话
     * AudioManager.MODE_IN_COMMUNICATION：通话
     */
    fun getMode(): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.mode
            }
        }
        return AudioManager.MODE_NORMAL
    }

    /**
     * 设置当前的音频模式
     *
     * @param mode 音频模式
     *             AudioManager.MODE_NORMAL：普通
     *             AudioManager.MODE_RINGTONE：铃声
     *             AudioManager.MODE_IN_CALL：打电话
     *             AudioManager.MODE_IN_COMMUNICATION：通话
     */
    fun setMode(mode: Int): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.mode = mode
                return true
            }
        }
        return false
    }

    /**
     * 获取当前的铃声模式
     * 返回值有下述几种模式:
     * AudioManager.RINGER_MODE_NORMAL：普通
     * AudioManager.RINGER_MODE_SILENT：静音
     * AudioManager.RINGER_MODE_VIBRATE：振动
     */
    fun getRingerMode(): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.ringerMode
            }
        }
        return AudioManager.RINGER_MODE_NORMAL
    }

    /**
     * 设置静音模式(静音，且无振动)
     */
    fun ringerSilent() = setRingerMode(AudioManager.RINGER_MODE_SILENT)

    /**
     * 设置振动模式(静音，但有振动)
     */
    fun ringerVibrate() = setRingerMode(AudioManager.RINGER_MODE_VIBRATE)

    /**
     * 设置正常模式(正常声音，振动开关由setVibrateSetting决定)
     */
    fun ringerNormal() = setRingerMode(AudioManager.RINGER_MODE_NORMAL)

    /**
     * 设置当前的铃声模式
     *
     * @param ringerMode 铃声模式
     *                   {@link AudioManager#RINGER_MODE_NORMAL}：普通
     *                   {@link AudioManager#RINGER_MODE_SILENT}：静音
     *                   {@link AudioManager#RINGER_MODE_VIBRATE}：振动
     * @param setting    如果没授权, 是否跳转到设置页面
     */
    fun setRingerMode(ringerMode: Int, setting: Boolean = true): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                if (isDoNotDisturb(BaseApplication.instance, setting)) {
                    it.ringerMode = ringerMode
                    return true
                }
            }
        }
        return false
    }

    /**
     * 判断是否授权Do not disturb权限，授权Do not disturb权限, 才可进行音量操作
     *
     * @param setting 如果没授权, 是否跳转到设置页面
     */
    fun isDoNotDisturb(context: Context, setting: Boolean): Boolean {
        kotlin.runCatching {
            val notificationManager =
                context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted) {
                if (setting) {
                    val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            } else {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否打开扬声器
     */
    fun isSpeakerphoneOn(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isSpeakerphoneOn
            }
        }
        return false
    }

    /**
     * 设置是否打开扬声器
     *
     * @param on 是否打开
     */
    fun setSpeakerphoneOn(on: Boolean): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.isSpeakerphoneOn = on
                return true
            }
        }
        return false
    }

    /**
     * 判断麦克风是否静音
     */
    fun isMicrophoneMute(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isMicrophoneMute
            }
        }
        return false
    }

    /**
     * 设置是否让麦克风静音
     *
     * @param on 是否打开
     */
    fun setMicrophoneMute(on: Boolean): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.isMicrophoneMute = on
                return true
            }
        }
        return false
    }

    /**
     * 判断是否有音乐处于活跃状态
     */
    fun isMusicActive(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isMusicActive
            }
        }
        return false
    }

    /**
     * 判断是否插入了耳机
     */
    fun isWiredHeadsetOn(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isWiredHeadsetOn
            }
        }
        return false
    }

    /**
     * 判断蓝牙A2DP音频外设是否已连接
     */
    fun isBluetoothA2dpOn(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isBluetoothA2dpOn
            }
        }
        return false
    }

    /**
     * 检查当前平台是否支持使用SCO的关闭调用用例
     */
    fun isBluetoothScoAvailableOffCall(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isBluetoothScoAvailableOffCall
            }
        }
        return false
    }

    /**
     * 检查通信是否使用蓝牙SCO
     */
    fun isBluetoothScoOn(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.isBluetoothScoOn
            }
        }
        return false
    }

    /**
     * 设置是否使用蓝牙SCO耳机进行通讯
     *
     * @param on 是否打开
     */
    fun setBluetoothScoOn(on: Boolean): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.isBluetoothScoOn = on
                return true
            }
        }
        return false
    }

    /**
     * 启动蓝牙SCO音频连接
     */
    fun startBluetoothSco(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.startBluetoothSco()
                return true
            }
        }
        return false
    }

    /**
     * 停止蓝牙 SCO 音频连接
     */
    fun stopBluetoothSco(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.stopBluetoothSco()
                return true
            }
        }
        return false
    }

    /**
     * 加载音效
     */
    fun loadSoundEffects(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.loadSoundEffects()
                return true
            }
        }
        return false
    }

    /**
     * 卸载音效
     */
    fun unloadSoundEffects(): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.unloadSoundEffects()
                return true
            }
        }
        return false
    }

    /**
     * 播放音效
     *
     * @param effectType {@link AudioManager#FX_KEY_CLICK},
     *                   {@link AudioManager#FX_FOCUS_NAVIGATION_UP},
     *                   {@link AudioManager#FX_FOCUS_NAVIGATION_DOWN},
     *                   {@link AudioManager#FX_FOCUS_NAVIGATION_LEFT},
     *                   {@link AudioManager#FX_FOCUS_NAVIGATION_RIGHT},
     *                   {@link AudioManager#FX_KEYPRESS_STANDARD},
     *                   {@link AudioManager#FX_KEYPRESS_SPACEBAR},
     *                   {@link AudioManager#FX_KEYPRESS_DELETE},
     *                   {@link AudioManager#FX_KEYPRESS_RETURN},
     *                   {@link AudioManager#FX_KEYPRESS_INVALID},
     * @param volume     音量大小
     */
    fun playSoundEffect(effectType: Int, volume: Float): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.playSoundEffect(effectType, volume)
                return true
            }
        }
        return false
    }

    /**
     * 放弃音频焦点，使上一个焦点所有者(如果有)接收焦点
     *
     * @param listener 焦点监听事件
     */
    fun abandonAudioFocus(listener: AudioManager.OnAudioFocusChangeListener): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.abandonAudioFocus(listener)
                return true
            }
        }
        return false
    }

    /**
     * 调整最相关的流的音量，或者给定的回退流
     *
     * @param direction 调整参数
     */
    fun adjustSuggestedStreamVolume(direction: Int): Boolean {
        getAudioManager()?.let {
            kotlin.runCatching {
                it.adjustSuggestedStreamVolume(direction, AudioManager.USE_DEFAULT_STREAM_TYPE, 0)
                return true
            }
        }
        return false
    }

    /**
     * 获取音频硬件指定key的参数值
     *
     * @param keys Key
     */
    fun getParameters(keys: String): String? {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.getParameters(keys)
            }
        }
        return null
    }

    /**
     * 获取用户对振动类型的振动设置
     *
     * @param vibrateType 振动类型
     *                    {@link AudioManager#VIBRATE_TYPE_NOTIFICATION}
     *                    {@link AudioManager#VIBRATE_TYPE_RINGER}
     * @return {@link AudioManager#VIBRATE_SETTING_ON}, {@link AudioManager#VIBRATE_SETTING_OFF}, {@link AudioManager#VIBRATE_SETTING_ONLY_SILENT}
     */
    fun getVibrateSetting(vibrateType: Int): Int {
        getAudioManager()?.let {
            kotlin.runCatching {
                return it.getVibrateSetting(vibrateType)
            }
        }
        return -1
    }

    fun getAudioManager(): AudioManager? {
        return BaseApplication.instance.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    }

}