package top.zhangqianxi.strategy.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhangqianxi.dto.UserInfoDTO;
import top.zhangqianxi.enums.LoginMethodEnum;
import top.zhangqianxi.strategy.SocialLoginStrategy;

import java.util.Map;

/**
 * ClassName: SocialLoginStrategyContext
 * Description: 第三方登录策略上下文
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/6 16:35
 * @Version: V1.0
 */
@Service
public class SocialLoginStrategyContext {

    @Autowired
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 执行第三方登录策略
     *
     * @param data          数据
     * @param loginTypeEnum 登录枚举类型
     * @return {@link UserInfoDTO} 用户信息
     */
    public UserInfoDTO executeLoginStrategy(String data, LoginMethodEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

}
