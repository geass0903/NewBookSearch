package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.nuranimation.new_book_search.databinding.ItemBookBinding;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;

@SuppressWarnings("WeakerAccess")
public class NewBooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<Item> items;

    private RecyclerView mRecyclerView;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(NewBooksRecyclerViewAdapter adapter, int position, Item data);
    }

    public NewBooksRecyclerViewAdapter(List<Item> items) {
        this.items = items;
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
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.getRoot().setOnClickListener(this);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Item item = items.get(position);
            viewHolder.binding.setBook(item);
            viewHolder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onClick(View view) {
        if(mRecyclerView != null && mListener != null){
            int position = mRecyclerView.getChildAdapterPosition(view);
            Item data = items.get(position);
            mListener.onItemClick(this, position, data);
        }
    }

    public void setClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    @BindingAdapter({"items"})
    public static void bindItems(RecyclerView recyclerView, List<Item> books){
        if(books == null){
            return;
        }
        if(recyclerView.getAdapter() instanceof NewBooksRecyclerViewAdapter) {
            NewBooksRecyclerViewAdapter adapter = (NewBooksRecyclerViewAdapter)recyclerView.getAdapter();

            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.items,books),false);
            adapter.items = books;
            diffResult.dispatchUpdatesTo(adapter);
        }
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(SimpleDraweeView view, String imageUrl) {
        Uri uri = Uri.parse(parseUrlString(imageUrl));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setCacheChoice(ImageRequest.CacheChoice.SMALL)
                .build();
        view.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(view.getController())
                        .setImageRequest(request)
                        .build());
    }


    private static class ItemViewHolder extends RecyclerView.ViewHolder {
       final ItemBookBinding binding;

        ItemViewHolder(ItemBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static String parseUrlString(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        String REGEX_SURROUND_DOUBLE_QUOTATION = "^\"|\"$";
        String REGEX_SURROUND_BRACKET = "^\\(|\\)$";
        Pattern sdqPattern = Pattern.compile(REGEX_SURROUND_DOUBLE_QUOTATION);
        Matcher matcher = sdqPattern.matcher(url);
        url = matcher.replaceAll("");
        Pattern sbPattern = Pattern.compile(REGEX_SURROUND_BRACKET);
        matcher = sbPattern.matcher(url);
        url = matcher.replaceAll("");

        int index = url.lastIndexOf(".jpg");
        if (index != -1) {
            url = url.substring(0, index + 4);
        } else {
            index = url.lastIndexOf(".gif");
            if (index != -1) {
                url = url.substring(0, index + 4);
            } else {
                return "";
            }
        }

        url = url + "?_200x200";

        return url;
    }


    private static class DiffCallback extends DiffUtil.Callback{
        List<Item> oldList;
        List<Item> newList;

        DiffCallback(List<Item> oldList, List<Item> newList){
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
            return oldList.get(oldItemPosition).getIsbn().equals(newList.get(newItemPosition).getIsbn());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

}
