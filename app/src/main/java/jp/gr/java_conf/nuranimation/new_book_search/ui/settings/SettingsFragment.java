package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.dropbox.core.android.Auth;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentSettingsBinding;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.NormalDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog.ProgressDialogViewModel;

public class SettingsFragment extends BaseFragment implements NormalDialogFragment.OnNormalDialogListener{
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final boolean D = true;

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
        if (D) Log.d(TAG, "onActivityCreated");
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
                    settingsViewModel.authDropbox();
                    break;
                case R.id.button_log_out:
                    if(D) Log.d(TAG,"log out");
                    popupLogoutDialog();
                    break;
                case R.id.button_backup:
                    if(D) Log.d(TAG,"backup");
                    backup();
                    break;
                case R.id.button_restore:
                    if(D) Log.d(TAG,"restore");
                    restore();
                    break;
            }

        }
    };



    private void backup(){
        SettingsFragmentDirections.SettingsToProgress actionToProgress
                = SettingsFragmentDirections.settingsToProgress(ProgressDialogViewModel.TYPE_BACKUP, getString(R.string.dialog_title_backup));
        NavHostFragment.findNavController(this).navigate(actionToProgress);
    }

    private void restore(){
        SettingsFragmentDirections.SettingsToProgress actionToProgress
                = SettingsFragmentDirections.settingsToProgress(ProgressDialogViewModel.TYPE_RESTORE, getString(R.string.dialog_title_restore));
        NavHostFragment.findNavController(this).navigate(actionToProgress);
    }

    private void popupLogoutDialog(){


        SettingsFragmentDirections.SettingsToNormal actionToNormal
                = SettingsFragmentDirections.settingsToNormal("");
        NavHostFragment.findNavController(this).navigate(actionToNormal);


/*
        Bundle bundle = new Bundle();
        bundle.putString(NormalDialogFragment.KEY_TITLE, getString(R.string.dialog_title_log_out));
        bundle.putString(NormalDialogFragment.KEY_POSITIVE_LABEL, getString(R.string.label_positive));
        bundle.putString(NormalDialogFragment.KEY_NEGATIVE_LABEL, getString(R.string.label_negative));
        bundle.putInt(NormalDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_DROPBOX_LOGOUT);
        NormalDialogFragment.showNormalDialog(this, bundle, TAG_DROPBOX_LOGOUT);
 */
    }


    @Override
    public void onNormalDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        if(D) Log.d(TAG,"Log out");
        if (requestCode == REQUEST_CODE_DROPBOX_LOGOUT && resultCode == DialogInterface.BUTTON_POSITIVE) {
            if(D) Log.d(TAG,"Log out");
            settingsViewModel.deleteAccessToken();
        }
    }

    @Override
    public void onNormalDialogCancelled(int requestCode, Bundle params) {

    }


}