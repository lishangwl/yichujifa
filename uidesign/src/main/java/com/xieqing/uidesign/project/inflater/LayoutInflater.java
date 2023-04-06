package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.xieqing.uidesign.project.model.Attr;

public interface LayoutInflater {
    View getView(Context context, Attr attrs,boolean isDesgin);
}
