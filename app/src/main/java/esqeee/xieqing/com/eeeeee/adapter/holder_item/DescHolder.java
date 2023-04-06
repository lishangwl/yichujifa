package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.api.Color;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;
import esqeee.xieqing.com.eeeeee.widget.span.MyClickSpan;

public class DescHolder extends BaseHolder{
    @BindView(R.id.input)
    ValotionEdittext input;
    public DescHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_texteare,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        findViewById(R.id.card).setBackgroundResource(R.drawable.desc);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");
        input.setTextColor(android.graphics.Color.parseColor("#008B00"));
        input.bindChangeString(param,"text");

        input.setText(replace(param.getString("text")));

        //input.setAutoLinkMask(Linkify.WEB_URLS);
        //input.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private Pattern URL_PATTERN_DEFAULT= Pattern.compile("((http|https):\\/\\/)?([a-zA-Z0-9-]+\\.){1,5}(com|cn|net|org|hk|tw)((\\/(\\w|-)+(\\.([a-zA-Z]+))?)+)?(\\/)?(\\??([\\.%:a-zA-Z0-9_-]+=[#\\.%:a-zA-Z0-9_-]+(&)?)+)?");
    public Spannable getUrlSpan(CharSequence txt) {
        SpannableString value = SpannableString.valueOf(txt);
        Linkify.addLinks(value, URL_PATTERN_DEFAULT,"");
        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        for(URLSpan urlSpan : urlSpans) {
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(new MyClickSpan(urlSpan), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return value;
    }


    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_zhushi;
    }

    @Override
    public String getName() {
        return "脚本注释";
    }

}
