package jp.gr.java_conf.nuranimation.new_book_search.model.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

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

    public static int getLineCount(InputStream is, Charset charSet) throws IOException {
        int count = 0;
        BufferedReader br = getBufferedReaderSkipBOM(is, charSet);
        while (br.readLine() != null) {
            count++;
        }
        br.close();
        return count;
    }

    public static BufferedReader getBufferedReaderSkipBOM(InputStream is, Charset charSet) throws IOException {
        InputStreamReader isr;
        BufferedReader br;

        if (!(charSet == Charset.forName("UTF-8"))) {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            return br;
        }

        if (!is.markSupported()) {
            is = new BufferedInputStream(is);
        }
        is.mark(3);
        if (is.available() >= 3) {
            byte[] b = {0, 0, 0};
            int bytes = is.read(b, 0, 3);
            if (bytes == 3 && b[0] != (byte) 0xEF || b[1] != (byte) 0xBB || b[2] != (byte) 0xBF) {
                is.reset();
            }
        }
        isr = new InputStreamReader(is, charSet);
        br = new BufferedReader(isr);
        return br;
    }


    public static BufferedWriter getBufferedWriter(OutputStream os, Charset charSet) {
        OutputStreamWriter osr = new OutputStreamWriter(os, charSet);
        return new BufferedWriter(osr);
    }

}
