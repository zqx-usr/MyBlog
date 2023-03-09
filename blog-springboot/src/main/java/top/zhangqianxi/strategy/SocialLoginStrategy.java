package top.zhangqianxi.strategy;

import top.zhangqianxi.dto.UserInfoDTO;

/**
 * ClassName: SocialLoginStrategy
 * Description: 第三方登录策略
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/6 16:36
 * @Version: V1.0
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoDTO} 用户信息
     */
    UserInfoDTO login(String data);

}
