package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: QQTokenDTO
* Description: qq token信息
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQTokenDTO {

   /**
    * openid
    */
   private String openid;

   /**
    * 客户端id
    */
   private String client_id;

}
