package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: QQUserInfoDTO
* Description: qq用户信息
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoDTO {

   /**
    * 昵称
    */
   private String nickname;

   /**
    * qq头像
    */
   private String figureurl_qq_1;


}
