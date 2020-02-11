package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.keyword_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.DialogEditKeywordBinding;

public class EditKeywordDialogFragment extends DialogFragment{
    private static final String TAG = EditKeywordDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    private static final String KEY_TEMP_KEYWORD = "EditKeywordDialogFragment.KEY_TEMP_KEYWORD";

    private List<String> keywordList = new ArrayList<>();
    private String tempKeyword;

    private EditKeywordDialogViewModel editKeywordDialogViewModel;
    private OnRegisterKeywordDialogListener onRegisterKeywordDialogListener;

    public interface OnRegisterKeywordDialogListener {
        void onRegister(int requestCode, int resultCode, String keyword);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getFragmentManager() != null) {
            Fragment fragment = getFragmentManager().getFragments().get(0);
            if (fragment instanceof OnRegisterKeywordDialogListener) {
                onRegisterKeywordDialogListener = (OnRegisterKeywordDialogListener) fragment;
            } else {
                throw new UnsupportedOperationException("Listener is not Implementation.");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRegisterKeywordDialogListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(D) Log.d(TAG, "onSaveInstanceState");
        outState.putString(KEY_TEMP_KEYWORD, tempKeyword);
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

        setCancelable(true);
        editKeywordDialogViewModel = ViewModelProviders.of(this).get(EditKeywordDialogViewModel.class);
        editKeywordDialogViewModel.getKeywordList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                keywordList = new ArrayList<>(list);
            }
        });

        EditKeywordDialogFragmentArgs args = EditKeywordDialogFragmentArgs.fromBundle(getArguments());
        final DialogEditKeywordBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_keyword, null, false);
        binding.setLifecycleOwner(this);
        binding.setFragment(this);
        binding.setViewModel(editKeywordDialogViewModel);

        if(savedInstanceState == null){
            editKeywordDialogViewModel.setTmpKeyword(args.getKeyword());
        }else{
            editKeywordDialogViewModel.setTmpKeyword(savedInstanceState.getString(KEY_TEMP_KEYWORD));
        }

        final DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (D) Log.d(TAG, "onClick : " + which);
                onRegisterKeywordDialogListener.onRegister(getRequestCode(), which, tempKeyword);
            }
        };

        final String title = getString(R.string.dialog_title_edit);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setPositiveButton(getString(R.string.label_positive), onClickListener);
        builder.setNegativeButton(getString(R.string.label_negative), onClickListener);
        builder.setView(binding.getRoot());
        return builder.create();
    }

    private int getRequestCode() {
        if(getArguments() != null) {
            EditKeywordDialogFragmentArgs args = EditKeywordDialogFragmentArgs.fromBundle(getArguments());
            return args.getRequestCode();
        }
        return -1;
    }

    private void enablePositiveButton(boolean enable){
        if(getDialog() instanceof AlertDialog) {
            AlertDialog dialog = (AlertDialog) getDialog();
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setEnabled(enable);
        }
    }

    public TextWatcher getEditTextWatcher() {
        return new TextWatcher() {
            int currentLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if(D) Log.d(TAG, "beforeTextChanged : " + charSequence.toString());
                tempKeyword = charSequence.toString();
                checkKeyword(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                currentLength = charSequence.toString().length();
                if(D) Log.d(TAG, "onTextChanged : " + charSequence.toString());
                tempKeyword = charSequence.toString();
                checkKeyword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < currentLength) {
                    return;
                }
                boolean unfixed = false;
                Object[] spanned = editable.getSpans(0, editable.length(), Object.class);
                if (spanned != null) {
                    for (Object obj : spanned) {
                        if (obj instanceof android.text.style.UnderlineSpan) {
                            unfixed = true;
                        }
                    }
                }
                if (!unfixed) {
                    tempKeyword = editable.toString();
                    checkKeyword(tempKeyword);
                }
            }
        };
    }

    private void checkKeyword(String keyword){
        if(isRegistrable(keyword)){
            if(keywordList.contains(keyword)){
                if(getArguments() != null){
                    EditKeywordDialogFragmentArgs args = EditKeywordDialogFragmentArgs.fromBundle(getArguments());
                    String edit = args.getKeyword();
                    if(keyword.equals(edit)){
                        if(D) Log.d(TAG,"No Change");
                        editKeywordDialogViewModel.setErrorMessage("");
                        enablePositiveButton(true);
                        return;
                    }
                }
                if(D) Log.d(TAG,"already exists : " + keyword);
                editKeywordDialogViewModel.setErrorMessage(getString(R.string.message_error_exists));
                enablePositiveButton(false);
            }else{
                editKeywordDialogViewModel.setErrorMessage("");
                enablePositiveButton(true);
            }
        }else{
            editKeywordDialogViewModel.setErrorMessage(getString(R.string.message_error_keyword));
            enablePositiveButton(false);
        }
    }

    private static boolean isRegistrable(String word){
        if (TextUtils.isEmpty(word)) {
            if (D) Log.d(TAG, "No word");
            return false;
        }
        if (word.length() >= 2) {
            return true;
        }

        int bytes = 0;
        char[] array = word.toCharArray();
        for (char c : array) {
            if (String.valueOf(c).getBytes().length <= 1) {
                bytes += 1;
            } else {
                bytes += 2;
            }
        }
        if (bytes <= 1) {
            if (D) Log.d(TAG, "1 half width character. NG");
            return false;
        }
        String regex_InHIRAGANA = "\\p{InHIRAGANA}";
        String regex_InKATAKANA = "\\p{InKATAKANA}";
        String regex_InHALFWIDTH_AND_FULLWIDTH_FORMS = "\\p{InHALFWIDTH_AND_FULLWIDTH_FORMS}";
        String regex_InCJK_SYMBOLS_AND_PUNCTUATION = "\\p{InCJK_SYMBOLS_AND_PUNCTUATION}";


        if (word.matches(regex_InHIRAGANA)) {
            if (D) Log.d(TAG, "1 character in HIRAGANA");
            return false;
        }
        if (word.matches(regex_InKATAKANA)) {
            if (D) Log.d(TAG, "1 character in KATAKANA");
            return false;
        }
        if (word.matches(regex_InHALFWIDTH_AND_FULLWIDTH_FORMS)) {
            if (D) Log.d(TAG, "1 character in HALFWIDTH_AND_FULLWIDTH_FORMS");
            return false;
        }
        if (word.matches(regex_InCJK_SYMBOLS_AND_PUNCTUATION)) {
            if (D) Log.d(TAG, "1 character in CJK_SYMBOLS_AND_PUNCTUATION");
            return false;
        }
        return true;
    }



}
