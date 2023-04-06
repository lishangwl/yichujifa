package esqeee.xieqing.com.eeeeee.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ScreenUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.Calendar;


public class MatchingUtil {

    public static ScreenPointBean match(Bitmap inFile, Bitmap templateFile,int threshold) {
        Mat sourceImg = new Mat();
        Utils.bitmapToMat(inFile,sourceImg);
        ImageUtils.recycle(inFile);

        Mat tempImg = new Mat();
        Utils.bitmapToMat(templateFile,tempImg);
        ImageUtils.recycle(templateFile);

        return pyramidMatch(sourceImg,tempImg,(float)threshold / 100.f);
    }

    public static ScreenPointBean match(Mat inFile, Bitmap templateFile, android.graphics.Rect rect, int threshold) {
        return match(inFile,templateFile,rect == null?null:new org.opencv.core.Rect(rect.left,rect.top,rect.width(),rect.height()),threshold);
    }

    public static ScreenPointBean match(Mat inFile, Bitmap templateFile,org.opencv.core.Rect rect, int threshold) {
        if (inFile == null || inFile.empty()){
            return null;
        }
        if (rect!=null){
            try {
                inFile = new Mat(inFile,rect);
            }catch (Exception e){
                RuntimeLog.i("区域已超出屏幕,自动使用全屏识别");
            }
        }
        Mat tempImg = new Mat();
        Utils.bitmapToMat(templateFile,tempImg);
        ImageUtils.recycle(templateFile);

        return pyramidMatch(inFile,tempImg,(float)threshold / 100.f);
    }


    /*
    * 金字塔图像算法
    * */
    private static ScreenPointBean pyramidMatch(Mat sourceImg, Mat tempImg,float threshold) {
        long c = System.currentTimeMillis();
        if (sourceImg == null || sourceImg.empty()){
            //RuntimeLog.log("图像识别: 图像为空！");
            return null;
        }
        if (tempImg == null || tempImg.empty()){
            //RuntimeLog.log("图像识别: 待识别图像为空！");
            return null;
        }

        org.opencv.core.Point point = TemplateMatching.fastTemplateMatching(sourceImg, tempImg, TemplateMatching.MATCHING_METHOD_DEFAULT,
                0.75f, threshold, TemplateMatching.MAX_LEVEL_AUTO);


        if (point != null) {
            //RuntimeLog.log("MatchingUtil","true math use "+(System.currentTimeMillis() - c)+"ms");
            return new ScreenPointBean((int)point.x, (int)point.y, tempImg.cols(), tempImg.rows());
        }
        //RuntimeLog.log("MatchingUtil","false math use "+(System.currentTimeMillis() - c)+"ms");
        return null;
    }
}