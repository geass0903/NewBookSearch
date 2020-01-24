package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.DialogProgressBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;

public class ProgressDialogFragment extends DialogFragment{
    private static final String TAG = ProgressDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    private ProgressDialogViewModel progressDialogViewModel;

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
        binding.progressDialogTitle.setText(args.getTitle());
        binding.progressDialogMessage.setText(args.getMessage());
        binding.progressDialogProgress.setText(args.getProgress());
        binding.setLifecycleOwner(this);
        binding.setViewModel(progressDialogViewModel);
        progressDialogViewModel.getResult().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result != null && progressDialogViewModel.getState() == ProgressDialogViewModel.STATE_COMPLETE) {
                    onFinish(result);
                }
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        builder.setNegativeButton(R.string.label_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFinish(null);
            }
        });

        return builder.create();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            if(progressDialogViewModel.getState() == ProgressDialogViewModel.STATE_NONE){
                onFinish(null);
            }
        }else{
            progressDialogViewModel.setResult(null);
            progressDialogViewModel.reload();
        }

    }



    private void onFinish(Result result){
        progressDialogViewModel.setState(ProgressDialogViewModel.STATE_NONE);
        ProgressDialogFragmentDirections.ProgressToNewBooks action = ProgressDialogFragmentDirections.progressToNewBooks();

        if(result != null){
            if(D) Log.d(TAG,"result : " + result);
            action.setSrcId(1);
            action.setResult(true);
        }
        NavHostFragment.findNavController(this).navigate(action);
    }

}



