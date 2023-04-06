package esqeee.xieqing.com.eeeeee.doAction.core;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Text2Speech extends UtteranceProgressListener implements TextToSpeech.OnInitListener {
    TextToSpeech speech;
    public Text2Speech(Context context){
        speech = new TextToSpeech(context,this);
        speech.setOnUtteranceProgressListener(this);
    }

    //

    //设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
    public void setPitch(float pitch){
        speech.setPitch(pitch);
    }

    //设置语速
    public void setSpeechRate(float SpeechRate){
        speech.setSpeechRate(SpeechRate);
    }

    boolean isInited = false;

    public boolean isInited() {
        return isInited;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            setPitch(1.0f);
            setSpeechRate(1.0f);
            int result = speech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                RuntimeLog.log("语音播报：数据丢失或不支持中文语言");
            }
            isInited = true;
        }else{
            RuntimeLog.log("语音播报：初始化失败");
        }
    }

    public void reslese() {
        speech.stop();
        speech.shutdown();
    }

    public void speek(String text) {
        int result = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            result = speech.speak(text, TextToSpeech.QUEUE_FLUSH, null,String.valueOf(System.currentTimeMillis()));
        }else{
            result = speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        if (result != TextToSpeech.SUCCESS){

            RuntimeLog.log("语音播报：播放失败");
        }
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId) {

    }

    public boolean isSpeeking() {
        return speech.isSpeaking();
    }
}
