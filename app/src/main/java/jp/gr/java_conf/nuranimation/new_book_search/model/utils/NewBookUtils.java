package jp.gr.java_conf.nuranimation.new_book_search.model.utils;

import android.text.TextUtils;
import android.util.Log;

public class NewBookUtils {
    private static final String TAG = NewBookUtils.class.getSimpleName();
    private static final boolean D = true;


    public static boolean isRegistrable(String word){
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
