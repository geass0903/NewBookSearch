package jp.gr.java_conf.nuranimation.new_book_search.ui.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private PausedHandler handler = new PausedHandler();
    private boolean isClickable = true;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.pause();
    }

    @SuppressWarnings("unused")
    protected PausedHandler getPausedHandler(){
        return handler;
    }

    protected boolean isClickable(){
        return isClickable;
    }

    @SuppressWarnings("SameParameterValue")
    protected void waitClickable(long delayMillis) {
        isClickable = false;
        handler.removeCallbacks(setClickable);
        handler.postDelayed(setClickable, delayMillis);
    }

    private Runnable setClickable = new Runnable() {
        @Override
        public void run() {
            isClickable = true;
        }
    };

}
