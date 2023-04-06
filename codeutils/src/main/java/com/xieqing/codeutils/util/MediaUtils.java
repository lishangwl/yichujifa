package com.xieqing.codeutils.util;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class MediaUtils {
    private static SoundPool mSoundPool;
    private static HashMap<String,Integer> resources = new HashMap<>();

    public static void playMusicFromAssets(String path){
        ensureSoundPool();
        if (resources.containsKey(path)){
            play(resources.get(path));
        }else{
            int source = 0;
            try {
                source = mSoundPool.load(Utils.getApp().getAssets().openFd(path),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resources.put(path,source);
            play(source);
        }
    }


    private static void play(int source){
        mSoundPool.play(source,1,1,1,0,1);
    }

    private static void ensureSoundPool(){
        if (mSoundPool == null){
            //初始化SoundPool
            if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP){
                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(10)
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build())
                        .build() ;
            }else{
                mSoundPool = new SoundPool(60, AudioManager.STREAM_MUSIC,8) ;
            }
            //设置资源加载监听
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {

                }
            });
        }
    }
}
