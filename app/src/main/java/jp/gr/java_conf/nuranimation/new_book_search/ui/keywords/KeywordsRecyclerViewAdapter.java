package jp.gr.java_conf.nuranimation.new_book_search.ui.keywords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.databinding.ItemKeywordBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;


public class KeywordsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    private List<Keyword> keywords;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mListener;



    public interface OnItemClickListener {
        void onItemClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword);
        void onItemLongClick(KeywordsRecyclerViewAdapter adapter, int position, Keyword keyword);
    }

    public KeywordsRecyclerViewAdapter(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView= recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        ItemKeywordBinding binding = ItemKeywordBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.getRoot().setOnClickListener(this);
        binding.getRoot().setOnLongClickListener(this);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Keyword keyword = keywords.get(position);
            viewHolder.binding.setKeyword(keyword);
            viewHolder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }


    @Override
    public void onClick(View view) {
        if(mRecyclerView != null && mListener != null){
            int position = mRecyclerView.getChildAdapterPosition(view);
            Keyword keyword = keywords.get(position);
            mListener.onItemClick(this, position, keyword);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(mRecyclerView != null && mListener != null){
            int position = mRecyclerView.getChildAdapterPosition(view);
            Keyword keyword = keywords.get(position);
            mListener.onItemLongClick(this, position, keyword);
            return true;
        }
        return false;
    }

    public void setClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @BindingAdapter({"items"})
    public static void bindItems(RecyclerView recyclerView, List<Keyword> keywords){
        if(keywords == null){
            return;
        }
        if(recyclerView.getAdapter() instanceof KeywordsRecyclerViewAdapter) {
            KeywordsRecyclerViewAdapter adapter = (KeywordsRecyclerViewAdapter)recyclerView.getAdapter();
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.keywords,keywords),true);
            adapter.keywords = keywords;
            diffResult.dispatchUpdatesTo(adapter);
        }
    }



    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final ItemKeywordBinding binding;

        ItemViewHolder(ItemKeywordBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    private static class DiffCallback extends DiffUtil.Callback{
        List<Keyword> oldList;
        List<Keyword> newList;

        DiffCallback(List<Keyword> oldList, List<Keyword> newList){
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }


}
