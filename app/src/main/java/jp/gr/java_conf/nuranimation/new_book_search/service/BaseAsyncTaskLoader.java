package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

public abstract class BaseAsyncTaskLoader<D>extends AsyncTaskLoader<D> {

    private D mResult;
    private boolean mIsStarted = false;

    public BaseAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
            return;
        }
        if (!mIsStarted || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mIsStarted = true;
    }

    @Override
    public void deliverResult(D data) {
        mResult = data;
        super.deliverResult(data);
    }
}
