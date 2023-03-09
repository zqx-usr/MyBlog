package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: WebsocketMessageDTO
* Description: websocket消息
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebsocketMessageDTO {

   /**
    * 类型
    */
   private Integer type;

   /**
    * 数据
    */
   private Object data;

}
