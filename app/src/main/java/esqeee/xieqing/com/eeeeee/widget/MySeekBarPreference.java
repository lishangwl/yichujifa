package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import esqeee.xieqing.com.eeeeee.R;

public class MySeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    
    private TextView value;
    private TextView title;
    private TextView summy;
    private int mProgress;
    private int mMax = 100;//如果您的seekbar最大值超过了10000，那么在这里修改下即可。否则会被限制最大值为10000
    private boolean mTrackingTouch;
    private OnSeekBarPrefsChangeListener mListener = null;
    
    public MySeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        setMax(mMax);
        setLayoutResource(R.layout.seekbar_prefs);
    }

    public MySeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySeekBarPreference(Context context) {
        this(context, null);
    }
    
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(mMax);
        seekBar.setProgress(mProgress);
        seekBar.setEnabled(isEnabled());
        seekBar.setOnSeekBarChangeListener(this);
        value = (TextView)view.findViewById(R.id.value);
        title = (TextView)view.findViewById(R.id.title);
        summy = (TextView)view.findViewById(R.id.summy);
        value.setText(String.valueOf(mProgress));
        title.setText(getTitle());
        if (TextUtils.isEmpty(getSummary())){
            summy.setVisibility(View.GONE);
        }else{
            summy.setVisibility(View.VISIBLE);
            summy.setText(getSummary());
        }

        ThemeManager.attachTheme(seekBar);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index,30);
    }

    
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setProgress(restoreValue ? getPersistedInt(mProgress): (Integer) defaultValue);
    }

    public void setMax(int max) {
        if (max != mMax) {
            mMax = max;
            notifyChanged();
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    private void setProgress(int progress, boolean notifyChanged) {
        if (progress > mMax) {
            progress = mMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (progress != mProgress) {
            mProgress = progress;
            persistInt(progress);
            if (notifyChanged) {
                notifyChanged();
            }
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setOnSeekBarPrefsChangeListener(OnSeekBarPrefsChangeListener listener) {
        mListener = listener;
    }
    
    /**
     * @author:Jack Tony
     * @tips  :设置监听器
     * @date  :2014-8-17
     */
    public interface OnSeekBarPrefsChangeListener {
        //public void OnSeekBarChangeListener(SeekBar seekBar, boolean isChecked);
        public void onStopTrackingTouch(String key ,SeekBar seekBar) ;
        public void onStartTrackingTouch(String key ,SeekBar seekBar);
        public void onProgressChanged(String key ,SeekBar seekBar, int progress,boolean fromUser);
    }
    
    /**
     * Persist the seekBar's progress value if callChangeListener
     * returns true, otherwise set the seekBar's progress to the stored value
     */
    void syncProgress(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress != mProgress) {
            if (callChangeListener(progress)) {
                setProgress(progress, false);
            } else {
                seekBar.setProgress(mProgress);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //value.setText(seekBar.getProgress()+"");
        if (mListener != null) {
            mListener.onProgressChanged(getKey(),seekBar, progress, fromUser);
        }
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        if (fromUser && !mTrackingTouch) {
            syncProgress(seekBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStartTrackingTouch(getKey(),seekBar);
        }
        mTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStopTrackingTouch(getKey(),seekBar);
        }
        mTrackingTouch = false;
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        notifyHierarchyChanged();
    }


}