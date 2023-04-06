package esqeee.xieqing.com.eeeeee.library.image;

import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;


import com.xieqing.codeutils.util.ScreenUtils;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

/**
 * Created by Stardust on 2017/5/18.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ColorFinder {

    private static ColorFinder finder;

    public static ColorFinder getFinder() {
        if (finder == null){
            finder = new ColorFinder();
        }
        return finder;
    }


    public Point findColor(Mat image, int color, int threshold, Rect rect) {
        MatOfPoint matOfPoint = null;
        try {
            if (image == null || image.empty()){
                //RuntimeLog.i("找色: 图像为空！");
                return null;
            }
            Scalar lowerBound = new Scalar(Color.red(color) - threshold, Color.green(color) - threshold,Color.blue(color) - threshold, 255);
            Scalar upperBound = new Scalar(Color.red(color) + threshold, Color.green(color) + threshold,Color.blue(color) + threshold, 255);

            if (rect != null) {
                if (rect.x < 0){
                    rect.x = 0;
                }
                if (rect.y < 0){
                    rect.y = 0;
                }
                if (rect.x + rect.width > ScreenUtils.getScreenWidth()){
                    rect.width = ScreenUtils.getScreenWidth() - rect.x;
                }
                if (rect.y + rect.height > ScreenUtils.getScreenHeight()){
                    rect.height = ScreenUtils.getScreenHeight() - rect.y;
                }
                image = new Mat(image, rect);
            }

            Mat bi = new Mat();
            Core.inRange(image, lowerBound, upperBound, bi);

            Mat nonZeroPos = new Mat();
            Core.findNonZero(bi, nonZeroPos);

            if (nonZeroPos.rows() != 0 || nonZeroPos.cols() != 0) {
                matOfPoint = new MatOfPoint(nonZeroPos);
            }

            bi.release();
            nonZeroPos.release();
        }catch (CvException e){
            RuntimeLog.log(e.getMessage());
            return null;
        }
        if (matOfPoint == null) {
            return null;
        }
        Point point = matOfPoint.toArray()[0];
        if (rect != null) {
            point.x = point.x + rect.x;
            point.y = point.y + rect.y;
        }
        return point;
    }
}