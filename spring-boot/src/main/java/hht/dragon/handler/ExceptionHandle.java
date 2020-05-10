package hht.dragon.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常处理
 * <p>
 * User : Dragon_hht
 * Date : 17-4-10
 * Time : 下午6:59
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public String handler(Exception e){
        System.out.println("正在处理异常");
        return "处理异常";
    }
}
