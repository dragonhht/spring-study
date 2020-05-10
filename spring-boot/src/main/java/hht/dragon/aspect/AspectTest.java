package hht.dragon.aspect;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 切面
 * <p>
 * User : Dragon_hht
 * Date : 17-4-10
 * Time : 下午5:09
 */
@Aspect
@Component
public class AspectTest {

//    @Before("execution(public * hht.dragon.Controller.DataController.getData(..))")
//    public void log() {
//        System.out.println("AOP 测试 Before");
//    }
//
//    @After("execution(public * hht.dragon.Controller.DataController.getData(..))")
//    public void gf() {
//        System.out.println("AOP 测试 After");
//    }

    @Pointcut("execution(public * hht.dragon.Controller.DataController.getData(..))")
    public void log() {

   }

   @Pointcut("execution(public * hht.dragon.Controller.DataController.doException(..))")
   public void dealException() {

   }

   @After("log()")
    public void af() {
       System.out.println("AOP 测试 After");
   }

   @Before("log()")
    public void bf() {
       System.out.println("AOP 测试 Before");
   }

   @AfterReturning(returning = "object", pointcut = "dealException()")
    public void doAfterReturning(Object object) {
        System.out.println("出现异常");
   }

}
