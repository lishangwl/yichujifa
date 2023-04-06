package com.xieqing.codeutils.util;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public final class 媒体操作 {
    private static int MAX_VOLUME = 100;
    private static int Progress;
    private static MediaPlayer mp;
    private static boolean over;

    static class 媒体操作_1 implements OnBufferingUpdateListener {
        媒体操作_1() {
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            媒体操作.setProgress(percent);
        }
    }

    static class 媒体操作_2 implements OnCompletionListener {
        媒体操作_2() {
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            媒体操作.over = true;
            mp.release();
        }
    }

    private 媒体操作() {
    }

    public static void 播放音乐(String mFile) throws Exception{
        Progress = 0;
        if (mp == null){
            mp = new MediaPlayer();
        }
        over = false;
        if (mFile.startsWith("/")) {
            if (new File(mFile).exists()) {
                mp.setDataSource(mFile);
            }else{
                throw new RuntimeException("音频文件不存在! "+ mFile);
            }
        } else if (mFile.startsWith("http://")) {
            mp.setDataSource(mFile);
            mp.setOnBufferingUpdateListener(new 媒体操作_1());
        } else {
            AssetFileDescriptor aFD = Utils.getApp().getAssets().openFd(mFile);
            mp.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getLength());
            aFD.close();
        }
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("MediaPlayUtils","onPrepared");
                mp.start();
            }
        });
        mp.prepare();
        mp.setOnCompletionListener(new 媒体操作_2());
    }

    private static void setProgress(int value) {
        Progress = value;
    }

    public static int 取缓冲进度() {
        return Progress;
    }

    public static void 继续播放() {
        if (!over && mp != null) {
            try {
                mp.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
    public static void 停止播放() {
        if (!over && mp != null) {
            try {
                over = true;
                mp.stop();
                mp.release();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void 暂停播放() {
        if (!over && mp != null) {
            try {
                mp.pause();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean 取循环播放() {
        if (over || mp == null) {
            return false;
        }
        return mp.isLooping();
    }

    public static void 置循环播放(boolean value) {
        if (!over && mp != null) {
            mp.setLooping(value);
        }
    }

    public static int 取音乐时长() {
        if (over || mp == null) {
            return 0;
        }
        return mp.getDuration();
    }

    public static int 取播放位置() {
        if (over || mp == null) {
            return 0;
        }
        return mp.getCurrentPosition();
    }

    public static void 置播放位置(int value) {
        if (!over && mp != null) {
            mp.seekTo(value);
        }
    }

    public static void 置播放音量(int soundVolume) {
        if (!over && mp != null) {
            float volume = (float) (1.0d - (Math.log((double) (MAX_VOLUME - soundVolume)) / Math.log((double) MAX_VOLUME)));
            mp.setVolume(volume, volume);
        }
    }

    public static boolean 取播放状态() {
        if (over || mp == null) {
            return false;
        }
        return mp.isPlaying();
    }
}