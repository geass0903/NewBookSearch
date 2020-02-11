package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
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
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.normal_dialog.NormalDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.progress_dialog.ProgressDialogFragment;

public class SettingsFragment extends BaseFragment implements NormalDialogFragment.OnNormalDialogListener, ProgressDialogFragment.OnProgressDialogListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final boolean D = true;

    private static final String DROP_BOX_KEY = "sf7d9ckccl57xvf";

    private SettingsViewModel settingsViewModel;

    private static final int REQUEST_CODE_DROPBOX_LOGOUT = 101;
    private static final int REQUEST_CODE_BACKUP         = 102;
    private static final int REQUEST_CODE_RESTORE        = 103;

    private boolean isRequestAuth = false;
    private static final String KEY_REQUEST_AUTH = "SettingsFragment.KEY_REQUEST_AUTH";


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
        if(savedInstanceState != null){
            isRequestAuth = savedInstanceState.getBoolean(KEY_REQUEST_AUTH);
        }
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
        settingsViewModel.checkLogin(isRequestAuth);
        isRequestAuth = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) Log.d(TAG, "onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_REQUEST_AUTH,isRequestAuth);
    }

    public View.OnClickListener getOnClickListener(){
        return onClickListener;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(D) Log.d(TAG,"id : " + id);
            switch (id){
                case R.id.button_log_in:
                    if(D) Log.d(TAG,"log in");
                    login();
                    break;
                case R.id.button_log_out:
                    if(D) Log.d(TAG,"log out");
                    logout();
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


    private void login(){
        isRequestAuth = true;
        Auth.startOAuth2Authentication(requireContext(), DROP_BOX_KEY);
    }

    private void logout(){
        SettingsFragmentDirections.SettingsToDialog actionToDialog
                = SettingsFragmentDirections.settingsToDialog(REQUEST_CODE_DROPBOX_LOGOUT,getString(R.string.dialog_title_log_out));
        NavHostFragment.findNavController(this).navigate(actionToDialog);
    }

    private void backup(){
        SettingsFragmentDirections.SettingsToProgress actionToProgress
                = SettingsFragmentDirections.settingsToProgress(REQUEST_CODE_BACKUP, ProgressDialogFragment.TYPE_BACKUP, getString(R.string.dialog_title_backup));
        NavHostFragment.findNavController(this).navigate(actionToProgress);
    }

    private void restore(){
        SettingsFragmentDirections.SettingsToProgress actionToProgress
                = SettingsFragmentDirections.settingsToProgress(REQUEST_CODE_RESTORE, ProgressDialogFragment.TYPE_RESTORE, getString(R.string.dialog_title_restore));
        NavHostFragment.findNavController(this).navigate(actionToProgress);
    }


    @Override
    public void onNormalDialogSucceeded(int requestCode, int resultCode) {
        if(D) Log.d(TAG,"onNormalDialogSucceeded");
        if (requestCode == REQUEST_CODE_DROPBOX_LOGOUT && resultCode == DialogInterface.BUTTON_POSITIVE) {
            if(D) Log.d(TAG,"Log out");
            settingsViewModel.logout();
        }
    }


    @Override
    public void onProgressDialogSucceeded(int requestCode, Result result) {

    }
}