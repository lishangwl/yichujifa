package esqeee.xieqing.com.eeeeee.doAction.core;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.xieqing.codeutils.util.Utils;
import com.xieqing.codeutils.util.媒体操作;

import java.io.File;

public class Media {
    private MediaPlayer mp;
    private int currentProgress = 0;
    private boolean isPlayCompleted;
    public Media(){
        mp = new MediaPlayer();
    }

    public void reslese(){
        if (mp != null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }

    public void play(String mFile){
        try {
            currentProgress = 0;
            isPlayCompleted = false;
            if (mFile.startsWith("/")) {
                if (new File(mFile).exists()) {
                    mp.setDataSource(mFile);
                }else{
                    throw new RuntimeException("音频文件不存在! "+ mFile);
                }
            } else if (mFile.startsWith("http://")) {
                mp.setDataSource(mFile);
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        currentProgress = percent;
                    }
                });
            } else {
                AssetFileDescriptor aFD = Utils.getApp().getAssets().openFd(mFile);
                mp.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getLength());
                aFD.close();
            }
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlayCompleted = true;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getCurrentProgress() {
        return currentProgress;
    }

    public void resume() {
        if (!isPlayCompleted && mp != null) {
            try {
                mp.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() {
        if (!isPlayCompleted && mp != null) {
            try {
                isPlayCompleted = true;
                mp.stop();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (!isPlayCompleted && mp != null) {
            try {
                mp.pause();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLooping() {
        if (isPlayCompleted || mp == null) {
            return false;
        }
        return mp.isLooping();
    }

    public void setLooping(boolean value) {
        if (!isPlayCompleted && mp != null) {
            mp.setLooping(value);
        }
    }

    public int getDuration() {
        if (isPlayCompleted || mp == null) {
            return 0;
        }
        return mp.getDuration();
    }

    public int getCurrentPosition() {
        if (isPlayCompleted || mp == null) {
            return 0;
        }
        return mp.getCurrentPosition();
    }

    public void seekTo(int value) {
        if (!isPlayCompleted && mp != null) {
            mp.seekTo(value);
        }
    }

    public void setVolume(int soundVolume) {
        if (!isPlayCompleted && mp != null) {
            float volume = (float) (1.0d - (Math.log((double) (100 - soundVolume)) / Math.log((double) 100)));
            mp.setVolume(volume, volume);
        }
    }

    public boolean isPlaying() {
        if (isPlayCompleted || mp == null) {
            return false;
        }
        return mp.isPlaying();
    }
}
