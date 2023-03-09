package top.zhangqianxi.entity;

import io.jsonwebtoken.Claims;
import lombok.Data;

/**
 * ClassName: CheckResult
 * Package: top.zhangqianxi.entity
 * Description: JWT 验证信息
 *
 * @Author: zhangqianxi
 * @Create: 2022/12/11 16:11
 * @Version: V1.0
 */
@Data
public class CheckResult {

    private Integer errCode;

    private boolean success;

    private Claims claims;

    public boolean isSuccess(){
        return success;
    }

}
