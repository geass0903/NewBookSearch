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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.dropbox.core.android.Auth;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.FragmentEvent;
import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.FragmentNewBooksBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.service.NewBookService;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.JanCodeDialogFragment;
import jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.ProgressDialogFragment;

public class NewBooksFragment extends BaseFragment implements NewBooksRecyclerViewAdapter.OnItemClickListener,ProgressDialogFragment.OnProgressDialogListener{
    private static final String TAG = NewBooksFragment.class.getSimpleName();
    private static final boolean D = true;


    private static final String TAG_RELOAD_PROGRESS_DIALOG = "NewBooksFragment.TAG_RELOAD_PROGRESS_DIALOG";
    private static final String TAG_JAN_CODE_DIALOG = "NewBooksFragment.TAG_JAN_CODE_DIALOG";
    private static final int REQUEST_CODE_RELOAD_PROGRESS_DIALOG = 103;

    private NewBooksViewModel newBooksViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }


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
            getFragmentListener().onFragmentEvent(FragmentEvent.START_RELOAD_NEW_BOOKS);
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
            JanCodeDialogFragment.showProgressDialog(this, bundle, TAG_JAN_CODE_DIALOG);
        }
    }


    @Override
    protected void onReceiveLocalBroadcast(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case NewBookService.FILTER_ACTION_UPDATE_SERVICE_STATE:
                    int state = intent.getIntExtra(NewBookService.KEY_SERVICE_STATE, NewBookService.STATE_NONE);
                    switch (state) {
                        case NewBookService.STATE_NONE:
                            if (D) Log.d(TAG, "STATE_NONE");
                            break;
                        case NewBookService.STATE_BACKGROUND_INCOMPLETE:
                            if (D) Log.d(TAG, "STATE_NEW_BOOKS_RELOAD_INCOMPLETE");
                            Bundle bundle = new Bundle();
                            bundle.putInt(ProgressDialogFragment.KEY_REQUEST_CODE, REQUEST_CODE_RELOAD_PROGRESS_DIALOG);
                            bundle.putString(ProgressDialogFragment.KEY_TITLE, getString(R.string.progress_reload));
                            bundle.putBoolean(ProgressDialogFragment.KEY_CANCELABLE, true);
                            ProgressDialogFragment.showProgressDialog(this, bundle, TAG_RELOAD_PROGRESS_DIALOG);
                            break;
                        case NewBookService.STATE_BACKGROUND_COMPLETE:
                            if (D) Log.d(TAG, "STATE_NEW_BOOKS_RELOAD_COMPLETE");
                            newBooksViewModel.loadAllBooks();
                            getFragmentListener().onFragmentEvent(FragmentEvent.STOP_RELOAD_NEW_BOOKS);
                            ProgressDialogFragment.dismissProgressDialog(this, TAG_RELOAD_PROGRESS_DIALOG);
                            Toast.makeText(getContext(), getString(R.string.toast_success_reload), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case NewBookService.FILTER_ACTION_UPDATE_PROGRESS:
                    String progress = intent.getStringExtra(NewBookService.KEY_PROGRESS_VALUE);
                    if (progress == null) {
                        progress = "";
                    }
                    String message = intent.getStringExtra(NewBookService.KEY_PROGRESS_MESSAGE);
                    if (message == null) {
                        message = "";
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(ProgressDialogFragment.KEY_MESSAGE, message);
                    bundle.putString(ProgressDialogFragment.KEY_PROGRESS, progress);
                    ProgressDialogFragment.updateProgress(this, bundle, TAG_RELOAD_PROGRESS_DIALOG);
                    break;
            }
        }
    }


    @Override
    public void onProgressDialogCancelled(int requestCode, Bundle params) {
        if(requestCode == REQUEST_CODE_RELOAD_PROGRESS_DIALOG){
            getFragmentListener().onFragmentEvent(FragmentEvent.STOP_RELOAD_NEW_BOOKS);
        }
    }
}
