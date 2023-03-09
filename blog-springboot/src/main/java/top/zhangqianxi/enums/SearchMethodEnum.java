package top.zhangqianxi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: SearchMethodEnum
 * Description: 搜索方式枚举
 *
 */
@Getter
@AllArgsConstructor
public enum SearchMethodEnum {
    /**
     * mysql
     */
    MYSQL("mysql", "mySqlSearchStrategyImpl"),
    /**
     * elasticsearch
     */
    ELASTICSEARCH("elasticsearch", "esSearchStrategyImpl");

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    /**
     * 获取策略
     *
     * @param mode 模式
     * @return {@link String} 搜索策略
     */
    public static String getStrategy(String mode) {
        for (SearchMethodEnum value : SearchMethodEnum.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
