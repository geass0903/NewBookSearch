package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.normal_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import jp.gr.java_conf.nuranimation.new_book_search.R;

public class NormalDialogFragment extends DialogFragment {
    private static final String TAG = NormalDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    public interface OnNormalDialogListener {
        void onNormalDialogSucceeded(int requestCode, int resultCode);
    }
    private OnNormalDialogListener onNormalDialogListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getFragmentManager() != null) {
            Fragment fragment = getFragmentManager().getFragments().get(0);
            if (fragment instanceof OnNormalDialogListener) {
                onNormalDialogListener = (OnNormalDialogListener) fragment;
            } else {
                throw new UnsupportedOperationException("Listener is not Implementation.");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNormalDialogListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getActivity() == null){
            throw new IllegalArgumentException("getActivity() == null");
        }
        if(getArguments() == null){
            throw new NullPointerException("getArguments() == null");
        }

        NormalDialogFragmentArgs args = NormalDialogFragmentArgs.fromBundle(getArguments());

        final DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (D) Log.d(TAG, "onClick : " + which);
                onNormalDialogListener.onNormalDialogSucceeded(getRequestCode(), which);
            }
        };

        setCancelable(true);

        final String title = args.getTitle();
        final String message = args.getMessage();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(getString(R.string.label_positive), onClickListener);
        builder.setNegativeButton(getString(R.string.label_negative), onClickListener);

        return builder.create();
    }


    private int getRequestCode() {
        if(getArguments() != null) {
            NormalDialogFragmentArgs args = NormalDialogFragmentArgs.fromBundle(getArguments());
            return args.getRequestCode();
        }
        return -1;
    }

}
