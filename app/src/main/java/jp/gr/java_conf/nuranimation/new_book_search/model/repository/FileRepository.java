package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.utils.NewBookUtils;

public class FileRepository {

    private static FileRepository fileRepository;

    private FileRepository(){
    }


    public synchronized static FileRepository getInstance(){
        if(fileRepository == null){
            fileRepository = new FileRepository();
        }
        return fileRepository;
    }


    public void write(File file, List<String> list) throws IOException {
        BufferedWriter writer = NewBookUtils.getBufferedWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
        for(String keyword : list){
            writer.write(keyword + "\r\n");
//            writer.write(keyword);
//            writer.newLine();
        }
        writer.flush();
        writer.close();
    }


}
