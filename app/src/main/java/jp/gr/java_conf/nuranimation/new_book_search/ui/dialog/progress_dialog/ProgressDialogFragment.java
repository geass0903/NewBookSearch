package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.progress_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.DialogProgressBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;

public class ProgressDialogFragment extends DialogFragment {
    private static final String TAG = ProgressDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    public static final int TYPE_RELOAD  = 1;
    public static final int TYPE_BACKUP  = 2;
    public static final int TYPE_RESTORE = 3;

    private ProgressDialogViewModel progressDialogViewModel;
    private OnProgressDialogListener onProgressDialogListener;

    public interface OnProgressDialogListener {
        void onProgressDialogSucceeded(int requestCode, Result result);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getFragmentManager() != null) {
            Fragment fragment = getFragmentManager().getFragments().get(0);
            if (fragment instanceof OnProgressDialogListener) {
                onProgressDialogListener = (OnProgressDialogListener) fragment;
            } else {
                throw new UnsupportedOperationException("Listener is not Implementation.");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onProgressDialogListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) {
            throw new IllegalArgumentException("getActivity() == null");
        }
        if (getArguments() == null) {
            throw new NullPointerException("getArguments() == null");
        }
        setCancelable(false);
        progressDialogViewModel = ViewModelProviders.of(this).get(ProgressDialogViewModel.class);

        ProgressDialogFragmentArgs args = ProgressDialogFragmentArgs.fromBundle(getArguments());
        final DialogProgressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_progress, null, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(progressDialogViewModel);
        progressDialogViewModel.setTitle(args.getTitle());
        progressDialogViewModel.setMessage(args.getMessage());
        progressDialogViewModel.setProgress(args.getProgress());

        progressDialogViewModel.getResult().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (D) Log.d(TAG, " onChanged : " + result);
                if (result != null) {
                    onProgressDialogListener.onProgressDialogSucceeded(getRequestCode(), result);
                    dismiss();
                }
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        if (args.getType() == TYPE_RELOAD) {
            builder.setNegativeButton(R.string.label_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (D) Log.d(TAG, "NegativeButton.onClick");
                    progressDialogViewModel.cancel();
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (progressDialogViewModel.getState() == ProgressDialogViewModel.STATE_NONE) {
                progressDialogViewModel.cancel();
                dismiss();
            }
        } else {
            if (getArguments() != null) {
                ProgressDialogFragmentArgs args = ProgressDialogFragmentArgs.fromBundle(getArguments());
                progressDialogViewModel.setResult(null);
                switch (args.getType()){
                    case TYPE_RELOAD:
                        progressDialogViewModel.reload();
                        break;
                    case TYPE_BACKUP:
                        progressDialogViewModel.backup();
                        break;
                    case TYPE_RESTORE:
                        progressDialogViewModel.restore();
                        break;
                }
            }
        }
    }

    private int getRequestCode() {
        if(getArguments() != null) {
            ProgressDialogFragmentArgs args = ProgressDialogFragmentArgs.fromBundle(getArguments());
            return args.getRequestCode();
        }
        return -1;
    }



}

