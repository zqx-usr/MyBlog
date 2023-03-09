package top.zhangqianxi.annotation;

import java.lang.annotation.*;

/**
 * ClassName: OptLog
 * Description: 操作日志注解
 *
 * @Author: zhangqianxi
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * @return 操作类型
     */
    String optType() default "";
}
