package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.content.Context;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;

public class BaseThread extends Thread{

    private ThreadListener mListener;
    private boolean isCanceled;

    public interface ThreadListener {
        void deliverResult(Result result);
        void deliverProgress(String message, String value);
    }

    protected BaseThread(Context context){
        isCanceled = false;
        if (context instanceof ThreadListener) {
            mListener = (ThreadListener) context;
        } else {
            throw new UnsupportedOperationException("Listener is not Implementation.");
        }
    }

    public void cancel() {
        isCanceled = true;
    }


    protected ThreadListener getThreadListener(){
        return mListener;
    }

    protected boolean isCanceled(){
        return isCanceled;
    }

}
