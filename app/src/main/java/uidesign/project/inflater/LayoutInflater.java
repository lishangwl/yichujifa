package uidesign.project.inflater;

import android.content.Context;
import android.view.View;

import uidesign.project.model.Attr;

public interface LayoutInflater {
    View getView(Context context, Attr attrs, boolean isDesgin);
}
