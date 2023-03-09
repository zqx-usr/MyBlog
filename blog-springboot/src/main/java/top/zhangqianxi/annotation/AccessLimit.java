package top.zhangqianxi.annotation;

import java.lang.annotation.*;

/**
 * ClassName: AccessLimit
 *  Description: redis接口限流
 *
 *
 * @Author: 张千禧
 * @CreateTime: 2023-03-09  13:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 单位时间（秒）
     *
     * @return int
     */
    int seconds();

    /**
     * 单位时间最大请求次数
     *
     * @return int
     */
    int maxCount();
}
