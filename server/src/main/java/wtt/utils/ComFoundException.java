package wtt.utils;

public class ComFoundException extends RuntimeException{
public ComFoundException(){
    super();
}
public ComFoundException(String message){
    super(message);
}
public ComFoundException(String message,Throwable cause){
super(message,cause);
}
public ComFoundException(Throwable cause){
    super(cause);
}
protected  ComFoundException(String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace){
    super(message,cause,enableSuppression,writableStackTrace);
}
}
