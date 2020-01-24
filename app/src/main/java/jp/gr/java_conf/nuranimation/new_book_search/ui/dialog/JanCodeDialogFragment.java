package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.EAN13Writer;

import jp.gr.java_conf.nuranimation.new_book_search.R;

public class JanCodeDialogFragment extends DialogFragment{
    private static final String TAG = JanCodeDialogFragment.class.getSimpleName();
    private static final boolean D = true;

    public static final String KEY_REQUEST_CODE = "JanCodeDialogFragment.KEY_REQUEST_CODE";
    public static final String KEY_TITLE = "JanCodeDialogFragment.KEY_TITLE";
    public static final String KEY_ISBN = "JanCodeDialogFragment.KEY_ISBN";

    private TextView bookTitleView;
    private TextView bookISBNView;
    private ImageView janCodeView;

    private String mISBN;
    private String mTitle;



    public static JanCodeDialogFragment newInstance( Bundle bundle) {
        JanCodeDialogFragment instance = new JanCodeDialogFragment();
        instance.setArguments(bundle);
        return instance;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, mTitle);
        outState.putString(KEY_ISBN, mISBN);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) {
            throw new IllegalArgumentException("getActivity() == null");
        }
        if (getArguments() == null) {
            throw new NullPointerException("getArguments() == null");
        }

        if (savedInstanceState != null) {

            mTitle = savedInstanceState.getString(KEY_TITLE);
            mISBN = savedInstanceState.getString(KEY_ISBN);
        } else {
//            Bundle bundle = this.getArguments();
//            Bundle bundle = JanCodeDialogFragmentArgs.fromBundle(getArguments()).getIsbn();

//            mTitle = bundle.getString(KEY_TITLE);/
//            mISBN = bundle.getString(KEY_ISBN);
            mTitle = JanCodeDialogFragmentArgs.fromBundle(getArguments()).getTitle();
            mISBN = JanCodeDialogFragmentArgs.fromBundle(getArguments()).getIsbn();
        }
        setCancelable(true);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_jan_code);




        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null) {

            float dpi = getResources().getDisplayMetrics().density;

            bookTitleView = getDialog().findViewById(R.id.book_title);
            bookTitleView.setText(mTitle);
            bookISBNView = getDialog().findViewById(R.id.isbn);
            bookISBNView.setText(mISBN);
            janCodeView = getDialog().findViewById(R.id.jan_code);
            janCodeView.setImageBitmap(createJANCode(mISBN,(int)(180*dpi),(int)(90*dpi)));


        }
    }




    public static void showProgressDialog(Fragment fragment, Bundle bundle, String tag) {
        if (D) Log.d(TAG, "showProgressDialog TAG: " + tag);
        if (fragment != null && bundle != null) {
            FragmentManager manager = fragment.getFragmentManager();
            if(manager != null) {
                Fragment findFragment = manager.findFragmentByTag(tag);
                if (!(findFragment instanceof JanCodeDialogFragment)) {
                    JanCodeDialogFragment dialog = JanCodeDialogFragment.newInstance(bundle);
                    dialog.show(manager, tag);
                }
            }
        }
    }

    public static void dismissProgressDialog(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentManager manager = fragment.getFragmentManager();
            if(manager != null) {
                Fragment findFragment = manager.findFragmentByTag(tag);
                if (findFragment instanceof JanCodeDialogFragment) {
                    ((JanCodeDialogFragment) findFragment).dismiss();
                }
            }
        }
    }


    private Bitmap createJANCode(String isbn, int width, int height) {
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

        }
        return bitmap;
    }
}



