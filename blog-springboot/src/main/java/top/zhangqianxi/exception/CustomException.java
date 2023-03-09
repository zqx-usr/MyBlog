package top.zhangqianxi.exception;

/**
 * ClassName: CustomException
 * Description: 自定义业务异常
 *
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
