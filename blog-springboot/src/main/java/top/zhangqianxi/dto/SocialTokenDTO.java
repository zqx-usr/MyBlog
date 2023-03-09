package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: SocialTokenDTO
* Description: 社交登录token
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialTokenDTO {

   /**
    * 开放id
    */
   private String openId;

   /**
    * 访问令牌
    */
   private String accessToken;

   /**
    * 登录类型
    */
   private Integer loginType;

}
