package jp.gr.java_conf.nuranimation.new_book_search.model.usecase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileApi {

    private static FileApi fileApi;

    private FileApi(){
    }


    public synchronized static FileApi getInstance(){
        if(fileApi == null){
            fileApi = new FileApi();
        }
        return fileApi;
    }


    public void write(File file, List<String> list) throws IOException {
        BufferedWriter writer = getBufferedWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
        for(String keyword : list){
            writer.write(keyword + "\r\n");
//            writer.write(keyword);
//            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public List<String> read(File file) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader reader = getBufferedReaderSkipBOM(new FileInputStream(file), Charset.forName("UTF-8"));
        String line;
        while( (line = reader.readLine()) != null){
            list.add(line);
        }
        reader.close();
        return list;
    }



    private static BufferedReader getBufferedReaderSkipBOM(InputStream is, Charset charSet) throws IOException {
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


    private static BufferedWriter getBufferedWriter(OutputStream os, Charset charSet) {
        OutputStreamWriter osr = new OutputStreamWriter(os, charSet);
        return new BufferedWriter(osr);
    }


}
