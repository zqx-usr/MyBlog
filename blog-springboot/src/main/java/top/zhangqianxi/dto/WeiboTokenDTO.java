package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: WeiboTokenDTO
* Description: 微博token
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboTokenDTO {

   /**
    * 微博uid
    */
   private String uid;

   /**
    * 访问令牌
    */
   private String access_token;

}
