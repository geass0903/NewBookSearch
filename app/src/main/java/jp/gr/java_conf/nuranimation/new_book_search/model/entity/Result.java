package jp.gr.java_conf.nuranimation.new_book_search.model.entity;

import java.util.List;

public class Result {
    public static final int ERROR_CODE_NO_ERROR                 =   0;
    public static final int ERROR_CODE_CANCELED                 =   1;
    public static final int ERROR_CODE_EMPTY_KEYWORDS           =   2;

    public static final int ERROR_CODE_IO_EXCEPTION             =   1;
    public static final int ERROR_CODE_JSON_EXCEPTION           =   2;
    public static final int ERROR_CODE_DBX_EXCEPTION            =   3;
    public static final int ERROR_CODE_HTTP_ERROR               =   4;
    public static final int ERROR_CODE_INTERRUPTED_EXCEPTION    =   5;
    public static final int ERROR_CODE_FILE_NOT_FOUND           =   6;
    public static final int ERROR_CODE_EMPTY_KEYWORD            =   7;

    public static final int ERROR_CODE_EXPORT_DIR_NOT_FOUND     =   9;
    public static final int ERROR_CODE_BACKUP_CANCELED =  10;
    public static final int ERROR_CODE_IMPORT_DIR_NOT_FOUND     =  11;
    public static final int ERROR_CODE_IMPORT_CANCELED          =  12;
    public static final int ERROR_CODE_UPLOAD_CANCELED          =  13;
    public static final int ERROR_CODE_DOWNLOAD_CANCELED        =  14;
    public static final int ERROR_CODE_SEARCH_CANCELED          =  15;
    public static final int ERROR_CODE_RELOAD_CANCELED          =  16;

    public static final int ERROR_CODE_UNKNOWN                  = 100;

    private final int errorCode;
    private final String errorMessage;

    private Result(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return this.errorCode == ERROR_CODE_NO_ERROR;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }


    public static Result DeepCopy(Result result){
        int errorCode = result.getErrorCode();
        String errorMessage = result.getErrorMessage();
        return new Result(errorCode, errorMessage);
    }


    public static Result Success(){
        return new Result(ERROR_CODE_NO_ERROR,"No Error");
    }

    public static Result Error(int errorCode, String errorMessage){
        return new Result(errorCode, errorMessage);
    }


    /*--- File Backup success ---*/
    public static Result DropboxSuccess(int type) {
        return new Result(ERROR_CODE_NO_ERROR, "No Error");
    }
    /*--- File Backup error ---*/
    public static Result DropboxError(int errorCode, String errorMessage) {
        return new Result(errorCode, errorMessage);
    }
}