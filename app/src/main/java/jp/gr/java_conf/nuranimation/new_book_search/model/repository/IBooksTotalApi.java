package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Books;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IBooksTotalApi {

    String HTTPS_API_RAKUTEN_URL    = "https://app.rakuten.co.jp/";
    String APPLICATION_ID           = "1028251347039610250";

    @GET("/services/api/BooksTotal/Search/20170404")
    Call<Books> search(@Query("applicationId") String applicationId,
                       @Query("format") String format,
                       @Query("formatVersion") int formatVersion,
                       @Query("booksGenreId") String booksGenreId,
                       @Query("hits") int hits,
                       @Query("outOfStockFlag") int outOfStockFlag,
                       @Query("field") int field,
                       @Query("sort") String sort,
                       @Query("keyword") String keyword,
                       @Query("page") int page
    );

}
