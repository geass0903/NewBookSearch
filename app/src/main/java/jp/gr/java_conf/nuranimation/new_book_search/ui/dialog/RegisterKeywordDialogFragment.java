package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.model.utils.NewBookUtils;

public class RegisterKeywordDialogFragment extends DialogFragment {
    private static final String TAG = RegisterKeywordDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    public static final String KEY_REQUEST_CODE     = "RegisterKeywordDialogFragment.KEY_REQUEST_CODE";
    public static final String KEY_TITLE            = "RegisterKeywordDialogFragment.KEY_TITLE";
    public static final String KEY_POSITIVE_LABEL   = "RegisterKeywordDialogFragment.KEY_POSITIVE_LABEL";
    public static final String KEY_NEGATIVE_LABEL   = "RegisterKeywordDialogFragment.KEY_NEGATIVE_LABEL";
    public static final String KEY_WORD             = "RegisterKeywordDialogFragment.KEY_WORD";
    public static final String KEY_WORD_LIST        = "RegisterKeywordDialogFragment.KEY_WORD_LIST";
    public static final String KEY_PARAMS           = "RegisterKeywordDialogFragment.KEY_PARAMS";

    private static final String KEY_TEMP_WORD       = "RegisterKeywordDialogFragment.KEY_TEMP_WORD";

    private TextView errorMessageView;
    private Button positiveButton;

    private List<String> keywordList;
    private String tempKeyword;




    public interface OnRegisterKeywordDialogListener {
        void onRegister(int requestCode, int resultCode, String keyword, Bundle params);
        void onCancelled(int requestCode, Bundle params);
    }
    private OnRegisterKeywordDialogListener mListener;


    public static RegisterKeywordDialogFragment newInstance(Fragment fragment, Bundle bundle){
        RegisterKeywordDialogFragment instance = new RegisterKeywordDialogFragment();
        instance.setArguments(bundle);
        int request_code = bundle.getInt(KEY_REQUEST_CODE);
        instance.setTargetFragment(fragment,request_code);
        return instance;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment targetFragment = this.getTargetFragment();
        if (targetFragment instanceof OnRegisterKeywordDialogListener) {
            mListener = (OnRegisterKeywordDialogListener) targetFragment;
        } else {
            Fragment parentFragment = this.getParentFragment();
            if (parentFragment instanceof OnRegisterKeywordDialogListener) {
                mListener = (OnRegisterKeywordDialogListener) parentFragment;
            } else {
                if (context instanceof OnRegisterKeywordDialogListener) {
                    mListener = (OnRegisterKeywordDialogListener) context;
                }
            }
        }
        if (mListener == null) {
            throw new UnsupportedOperationException("Listener is not Implementation.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(D) Log.d(TAG, "onSaveInstanceState");
        outState.putString(KEY_TEMP_WORD, tempKeyword);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(D) Log.d(TAG, "onCreateDialog");
        if (getActivity() == null) {
            throw new IllegalArgumentException("getActivity() == null");
        }
        if (getArguments() == null) {
            throw new NullPointerException("getArguments() == null");
        }

        Bundle bundle = this.getArguments();
        if (savedInstanceState != null) {
            if(D) Log.d(TAG, "savedInstanceState != null");
            tempKeyword = savedInstanceState.getString(KEY_TEMP_WORD);
        }else{
            tempKeyword = bundle.getString(KEY_WORD);
        }

        keywordList = bundle.getStringArrayList(KEY_WORD_LIST);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(D) Log.d(TAG, "onClick : " + which);
                Bundle params = null;
                if(getArguments() != null){
                    params = getArguments().getBundle(KEY_PARAMS);
                }
                mListener.onRegister(getRequestCode(), which, tempKeyword, params);
            }
        };
        final String title = bundle.getString(KEY_TITLE);
        final String positiveLabel = bundle.getString(KEY_POSITIVE_LABEL);
        final String negativeLabel = bundle.getString(KEY_NEGATIVE_LABEL);
        setCancelable(true);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(positiveLabel)) {
            builder.setPositiveButton(positiveLabel, listener);
        }
        if (!TextUtils.isEmpty(negativeLabel)) {
            builder.setNegativeButton(negativeLabel, listener);
        }
        builder.setView(R.layout.dialog_edit_keyword);

        return builder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        Bundle bundle = null;
        if(getArguments() != null){
            bundle = getArguments().getBundle(KEY_PARAMS);
        }
        mListener.onCancelled(getRequestCode(), bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() instanceof AlertDialog) {
            AlertDialog dialog = (AlertDialog) getDialog();
            positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
            errorMessageView = dialog.findViewById(R.id.edit_error_message);
            EditText keywordView = dialog.findViewById(R.id.edit_keyword_view);
            if (keywordView != null) {
                keywordView.addTextChangedListener(new GenericTextWatcher(keywordView));
                if(tempKeyword != null) {
                    keywordView.setText(tempKeyword);
                }
            }

        }
    }


    private int getRequestCode() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.containsKey(KEY_REQUEST_CODE)){
                return bundle.getInt(KEY_REQUEST_CODE);
            }else{
                return getTargetRequestCode();
            }
        }
        return -1;
    }


    public static void showRegisterKeywordDialog(Fragment fragment, Bundle bundle, String tag) {
        if (D) Log.d(TAG, "showRegisterKeywordDialog TAG: " + tag);
        if (fragment != null && bundle != null) {
            FragmentManager manager = fragment.getFragmentManager();
            if(manager != null) {
                Fragment findFragment = manager.findFragmentByTag(tag);
                if (!(findFragment instanceof RegisterKeywordDialogFragment)) {
                    RegisterKeywordDialogFragment dialog = RegisterKeywordDialogFragment.newInstance(fragment, bundle);
                    dialog.show(manager, tag);
                }
            }
        }
    }


    private class GenericTextWatcher implements TextWatcher {
        private View view;
        int currentLength = 0;

        GenericTextWatcher(View view){
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(D) Log.d(TAG, "beforeTextChanged : " + s.toString());
            tempKeyword = s.toString();
            checkKeyword(s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currentLength = s.toString().length();
            if(D) Log.d(TAG, "onTextChanged : " + s.toString());
            tempKeyword = s.toString();
            checkKeyword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() < currentLength) {
                return;
            }
            boolean unfixed = false;
            Object[] spanned = s.getSpans(0, s.length(), Object.class);
            if (spanned != null) {
                for (Object obj : spanned) {
                    if (obj instanceof android.text.style.UnderlineSpan) {
                        unfixed = true;
                    }
                }
            }
            if (!unfixed) {
                confirmString(view, s.toString());
            }

        }

        private void confirmString(View view, String author){
            if (view.getId() == R.id.edit_keyword_view) {
                tempKeyword = author;
                checkKeyword(tempKeyword);
            }
        }
    }

    private void checkKeyword(String keyword){
        if(NewBookUtils.isRegistrable(keyword)){
            if(keywordList.contains(keyword)){
                if(getArguments() != null){
                    String edit = getArguments().getString(KEY_WORD,"");
                    if(edit.equals(keyword)){
                        if(D) Log.d(TAG,"No Change");
                        errorMessageView.setText("");
                        positiveButton.setEnabled(true);
                        return;
                    }
                }
                if(D) Log.d(TAG,"already exists : " + keyword);
                errorMessageView.setText(R.string.error_exists);
                positiveButton.setEnabled(false);
            }else{
                errorMessageView.setText("");
                positiveButton.setEnabled(true);
            }
        }else{
            errorMessageView.setText(R.string.error_keyword);
            positiveButton.setEnabled(false);
        }
    }

}
