package esqeee.xieqing.com.eeeeee.bbs;

import androidx.annotation.Nullable;

import com.xieqing.codeutils.util.ThreadUtils;

public class NetTask<T> extends ThreadUtils.Task<T> {
    @Nullable
    @Override
    public T doInBackground() throws Throwable {
        return null;
    }

    @Override
    public void onSuccess(@Nullable T result) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFail(Throwable t) {

    }
}
