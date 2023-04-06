package esqeee.xieqing.com.eeeeee.library.image;


import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ThreadUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class CaptruerImage {
    private android.media.Image image;
    private Bitmap bitmap;
    private Mat mat;
    public CaptruerImage(Image image){
        this.image = image;
        this.bitmap = getBitmap();
    }
    public CaptruerImage(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public void close(){
        try {
            if (bitmap!=null){
                this.bitmap.recycle();
            }
            if (mat != null){
                mat.release();
            }
        }catch (Exception e){
            RuntimeLog.e("CaptruerImage[close]:"+e.getMessage());
        }
    }

    public Image getImage() {
        return image;
    }

    public Bitmap getBitmap(){
        if (this.bitmap == null && image!=null){
            try {
                this.bitmap = toBitmap(image);
            }catch (Throwable e){
                e.printStackTrace();
                //Log.d("ScreenCaptruer",e.getMessage());
                RuntimeLog.log("CaptruerImage[getBitmap]:"+image.getWidth()+e.getMessage());
            }
        }
        return this.bitmap;
    }

    public static Bitmap toBitmap(Image image) {
        Image.Plane plane = image.getPlanes()[0];
        ByteBuffer buffer = plane.getBuffer();
        buffer.position(0);
        int pixelStride = plane.getPixelStride();
        int rowPadding = plane.getRowStride() - pixelStride * image.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth() + rowPadding / pixelStride, image.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        if (rowPadding == 0) {
            return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, image.getWidth(), image.getHeight());
    }

    public Mat getMat() {
        if (this.mat == null){
            this.mat = new Mat();
            if (this.bitmap != null){
                Utils.bitmapToMat(this.bitmap, this.mat);
            }else{
                RuntimeLog.log("bitmap is null");
            }
        }
        return mat;
    }
}
