package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import java.io.IOException;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Books;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {

    private IBooksTotalApi booksTotalApi;
    private static BookRepository bookRepository;

    private static final String QUERY_FORMAT = "json";
    private static final String QUERY_GENRE = "001"; // Books
    private static final String QUERY_SORT = "-releaseDate"; // sort desc releaseDate
    private static final int QUERY_FORMAT_VERSION = 2;
    private static final int QUERY_HITS = 20;
    private static final int QUERY_STOCK_FLAG = 1;
    private static final int QUERY_FIELD = 0;

    private BookRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBooksTotalApi.HTTPS_API_RAKUTEN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        booksTotalApi = retrofit.create(IBooksTotalApi.class);
    }

    public synchronized static BookRepository getInstance() {
        if (bookRepository == null) {
            bookRepository = new BookRepository();
        }
        return bookRepository;
    }

    public Response<Books> search(String keyword, int page) throws IOException {
        return booksTotalApi.search(IBooksTotalApi.APPLICATION_ID, QUERY_FORMAT, QUERY_FORMAT_VERSION, QUERY_GENRE,
                QUERY_HITS, QUERY_STOCK_FLAG, QUERY_FIELD, QUERY_SORT, keyword, page).execute();
    }

}
