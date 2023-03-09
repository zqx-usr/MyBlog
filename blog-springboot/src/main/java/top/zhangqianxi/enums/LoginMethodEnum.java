package top.zhangqianxi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: LoginMethodEnum
 * Description: 登录方式枚举
 */
@Getter
@AllArgsConstructor
public enum LoginMethodEnum {

    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;


}
