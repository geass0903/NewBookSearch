package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.jan_code_dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.databinding.DialogJanCodeBinding;

public class JanCodeDialogFragment extends DialogFragment{
    private static final String TAG = JanCodeDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) {
            throw new IllegalArgumentException("getActivity() == null");
        }
        if (getArguments() == null) {
            throw new NullPointerException("getArguments() == null");
        }

        JanCodeDialogViewModel janCodeDialogViewModel = ViewModelProviders.of(this).get(JanCodeDialogViewModel.class);
        JanCodeDialogFragmentArgs args = JanCodeDialogFragmentArgs.fromBundle(getArguments());
        DialogJanCodeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_jan_code, null, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(janCodeDialogViewModel);
        janCodeDialogViewModel.setIsbn(args.getIsbn());
        janCodeDialogViewModel.setTitle(args.getTitle());
        setCancelable(true);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @BindingAdapter("janCode")
    public static void createBarcode(ImageView view, String janCode){
        if(!(TextUtils.isEmpty(janCode))) {
            if(D) Log.d(TAG,"ISBN : " + janCode);
            view.setImageBitmap(createJANCode(janCode, 200, 100));
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }


    @SuppressWarnings("SameParameterValue")
    private static Bitmap createJANCode(String isbn, int width, int height) {
        Bitmap bitmap = null;
        try {
            EAN13Writer writer = new EAN13Writer();
            // 対象データを変換する
            BitMatrix bitMatrix = writer.encode(isbn, BarcodeFormat.EAN_13, width, height); //...(1)
            // BitMatrixのデータが「true」の時は「黒」を設定し、「false」の時は「白」を設定する              //...(2)
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            // ビットマップ形式に変換する
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }






}



