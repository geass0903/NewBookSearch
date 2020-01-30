package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentNewBooksBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.JanCodeDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog.ProgressDialogViewModel;

public class NewBooksFragment extends BaseFragment implements NewBooksRecyclerViewAdapter.OnItemClickListener{
    private static final String TAG = NewBooksFragment.class.getSimpleName();
    private static final boolean D = true;


    private static final String TAG_RELOAD_PROGRESS_DIALOG = "NewBooksFragment.TAG_RELOAD_PROGRESS_DIALOG";
    private static final String TAG_JAN_CODE_DIALOG = "NewBooksFragment.TAG_JAN_CODE_DIALOG";
    private static final int REQUEST_CODE_RELOAD_PROGRESS_DIALOG = 103;
    private static final int LOADER_ID = 1;
    private String mTaskResult;
    private NewBooksViewModel newBooksViewModel;

    private ProgressDialogViewModel progressDialogViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newBooksViewModel = ViewModelProviders.of(this).get(NewBooksViewModel.class);

        progressDialogViewModel = ViewModelProviders.of(requireActivity()).get(ProgressDialogViewModel.class);


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

        if(savedInstanceState == null && getArguments() != null){
            NewBooksFragmentArgs args = NewBooksFragmentArgs.fromBundle(getArguments());

  //          int id = args.getSrcId();
  //          if (D) Log.d(TAG, "onViewCreated" + id);
            boolean result = args.getResult();
            if (D) Log.d(TAG, "onViewCreated" + result);

            if(result) {
                newBooksViewModel.loadAllBooks();
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"savedInstanceState: " + savedInstanceState);

        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString("KEY");
        }

        if (mTaskResult != null) {
            Log.d(TAG,"mTaskResult: " + mTaskResult);
        }
        Log.d(TAG,"mTaskResult: " + mTaskResult);
//        if(mTaskResult == null){
        if(LoaderManager.getInstance(this).getLoader(LOADER_ID) != null){
        }else{
            if(D) Log.d(TAG,"loader = null");
 //           progressDialogViewModel.setVisible(false);
        }

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
            NewBooksFragmentDirections.NewBooksToProgress action
                    = NewBooksFragmentDirections.newBooksToProgress(ProgressDialogViewModel.TYPE_RELOAD, getString(R.string.dialog_title_reload));
            NavHostFragment.findNavController(this).navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(NewBooksRecyclerViewAdapter adapter, int position, Item data) {
        if (isClickable()) {
            if (D) Log.d(TAG, "onItemClick" + data.getIsbn());
            waitClickable(500);
            Bundle bundle = new Bundle();
            bundle.putString(JanCodeDialogFragment.KEY_TITLE, data.getTitle());
            bundle.putString(JanCodeDialogFragment.KEY_ISBN, data.getIsbn());
//            JanCodeDialogFragment.showProgressDialog(this, bundle, TAG_JAN_CODE_DIALOG);


//            NewBooksFragmentDirections.ActionNavigationNewBooksToNavigationDialogJanCode action = NewBooksFragmentDirections.actionNavigationNewBooksToNavigationDialogJanCode(data.getIsbn(),data.getTitle());

 //           NewBooksFragmentDirections.NewBooksToProgress action = NewBooksFragmentDirections.newBooksToProgress("title","message","progress");
 //           NavHostFragment.findNavController(this).navigate(action);

//            NavHostFragment.findNavController(this).navigate(action);

//            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_new_books_to_navigation_dialog_jan_code);


//            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_new_books_to_navigation_dialog_jan_code);

//            Navigation.findNavController().navigate(R.id.navigation_dialog_jan_code);


        }
    }

/*
    @Override
    public void onProgressDialogCancelled(int requestCode, Bundle params) {
        if(requestCode == REQUEST_CODE_RELOAD_PROGRESS_DIALOG){
            getFragmentListener().onFragmentEvent(FragmentEvent.STOP_RELOAD_NEW_BOOKS);
        }
    }


 */

}
