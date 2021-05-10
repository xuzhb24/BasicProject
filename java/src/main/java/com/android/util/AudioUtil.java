package com.android.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.base.BaseApplication;

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
public class AudioUtil {

    /**
     * 获取指定声音流最大音量大小
     *
     * @param streamType 流类型
     *                   {@link AudioManager#STREAM_VOICE_CALL}：通话声音
     *                   {@link AudioManager#STREAM_SYSTEM}：系统声音，包括按键声音等
     *                   {@link AudioManager#STREAM_RING}：来电响铃
     *                   {@link AudioManager#STREAM_MUSIC}：媒体声音(包括音乐、视频、游戏声音)
     *                   {@link AudioManager#STREAM_ALARM}：闹钟声音
     *                   {@link AudioManager#STREAM_NOTIFICATION}：通知声音
     */
    public static int getStreamMaxVolume(int streamType) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getStreamMaxVolume(streamType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取指定声音流最小音量大小
     *
     * @param streamType 流类型
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static int getStreamMinVolume(int streamType) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getStreamMinVolume(streamType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取指定声音流音量大小
     *
     * @param streamType 流类型
     */
    public static int getStreamVolume(int streamType) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getStreamVolume(streamType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 设置指定声音流音量大小
     *
     * @param streamType 流类型
     * @param index      音量大小
     */
    public static boolean setStreamVolume(int streamType, int index) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setStreamVolume(streamType, index, 0);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 控制手机音量, 调小一个单位
     */
    public static boolean adjustVolumeLower() {
        return adjustVolume(AudioManager.ADJUST_LOWER);
    }

    /**
     * 控制手机音量, 调大一个单位
     */
    public static boolean adjustVolumeRaise() {
        return adjustVolume(AudioManager.ADJUST_RAISE);
    }

    /**
     * 控制手机音量, 调大或者调小一个单位
     *
     * @param direction 音量方向(调大、调小)
     *                  AudioManager.ADJUST_LOWER 可调小一个单位
     *                  AudioManager.ADJUST_RAISE 可调大一个单位
     */
    public static boolean adjustVolume(int direction) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.adjustVolume(direction, 0);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 控制指定声音流音量, 调小一个单位
     *
     * @param streamType 流类型
     */
    public static boolean adjustStreamVolumeLower(int streamType) {
        return adjustStreamVolume(streamType, AudioManager.ADJUST_LOWER);
    }

    /**
     * 控制指定声音流音量, 调大一个单位
     *
     * @param streamType 流类型
     */
    public static boolean adjustStreamVolumeRaise(int streamType) {
        return adjustStreamVolume(streamType, AudioManager.ADJUST_RAISE);
    }

    /**
     * 控制指定声音流音量, 调大或者调小一个单位
     *
     * @param streamType 流类型
     * @param direction  音量方向(调大、调小)
     *                   AudioManager.ADJUST_LOWER 可调小一个单位
     *                   AudioManager.ADJUST_RAISE 可调大一个单位
     */
    public static boolean adjustStreamVolume(int streamType, int direction) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.adjustStreamVolume(streamType, direction, 0);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置媒体声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteByMusic(boolean state) {
        return setStreamMute(AudioManager.STREAM_MUSIC, state);
    }

    /**
     * 设置通话声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteByVoiceCall(boolean state) {
        return setStreamMute(AudioManager.STREAM_VOICE_CALL, state);
    }

    /**
     * 设置系统声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteBySystem(boolean state) {
        return setStreamMute(AudioManager.STREAM_SYSTEM, state);
    }

    /**
     * 设置来电响铃静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteByRing(boolean state) {
        return setStreamMute(AudioManager.STREAM_RING, state);
    }

    /**
     * 设置闹钟声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteByAlarm(boolean state) {
        return setStreamMute(AudioManager.STREAM_ALARM, state);
    }

    /**
     * 设置通知声音静音状态
     *
     * @param state true：静音，false：非静音
     */
    public static boolean setStreamMuteByNotification(boolean state) {
        return setStreamMute(AudioManager.STREAM_NOTIFICATION, state);
    }

    /**
     * 设置指定声音流静音状态
     *
     * @param streamType 流类型
     * @param state      true：静音，false：非静音
     */
    public static boolean setStreamMute(int streamType, boolean state) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setStreamMute(streamType, state);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取当前的音频模式，返回值有下述几种模式:
     * {@link AudioManager#MODE_NORMAL}：普通
     * {@link AudioManager#MODE_RINGTONE}：铃声
     * {@link AudioManager#MODE_IN_CALL}：打电话
     * {@link AudioManager#MODE_IN_COMMUNICATION}：通话
     */
    public static int getMode() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AudioManager.MODE_NORMAL;
    }

    /**
     * 设置当前的音频模式
     *
     * @param mode 音频模式
     *             {@link AudioManager#MODE_NORMAL}：普通
     *             {@link AudioManager#MODE_RINGTONE}：铃声
     *             {@link AudioManager#MODE_IN_CALL}：打电话
     *             {@link AudioManager#MODE_IN_COMMUNICATION}：通话
     */
    public static boolean setMode(int mode) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setMode(mode);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取当前的铃声模式
     * 返回值有下述几种模式:
     * {@link AudioManager#RINGER_MODE_NORMAL}：普通
     * {@link AudioManager#RINGER_MODE_SILENT}：静音
     * {@link AudioManager#RINGER_MODE_VIBRATE}：振动
     */
    public static int getRingerMode() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getRingerMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 设置静音模式(静音，且无振动)
     */
    public static boolean ringerSilent() {
        return setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /**
     * 设置振动模式(静音，但有振动)
     */
    public static boolean ringerVibrate() {
        return setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    /**
     * 设置正常模式(正常声音，振动开关由setVibrateSetting决定)
     */
    public static boolean ringerNormal() {
        return setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    /**
     * 获取当前的铃声模式
     *
     * @param ringerMode 铃声模式
     */
    public static boolean setRingerMode(int ringerMode) {
        return setRingerMode(ringerMode, true);
    }

    /**
     * 设置当前的铃声模式
     *
     * @param ringerMode 铃声模式
     *                   {@link AudioManager#RINGER_MODE_NORMAL}：普通
     *                   {@link AudioManager#RINGER_MODE_SILENT}：静音
     *                   {@link AudioManager#RINGER_MODE_VIBRATE}：振动
     * @param setting    如果没授权, 是否跳转到设置页面
     */
    public static boolean setRingerMode(int ringerMode, boolean setting) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                if (isDoNotDisturb(BaseApplication.getInstance(), setting)) {
                    audioManager.setRingerMode(ringerMode);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断是否授权Do not disturb权限，授权Do not disturb权限, 才可进行音量操作
     *
     * @param setting 如果没授权, 是否跳转到设置页面
     */
    public static boolean isDoNotDisturb(Context context, boolean setting) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    && !notificationManager.isNotificationPolicyAccessGranted()) {
                if (setting) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否打开扬声器
     */
    public static boolean isSpeakerphoneOn() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isSpeakerphoneOn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置是否打开扬声器
     *
     * @param on 是否打开
     */
    public static boolean setSpeakerphoneOn(boolean on) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setSpeakerphoneOn(on);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断麦克风是否静音
     */
    public static boolean isMicrophoneMute() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isMicrophoneMute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置是否让麦克风静音
     *
     * @param on 是否打开
     */
    public static boolean setMicrophoneMute(boolean on) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setMicrophoneMute(on);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断是否有音乐处于活跃状态
     */
    public static boolean isMusicActive() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isMusicActive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断是否插入了耳机
     */
    public static boolean isWiredHeadsetOn() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isWiredHeadsetOn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断蓝牙A2DP音频外设是否已连接
     */
    public static boolean isBluetoothA2dpOn() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isBluetoothA2dpOn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 检查当前平台是否支持使用SCO的关闭调用用例
     */
    public static boolean isBluetoothScoAvailableOffCall() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isBluetoothScoAvailableOffCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 检查通信是否使用蓝牙SCO
     */
    public static boolean isBluetoothScoOn() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.isBluetoothScoOn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置是否使用蓝牙SCO耳机进行通讯
     *
     * @param on 是否打开
     */
    public static boolean setBluetoothScoOn(boolean on) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.setBluetoothScoOn(on);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 启动蓝牙SCO音频连接
     */
    public static boolean startBluetoothSco() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.startBluetoothSco();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 停止蓝牙 SCO 音频连接
     */
    public static boolean stopBluetoothSco() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.stopBluetoothSco();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 加载音效
     */
    public static boolean loadSoundEffects() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.loadSoundEffects();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 卸载音效
     */
    public static boolean unloadSoundEffects() {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.unloadSoundEffects();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
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
    public static boolean playSoundEffect(int effectType, float volume) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.playSoundEffect(effectType, volume);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 放弃音频焦点，使上一个焦点所有者(如果有)接收焦点
     *
     * @param listener 焦点监听事件
     */
    public static boolean abandonAudioFocus(AudioManager.OnAudioFocusChangeListener listener) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.abandonAudioFocus(listener);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 调整最相关的流的音量，或者给定的回退流
     *
     * @param direction 调整参数
     */
    public static boolean adjustSuggestedStreamVolume(int direction) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                audioManager.adjustSuggestedStreamVolume(direction, AudioManager.USE_DEFAULT_STREAM_TYPE, 0);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取音频硬件指定key的参数值
     *
     * @param keys Key
     */
    public static String getParameters(String keys) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getParameters(keys);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取用户对振动类型的振动设置
     *
     * @param vibrateType 振动类型
     *                    {@link AudioManager#VIBRATE_TYPE_NOTIFICATION}
     *                    {@link AudioManager#VIBRATE_TYPE_RINGER}
     * @return {@link AudioManager#VIBRATE_SETTING_ON}, {@link AudioManager#VIBRATE_SETTING_OFF}, {@link AudioManager#VIBRATE_SETTING_ONLY_SILENT}
     */
    public static int getVibrateSetting(int vibrateType) {
        AudioManager audioManager = getAudioManager();
        if (audioManager != null) {
            try {
                return audioManager.getVibrateSetting(vibrateType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static AudioManager getAudioManager() {
        return (AudioManager) BaseApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
    }

}
