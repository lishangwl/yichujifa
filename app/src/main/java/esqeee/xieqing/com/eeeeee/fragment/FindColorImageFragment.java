package esqeee.xieqing.com.eeeeee.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.dialog.SelectColorDialog;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;

import static android.app.Activity.RESULT_OK;

public class FindColorImageFragment extends BaseFragment implements View.OnTouchListener,View.OnClickListener,ActivityResultListener {

    @BindView(R.id.findColor_showColor)
    TextView showColor;

    @BindView(R.id.findColor_image)
    ImageView imageView;

    @BindView(R.id.findColor_zx)
    ImageView zx;

    @BindView(R.id.findColor_chooseImage)
    Button chooseImage;

    @BindView(R.id.findColor_selectColor)
    Button selectColor;

    @BindView(R.id.findColor_top)
    Button top;

    @BindView(R.id.findColor_bottom)
    Button bottom;

    @BindView(R.id.findColor_left)
    Button left;

    @BindView(R.id.findColor_right)
    Button right;

    @Override
    public View getContentView(LayoutInflater inflater) {
        return View.inflate(getBaseActivity(),R.layout.fragment_findcolor_image,null);
    }



    public static FindColorImageFragment create(Bitmap bitmap){
        return new FindColorImageFragment().setBitmap(bitmap);
    }



    private Bitmap bitmap;
    private SelectColorDialog selectColorDialog;

    public FindColorImageFragment setSelectColorDialog(SelectColorDialog selectColorDialog) {
        this.selectColorDialog = selectColorDialog;
        return this;
    }
    public FindColorImageFragment setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    private int REQUEST_CODE_CHOOSE = 5892;
    @Override
    protected void onFragmentInit() {
        zx.setOnTouchListener(this);
        imageView.setDrawingCacheEnabled(true);
        try {
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            RuntimeLog.log("error:[colorFinder - setBitmap]:"+e.getMessage());
        }

        left.setOnClickListener(this);
        top.setOnClickListener(this);
        right.setOnClickListener(this);
        bottom.setOnClickListener(this);

        chooseImage.setOnClickListener((v)->{
            selectColorDialog.dismiss();
            getBaseActivity().addActivityResultListener((int requestCode, int resultCode, Intent data)->{
                selectColorDialog.show();
                getBaseActivity().removeActivityResultListener(this);
                if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
                    List<String> result = Matisse.obtainPathResult(data);
                    if (result.size()>0){
                        if (bitmap!=null){
                            bitmap.recycle();
                            bitmap = null;
                            //System.gc();
                        }

                        bitmap = BitmapFactory.decodeFile(result.get(0));
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
            Matisse.from(getBaseActivity())
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        });

        selectColor.setOnClickListener(view -> {
            if (selectedListener!=null){
                selectedListener.selectColor(color);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getBaseActivity().removeActivityResultListener(this);
    }

    private float lastX,lastY,dx,dy,paramX,paramY;
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX =  event.getRawX();
                lastY =  event.getRawY();
                paramX =  zx.getX();
                paramY =  zx.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = event.getRawX() - lastX;
                dy = event.getRawY() - lastY;
                zx.setX(paramX+dx);
                zx.setY(paramY+dy);
                chooseColor();
                break;
            case MotionEvent.ACTION_UP://被弹起

                break;
        }
        return true;
    }
    private String color = "#FFFFFFFF";
    private void chooseColor() {
        Bitmap bitmap = imageView.getDrawingCache();
        int x = (int) zx.getX() + zx.getWidth()/2;
        int y = (int) zx.getY() + zx.getHeight()/2;
        if (bitmap == null){
            return;
        }
        if (x >= bitmap.getWidth() || y>=bitmap.getHeight() || x<0 || y<0){
            return;
        }
        int pixel = bitmap.getPixel(x,y);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);
        int alpha = Color.alpha(pixel);
        int color = Color.argb(alpha,red,green,blue);
        imageView.setBackgroundColor(color);

        this.color = "#"+(toHex(alpha)+toHex(red)+toHex(green)+toHex(blue)).toUpperCase();
        showColor.setText("当前选中颜色:"+this.color);
    }

    public String toHex(int i){
        String s = Integer.toHexString(i);
        if (s.length() == 1){
            s="0"+s;
        }
        return s.toUpperCase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.findColor_left:
                zx.setX(zx.getX() - 1);
                break;
            case R.id.findColor_right:
                zx.setX(zx.getX() + 1);
                break;
            case R.id.findColor_top:
                zx.setY(zx.getY() - 1);
                break;
            case R.id.findColor_bottom:
                zx.setY(zx.getY() + 1);
                break;
        }
        chooseColor();
    }


    private OnSelectedListener selectedListener;

    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public interface OnSelectedListener{
        void selectColor(String colorHexString);
    }
}
