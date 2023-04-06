package esqeee.xieqing.com.eeeeee.widget.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.R;

public class MyClickSpan extends ClickableSpan {

        private URLSpan url;
        public MyClickSpan(URLSpan url){
            this.url = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.linkColor = Utils.getApp().getResources().getColor(R.color.colorAccent);
            ds.setUnderlineText(false);
        }
        @Override
        public void onClick(View widget) {
            ToastUtils.showShort(url.getURL());
            BroswerActivity.luanch(Utils.getApp(),url.getURL());
        }
    }