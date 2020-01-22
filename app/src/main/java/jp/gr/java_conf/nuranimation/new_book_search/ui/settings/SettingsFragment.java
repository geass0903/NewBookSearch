package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.dropbox.core.android.Auth;

import jp.gr.java_conf.nuranimation.new_book_search.FragmentEvent;
import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentSettingsBinding;
import jp.gr.java_conf.nuranimation.new_book_search.service.NewBookService;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.NormalDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.ProgressDialogFragment;

public class SettingsFragment extends BaseFragment implements NormalDialogFragment.OnNormalDialogListener, ProgressDialogFragment.OnProgressDialogListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final boolean D = true;

    private static final String DROP_BOX_KEY = "sf7d9ckccl57xvf";

    private Context mContext;
    private SettingsViewModel settingsViewModel;

    private static final int REQUEST_CODE_DROPBOX_LOGOUT = 101;
    private static final int REQUEST_CODE_BACKUP_PROGRESS = 102;
    private static final int REQUEST_CODE_RESTORE_PROGRESS = 103;
    private static final String TAG_DROPBOX_LOGOUT = "SettingsFragment.TAG_DROPBOX_LOGOUT";
    private static final String TAG_BACKUP_PROGRESS = "SettingsFragment.TAG_BACKUP_PROGRESS";
    private static final String TAG_RESTORE_PROGRESS = "SettingsFragment.TAG_RESTORE_PROGRESS";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        final FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setLifecycleOwner(this);
        binding.setFragment(this);
        binding.setViewModel(settingsViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (D) Log.d(TAG, "onViewCreated");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (D) Log.d(TAG, "onResume()");
        settingsViewModel.checkAccessToken();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) Log.d(TAG, "onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View.OnClickListener getOnClickListener(){
        return mOnClickListener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(D) Log.d(TAG,"id : " + id);
            switch (id){
                case R.id.button_log_in:
                    if(D) Log.d(TAG,"log in");
                    Auth.startOAuth2Authentication(mContext, DROP_BOX_KEY);
                    break;
                case R.id.button_log_out:
                    if(D) Log.d(TAG,"log out");
                    popupLogoutDialog();
                    break;
                case R.id.button_backup:
                    if(D) Log.d(TAG,"backup");
                    getFragmentListener().onFragmentEvent(FragmentEvent.START_BACKUP_DROPBOX);
                    break;
                case R.id.button_restore:
                    if(D) Log.d(TAG,"restore");
                    getFragmentListener().onFragmentEvent(FragmentEvent.START_RESTORE_DROPBOX);
                    break;
            }

        }
    };

    private void popupLogoutDialog(){
        Bundle bundle = new Bundle();
        bundle.putString(NormalDialogFragment.KEY_TITLE, getString(R.string.title_log_out));
        bundle.putString(NormalDialogFragment.KEY_POSITIVE_LABEL, getString(R.string.label_positive));
        bundle.putString(NormalDialogFragment.KEY_NEGATIVE_LABEL, getString(R.string.label_negative));
        bundle.putInt(NormalDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_DROPBOX_LOGOUT);
        NormalDialogFragment.showNormalDialog(this, bundle, TAG_DROPBOX_LOGOUT);
    }


    @Override
    public void onNormalDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        if (requestCode == REQUEST_CODE_DROPBOX_LOGOUT && resultCode == DialogInterface.BUTTON_POSITIVE) {
            if(D) Log.d(TAG,"Log out");
            settingsViewModel.deleteAccessToken();
        }
    }

    @Override
    public void onNormalDialogCancelled(int requestCode, Bundle params) {

    }

    @Override
    public void onProgressDialogCancelled(int requestCode, Bundle params) {
        if(requestCode == REQUEST_CODE_BACKUP_PROGRESS){
            getFragmentListener().onFragmentEvent(FragmentEvent.STOP_BACKUP_DROPBOX);
        }
    }

    @Override
    protected void onReceiveLocalBroadcast(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case NewBookService.FILTER_ACTION_UPDATE_SERVICE_STATE:
                    int state = intent.getIntExtra(NewBookService.KEY_SERVICE_STATE, NewBookService.STATE_NONE);
                    switch (state) {
                        case NewBookService.STATE_NONE:
                            if (D) Log.d(TAG, "STATE_NONE");
                            break;
                        case NewBookService.STATE_BACKGROUND_INCOMPLETE:
                            if (D) Log.d(TAG, "STATE_NEW_BOOKS_RELOAD_INCOMPLETE");
                            Bundle bundle = new Bundle();
                            bundle.putInt(ProgressDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_BACKUP_PROGRESS);
                            bundle.putString(ProgressDialogFragment.KEY_TITLE, getString(R.string.progress_backup));
                            bundle.putBoolean(ProgressDialogFragment.KEY_CANCELABLE, true);
                            ProgressDialogFragment.showProgressDialog(this, bundle, TAG_BACKUP_PROGRESS);
                            break;
                        case NewBookService.STATE_BACKGROUND_COMPLETE:
                            if (D) Log.d(TAG, "STATE_BACKUP_COMPLETE");
                            getFragmentListener().onFragmentEvent(FragmentEvent.STOP_BACKUP_DROPBOX);
                            ProgressDialogFragment.dismissProgressDialog(this, TAG_BACKUP_PROGRESS);
                            Toast.makeText(getContext(), getString(R.string.toast_success_backup), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case NewBookService.FILTER_ACTION_UPDATE_PROGRESS:
                    String progress = intent.getStringExtra(NewBookService.KEY_PROGRESS_VALUE);
                    if (progress == null) {
                        progress = "";
                    }
                    String message = intent.getStringExtra(NewBookService.KEY_PROGRESS_MESSAGE);
                    if (message == null) {
                        message = "";
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(ProgressDialogFragment.KEY_MESSAGE, message);
                    bundle.putString(ProgressDialogFragment.KEY_PROGRESS, progress);
                    ProgressDialogFragment.updateProgress(this, bundle, TAG_BACKUP_PROGRESS);
                    break;
            }
        }
    }

}