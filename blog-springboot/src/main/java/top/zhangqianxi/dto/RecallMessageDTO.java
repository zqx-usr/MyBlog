package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: RecallMessageDTO
* Description: 撤回消息
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecallMessageDTO {

   /**
    * 消息id
    */
   private Integer id;

   /**
    * 是否为语音
    */
   private Boolean isVoice;

}
