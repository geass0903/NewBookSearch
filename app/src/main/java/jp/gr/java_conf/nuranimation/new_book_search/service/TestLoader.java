package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestLoader extends BaseAsyncTaskLoader{
    private String mExtraParam;

    public TestLoader(Context context, String extraParam) {
        super(context);
        mExtraParam = extraParam;
    }

    @Override
    public String loadInBackground() {

        Log.d("SampleLoader", "loadInBackground");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}

        return "task complete: " + mExtraParam;
    }
}
