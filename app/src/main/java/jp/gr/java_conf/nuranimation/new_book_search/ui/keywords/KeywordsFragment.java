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
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentKeywordsBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.normal_dialog.NormalDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.keyword_dialog.EditKeywordDialogFragment;


public class KeywordsFragment extends BaseFragment implements KeywordsRecyclerViewAdapter.OnItemClickListener, NormalDialogFragment.OnNormalDialogListener, EditKeywordDialogFragment.OnRegisterKeywordDialogListener {
    private static final String TAG = KeywordsFragment.class.getSimpleName();
    private static final boolean D = true;
    private KeywordsViewModel keywordsViewModel;

    private static final int REQUEST_CODE_REGISTER_KEYWORD = 101;
    private static final int REQUEST_CODE_DELETE_KEYWORD   = 102;

    private static final String KEY_ID = "KeywordsFragment.KEY_ID";
    private int keyword_id = 0;


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
        if(savedInstanceState != null) {
            keyword_id = savedInstanceState.getInt(KEY_ID, 0);
        }
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
        outState.putInt(KEY_ID, 0);
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
                KeywordsFragmentDirections.KeywordsToEdit actionToEdit
                        = KeywordsFragmentDirections.keywordsToEdit(REQUEST_CODE_REGISTER_KEYWORD);
                keyword_id = 0;
                NavHostFragment.findNavController(this).navigate(actionToEdit);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword) {
        if (isClickable()) {
            waitClickable(500);
            KeywordsFragmentDirections.KeywordsToEdit actionToEdit
                    = KeywordsFragmentDirections.keywordsToEdit(REQUEST_CODE_REGISTER_KEYWORD);
            actionToEdit.setKeyword(keyword.getWord());
            keyword_id = keyword.getId();
            NavHostFragment.findNavController(this).navigate(actionToEdit);

        }
    }

    @Override
    public void onItemLongClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword) {
        if (isClickable()) {
            waitClickable(500);
            KeywordsFragmentDirections.KeywordsToDialog actionToDialog
                    = KeywordsFragmentDirections.keywordsToDialog(REQUEST_CODE_DELETE_KEYWORD, getString(R.string.dialog_title_delete));
            keyword_id = keyword.getId();
            NavHostFragment.findNavController(this).navigate(actionToDialog);
        }
    }

    @Override
    public void onRegister(int requestCode, int resultCode, String keyword) {
        if(requestCode == REQUEST_CODE_REGISTER_KEYWORD && resultCode == DialogInterface.BUTTON_POSITIVE){
            if(keyword_id == 0){
                if(D) Log.d(TAG,"register : " + keyword);
                Keyword register = new Keyword(keyword);
                keywordsViewModel.registerKeyword(register);
            }else{
                if(D) Log.d(TAG,"update : " + keyword);
                Keyword update = new Keyword(keyword);
                update.setId(keyword_id);
                keywordsViewModel.registerKeyword(update);
            }
        }
    }

    @Override
    public void onNormalDialogSucceeded(int requestCode, int resultCode) {
        if(requestCode == REQUEST_CODE_DELETE_KEYWORD && resultCode == DialogInterface.BUTTON_POSITIVE){
            Keyword keyword = new Keyword(keyword_id, "delete");
            keywordsViewModel.deleteKeyword(keyword);
        }
    }

}