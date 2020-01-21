package jp.gr.java_conf.nuranimation.new_book_search.ui.keywords;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentKeywordsBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.NormalDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.RegisterKeywordDialogFragment;


public class KeywordsFragment extends BaseFragment implements KeywordsRecyclerViewAdapter.OnItemClickListener, NormalDialogFragment.OnNormalDialogListener,RegisterKeywordDialogFragment.OnRegisterKeywordDialogListener {
    private static final String TAG = KeywordsFragment.class.getSimpleName();
    private static final boolean D = true;
    private KeywordsViewModel keywordsViewModel;

    private static final String TAG_REGISTER_KEYWORD = "KeywordsFragment.TAG_REGISTER_KEYWORD";
    private static final String TAG_DELETE_KEYWORD = "KeywordsFragment.TAG_DELETE_KEYWORD";
    private static final int REQUEST_CODE_REGISTER_KEYWORD = 101;
    private static final int REQUEST_CODE_DELETE_KEYWORD   = 102;

    private static final String KEY_ID = "KeywordsFragment.KEY_ID";
    private static final String KEY_WORD = "KeywordsFragment.KEY_WORD";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        keywordsViewModel = ViewModelProviders.of(this).get(KeywordsViewModel.class);
        final FragmentKeywordsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_keywords, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(keywordsViewModel);
        KeywordsRecyclerViewAdapter adapter = new KeywordsRecyclerViewAdapter(new ArrayList<Keyword>());
        adapter.setClickListener(this);
        binding.recyclerView.setAdapter(adapter);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.keywords_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_action_add).getIcon().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_add) {
            if (isClickable()) {
                waitClickable(500);
                Bundle bundle = new Bundle();
                ArrayList<String> keywordList = new ArrayList<>();
                List<Keyword> list = keywordsViewModel.getKeywords().getValue();
                if(list != null){
                    for(Keyword keyword : list){
                        if(!(keywordList.contains(keyword.getWord()))){
                            keywordList.add(keyword.getWord());
                        }
                    }
                }
                bundle.putString(RegisterKeywordDialogFragment.KEY_TITLE, getString(R.string.title_edit));
                bundle.putStringArrayList(RegisterKeywordDialogFragment.KEY_WORD_LIST, keywordList);
                bundle.putString(RegisterKeywordDialogFragment.KEY_POSITIVE_LABEL, getString(R.string.label_positive));
                bundle.putString(RegisterKeywordDialogFragment.KEY_NEGATIVE_LABEL, getString(R.string.label_negative));
                bundle.putInt(RegisterKeywordDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_REGISTER_KEYWORD);
                RegisterKeywordDialogFragment.showRegisterKeywordDialog(this, bundle, TAG_REGISTER_KEYWORD);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword) {
        if (isClickable()) {
            waitClickable(500);

            Bundle params = new Bundle();
            params.putInt(KEY_ID, keyword.getId());
            params.putString(KEY_WORD, keyword.getWord());
            if(D) Log.d(TAG,"id: " + keyword.getId());
            if(D) Log.d(TAG,"word: " + keyword.getWord());
            ArrayList<String> keywordList = new ArrayList<>();
            List<Keyword> list = keywordsViewModel.getKeywords().getValue();
            if(list != null){
                for(Keyword word : list){
                    if(!(keywordList.contains(word.getWord()))){
                        keywordList.add(word.getWord());
                    }
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString(RegisterKeywordDialogFragment.KEY_TITLE, getString(R.string.title_edit));
            bundle.putString(RegisterKeywordDialogFragment.KEY_WORD, keyword.getWord());
            bundle.putStringArrayList(RegisterKeywordDialogFragment.KEY_WORD_LIST, keywordList);
            bundle.putString(RegisterKeywordDialogFragment.KEY_POSITIVE_LABEL, getString(R.string.label_positive));
            bundle.putString(RegisterKeywordDialogFragment.KEY_NEGATIVE_LABEL, getString(R.string.label_negative));
            bundle.putBundle(RegisterKeywordDialogFragment.KEY_PARAMS, params);
            bundle.putInt(RegisterKeywordDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_REGISTER_KEYWORD);
            RegisterKeywordDialogFragment.showRegisterKeywordDialog(this, bundle, TAG_REGISTER_KEYWORD);


        }
    }

    @Override
    public void onItemLongClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword) {
        if (isClickable()) {
            waitClickable(500);

            Bundle params = new Bundle();
            params.putInt(KEY_ID, keyword.getId());
            params.putString(KEY_WORD, keyword.getWord());
            if (D) Log.d(TAG, "id: " + keyword.getId());
            if (D) Log.d(TAG, "word: " + keyword.getWord());
            Bundle bundle = new Bundle();
            bundle.putString(NormalDialogFragment.KEY_TITLE, getString(R.string.title_delete));
            bundle.putString(NormalDialogFragment.KEY_POSITIVE_LABEL, getString(R.string.label_positive));
            bundle.putString(NormalDialogFragment.KEY_NEGATIVE_LABEL, getString(R.string.label_negative));
            bundle.putInt(NormalDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_DELETE_KEYWORD);
            bundle.putBundle(NormalDialogFragment.KEY_PARAMS, params);
            bundle.putBoolean(NormalDialogFragment.KEY_CANCELABLE, true);
            NormalDialogFragment.showNormalDialog(this, bundle, TAG_DELETE_KEYWORD);

        }
    }

    @Override
    public void onRegister(int requestCode, int resultCode, String keyword, Bundle params) {
        if(requestCode == REQUEST_CODE_REGISTER_KEYWORD && resultCode == DialogInterface.BUTTON_POSITIVE){
            int id = 0;
            if(params != null){
                id = params.getInt(KEY_ID,0);
            }
            if(id == 0){
                if(D) Log.d(TAG,"register : " + keyword);
                Keyword register = new Keyword(keyword);
                keywordsViewModel.registerKeyword(register);
            }else{
                if(D) Log.d(TAG,"update : " + keyword);
                Keyword update = new Keyword(keyword);
                update.setId(id);
                keywordsViewModel.registerKeyword(update);
            }
        }
    }

    @Override
    public void onCancelled(int requestCode, Bundle params) {

    }

    @Override
    public void onNormalDialogSucceeded(int requestCode, int resultCode, Bundle params) {
        if(requestCode == REQUEST_CODE_DELETE_KEYWORD && resultCode == DialogInterface.BUTTON_POSITIVE && params != null){
            int id = params.getInt(KEY_ID);
            String word = params.getString(KEY_WORD);
            Keyword keyword = new Keyword(id,word);
            keywordsViewModel.deleteKeyword(keyword);
        }
    }

    @Override
    public void onNormalDialogCancelled(int requestCode, Bundle params) {

    }
}