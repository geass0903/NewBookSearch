package jp.gr.java_conf.nuranimation.new_book_search.model.entity;

public class Result {
    public static final int ERROR_CODE_CANCELED         = 1;
    public static final int ERROR_CODE_EMPTY_KEYWORDS   = 2;
    public static final int ERROR_CODE_IO_EXCEPTION     = 3;
    public static final int ERROR_CODE_DBX_EXCEPTION    = 4;

    private final int errorCode;
    private final String errorMessage;

    private Result(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return this.errorCode == 0;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }


    public static Result Success() {
        return new Result(0, "No Error");
    }

    public static Result Error(int errorCode, String errorMessage) {
        return new Result(errorCode, errorMessage);
    }
}