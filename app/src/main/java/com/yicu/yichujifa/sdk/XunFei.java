package com.yicu.yichujifa.sdk;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.HttpUtils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import okhttp3.Request;

public class XunFei {
   /* public static final String APPID = "5d7f17bf";
    public static final File JET_COMMON = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YiChuJiFaProject/sdk/res/jet/common.jet");
    public static final File JET_16K = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YiChuJiFaProject/sdk/res/jet/sms_16k.jet");

    public static final File TTS_COMMON = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YiChuJiFaProject/sdk/res/tts/common.jet");
    public static final File TTS_PERSON = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YiChuJiFaProject/sdk/res/tts/xiaoyan.jet");

    public static void init(Context application){
        if (SpeechUtility.createUtility(application, SpeechConstant.APPID +"="+APPID) == null){
            RuntimeLog.e("语音引擎初始化失败！");
        }

        initSynthesizer(application);
        initRecognizer(application);
    }

    private static SpeechSynthesizer synthessizer;
    public static void initSynthesizer(Context context) {
        synthessizer = SpeechSynthesizer.createSynthesizer(context,(int code)->{
            if (code == 0) {
                //RuntimeLog.e("语音合成引擎初始化成功！");
            } else {
                RuntimeLog.e("语音合成引擎初始化失败！");
            }

        });
        //本地识别
        synthessizer.setParameter(SpeechConstant.PARAMS, null);// 清空参数
        synthessizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);//设置使用本地引擎
        synthessizer.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath2(context));//设置发音人资源路径
        synthessizer.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");//设置发音人
        synthessizer.setParameter(SpeechConstant.SPEED, "50");//设置合成语速
        synthessizer.setParameter(SpeechConstant.PITCH, "50");//设置合成音调
        synthessizer.setParameter(SpeechConstant.VOLUME,"100");//设置合成音量
        synthessizer.setParameter(SpeechConstant.STREAM_TYPE, "3");//设置播放器音频流类型
        synthessizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");// 设置播放合成音频打断音乐播放，默认为true
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        synthessizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        synthessizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    private static SpeechRecognizer recognizer;
    public static void initRecognizer(Context context) {
        recognizer = SpeechRecognizer.createRecognizer(context, (int code)->{
            if (code == 0) {
                //RuntimeLog.e("语音识别引擎初始化成功！");
            } else {
                RuntimeLog.e("语音识别引擎初始化失败！");
            }

        });
        if (recognizer == null) {
            RuntimeLog.e("语音识别引擎初始化失败！");
            return;
        }

        recognizer.setParameter(SpeechConstant.PARAMS, null);// 清空参数
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_LOCAL);// 设置引擎
        recognizer.setParameter(SpeechConstant.RESULT_TYPE,"josn");// 设置返回结果格式
        recognizer.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath(context));// 设置本地识别资源
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//设置语言，目前离线听写仅支持中文
        recognizer.setParameter(SpeechConstant.ACCENT, null);
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        recognizer.setParameter(SpeechConstant.VAD_BOS,  "3000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        recognizer.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        recognizer.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    private static String getResourcePath(Context context){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.path, JET_COMMON.getAbsolutePath()));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.path, JET_16K.getAbsolutePath()));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

    private static String getResourcePath2(Context context){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.path, TTS_COMMON.getAbsolutePath()));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.path, TTS_PERSON.getAbsolutePath()));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

    public static void startSynthesizer(String text,DownloadResourceListener listener){
        if (synthessizer!=null){
            synthessizer.startSpeaking(text, new SynthesizerListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onBufferProgress(int i, int i1, int i2, String s) {

                }

                @Override
                public void onSpeakPaused() {

                }

                @Override
                public void onSpeakResumed() {

                }

                @Override
                public void onSpeakProgress(int i, int i1, int i2) {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    listener.onComplete();
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
        }
    }

    public static void startRecognizer(RecognizerDialogListener listener){
        if (recognizer!=null){
            //recognizerDialog.setListener(listener);
            //recognizerDialog.show();
            recognizer.startListening(new RecognizerListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {

                }

                @Override
                public void onBeginOfSpeech() {
                    //RuntimeLog.log("开始语音识别");
                }

                @Override
                public void onEndOfSpeech() {
                    //RuntimeLog.log("停止语音识别");
                }

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean b) {
                    listener.onResult(recognizerResult,b);
                }

                @Override
                public void onError(SpeechError speechError) {
                    listener.onError(speechError);
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
        }
    }

    public static boolean isInstallLocalService() {
        return JET_16K.exists() &&
                JET_COMMON.exists() && TTS_PERSON.exists() && TTS_COMMON.exists();
    }

    public interface DownloadResourceListener{
        void onComplete();
    }
    public static void inStallLocalService(DownloadResourceListener listener) {
        new Thread(()-> {
            if (!JET_COMMON.exists()){
                HttpUtils.Response response = new HttpUtils.Request().url("https://yichujifa.oss-cn-beijing.aliyuncs.com/%E4%B8%80%E8%A7%A6%E5%8D%B3%E5%8F%91%E8%B5%84%E6%BA%90/%E8%AE%AF%E9%A3%9E/iat/common.jet").exec();
                FileIOUtils.writeFileFromBytesByStream(JET_COMMON,response.body().bytes());
            }
            if (!JET_16K.exists()){
                HttpUtils.Response response = new HttpUtils.Request().url("https://yichujifa.oss-cn-beijing.aliyuncs.com/%E4%B8%80%E8%A7%A6%E5%8D%B3%E5%8F%91%E8%B5%84%E6%BA%90/%E8%AE%AF%E9%A3%9E/iat/sms_16k.jet").exec();
                FileIOUtils.writeFileFromBytesByStream(JET_16K,response.body().bytes());
            }
            if (!TTS_COMMON.exists()){
                HttpUtils.Response response = new HttpUtils.Request().url("https://yichujifa.oss-cn-beijing.aliyuncs.com/%E4%B8%80%E8%A7%A6%E5%8D%B3%E5%8F%91%E8%B5%84%E6%BA%90/%E8%AE%AF%E9%A3%9E/tts/common.jet").exec();
                FileIOUtils.writeFileFromBytesByStream(TTS_COMMON,response.body().bytes());
            }
            if (!TTS_PERSON.exists()){
                HttpUtils.Response response = new HttpUtils.Request().url("https://yichujifa.oss-cn-beijing.aliyuncs.com/%E4%B8%80%E8%A7%A6%E5%8D%B3%E5%8F%91%E8%B5%84%E6%BA%90/%E8%AE%AF%E9%A3%9E/tts/xiaoyan.jet").exec();
                FileIOUtils.writeFileFromBytesByStream(TTS_PERSON,response.body().bytes());
            }
            listener.onComplete();
        }).start();
    }*/
}
