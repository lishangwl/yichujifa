package esqeee.xieqing.com.eeeeee.library.ocr;

import android.graphics.Rect;

public interface OCRScanResult {
    void onScan(ScanItem[] result);
    public class ScanItem{
        public int width;
        public int height;
        public int top;
        public int left;
        public String text;

        @Override
        public String toString() {
            return left+","+top+","+width+','+height+","+text;
        }
    }

}
