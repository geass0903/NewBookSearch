package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentNewBooksBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog.ProgressDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog.ProgressDialogViewModel;

public class NewBooksFragment extends BaseFragment implements NewBooksRecyclerViewAdapter.OnItemClickListener, ProgressDialogFragment.OnProgressDialogListener {
    private static final String TAG = NewBooksFragment.class.getSimpleName();
    private static final boolean D = true;

    private NewBooksViewModel newBooksViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newBooksViewModel = ViewModelProviders.of(this).get(NewBooksViewModel.class);

        final FragmentNewBooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_books, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(newBooksViewModel);
        NewBooksRecyclerViewAdapter mRecyclerViewAdapter = new NewBooksRecyclerViewAdapter(new ArrayList<Item>());
        mRecyclerViewAdapter.setClickListener(this);
        binding.recyclerView.setAdapter(mRecyclerViewAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (D) Log.d(TAG, "onViewCreated");
        if (savedInstanceState == null && getArguments() != null) {
            NewBooksFragmentArgs args = NewBooksFragmentArgs.fromBundle(getArguments());
            boolean result = args.getResult();
            if (D) Log.d(TAG, "onViewCreated" + result);
            if (result) {
                newBooksViewModel.loadAllBooks();
            }
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
        inflater.inflate(R.menu.new_books_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_action_reload).getIcon().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_reload) {
            NewBooksFragmentDirections.NewBooksToProgress actionToProgress
                    = NewBooksFragmentDirections.newBooksToProgress(ProgressDialogViewModel.TYPE_RELOAD, getString(R.string.dialog_title_reload));
            NavHostFragment.findNavController(this).navigate(actionToProgress);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(NewBooksRecyclerViewAdapter adapter, int position, Item data) {
        if (isClickable()) {
            if (D) Log.d(TAG, "onItemClick" + data.getIsbn());
            waitClickable(500);
            NewBooksFragmentDirections.NewBooksToJanCode actionToJanCode
                    = NewBooksFragmentDirections.newBooksToJanCode(data.getIsbn(), data.getTitle());
            NavHostFragment.findNavController(this).navigate(actionToJanCode);
        }
    }

    @Override
    public void onProgressDialogSucceeded(int requestCode, Result result) {
        if(requestCode == ProgressDialogFragment.REQUEST_CODE_PROGRESS_DIALOG){
            if (D) Log.d(TAG, "onProgressDialogSucceeded : " + result);
            if(result.isSuccess()){
                newBooksViewModel.loadAllBooks();
            }
        }
    }

}
