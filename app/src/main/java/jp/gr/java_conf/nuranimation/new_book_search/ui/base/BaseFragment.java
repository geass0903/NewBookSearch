package jp.gr.java_conf.nuranimation.new_book_search.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import jp.gr.java_conf.nuranimation.new_book_search.FragmentEvent;
import jp.gr.java_conf.nuranimation.new_book_search.service.NewBookService;

public class BaseFragment extends Fragment {
    private PausedHandler handler = new PausedHandler();
    private LocalBroadcastManager mLocalBroadcastManager;
    private FragmentListener mFragmentListener = null;
    private boolean isClickable = true;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceiveLocalBroadcast(context, intent);
        }
    };

    public interface FragmentListener {
        void onFragmentEvent(FragmentEvent event);
    }



    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        if (context instanceof FragmentListener) {
            mFragmentListener = (FragmentListener) context;
        } else {
            throw new UnsupportedOperationException("Listener is not Implementation.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.resume();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(NewBookService.FILTER_ACTION_UPDATE_SERVICE_STATE);
        mIntentFilter.addAction(NewBookService.FILTER_ACTION_UPDATE_PROGRESS);
        mLocalBroadcastManager.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.pause();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    @SuppressWarnings("unused")
    protected PausedHandler getPausedHandler(){
        return handler;
    }

    protected FragmentListener getFragmentListener(){
        return mFragmentListener;
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

    protected void onReceiveLocalBroadcast(Context context, Intent intent){

    }

}
