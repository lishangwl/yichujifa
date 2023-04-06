package esqeee.xieqing.com.eeeeee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FormatCastUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ShareUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;

public class ListActivity extends AppCompatActivity {

    private String data = "";
    private Bitmap generateBitmap(String content,int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = Color.parseColor("#000080");
                    } else {
                        pixels[i * width + j] = Color.WHITE;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String md5(String str) {
        int i = 0;
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length * 2)];
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareactivity);

        final ImageView imageView = findViewById(R.id.srcode);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        ((TextView)findViewById(R.id.pixel)).setText("推荐分辨率 "+ ScreenUtils.getScreenWidth() +" * "+ScreenUtils.getScreenHeight());
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Action action = ActionHelper.from(getIntent().getStringExtra("path"));
        if (action == null){

            ToastUtils.showShort("分享失败！");
            finish();
            return;
        }

        ((TextView)findViewById(R.id.title)).setText(action.getTitle());
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = ActionHelper.actionToString(action,true,getIntent().getBooleanExtra("encrypt",false));
                final String result=POST("http://www.baidu.com/esqeee.xieqing.com.eeeeee/share.php","data="+URLEncoder.encode(data)+"&time="+System.currentTimeMillis());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StringUtils.isSpace(result)){
                            ToastUtils.showShort("分享失败，检测你的网络重试");
                        }else{
                            final Bitmap qrBitmap = generateBitmap(result, SizeUtils.dp2px(260), SizeUtils.dp2px(260));
                            final Bitmap qrBitmap2 = addLogo(qrBitmap, BitmapFactory.decodeResource(getResources(),R.drawable.icon));
                            imageView.setImageBitmap(qrBitmap2);
                            findViewById(R.id.loading).setVisibility(View.GONE);
                        }
                    }
                });

            }
        }).start();


    }

    public String POST(String url,String data) {
        try {
            URL url2=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            String time=System.currentTimeMillis()/1000+"";
            String sign=md5("damingshishuaige"+time+"shuaigeshidaming");
            httpURLConnection.setRequestProperty("time", time);
            httpURLConnection.setRequestProperty("sign", sign);
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.getOutputStream().write(data.getBytes());
            httpURLConnection.getOutputStream().flush();
            InputStream inputStream=httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode()==200) {
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=inputStream.read(buffer))>0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save();
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void shareWX(View view) {
        ShareUtils.分享图片到微信好友(getSaveShared());
    }

    private File save;
    private String getSaveShared() {
        if (save!=null){
            return save.getAbsolutePath();
        }
        File Share = new File(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaShare");
        if (!Share.isDirectory()){
            Share.mkdir();
        }
        save = new File(Share,System.currentTimeMillis()+".png");
        FileIOUtils.writeFileFromBytesByChannel(save.getAbsolutePath(), FormatCastUtils.getBytesByBitmap(ViewUtils.getCacheBitmapFromView(findViewById(R.id.card))),true);
        return save.getAbsolutePath();
    }

    public void shareQQ(View view) {
        ShareUtils.分享图片到QQ好友(getSaveShared());
    }

    public void save(View view) {
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + getSaveShared())));
        ToastUtils.showLong("已保存到系统相册");
    }

    public void shareFile(View view) {
        Action action = ActionHelper.from(getIntent().getStringExtra("path"));

        String sahre = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+action.getTitle()+".ycf";

        FileIOUtils.writeFileFromString(sahre,ActionHelper.actionToString(action,true,getIntent().getBooleanExtra("encrypt",false)));

        ShareCompat.IntentBuilder sc = ShareCompat.IntentBuilder.from(this);
        sc.setType("*/*");
        sc.setStream(FileProvider.getUriForFile(this,getPackageName(),new File(sahre)));
        sc.createChooserIntent();
        sc.startChooser();
    }
}
