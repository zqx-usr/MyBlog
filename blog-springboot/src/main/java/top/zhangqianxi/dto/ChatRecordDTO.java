package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zhangqianxi.entity.ChatRecord;

import java.util.List;


/**
* ClassName: ChatRecordDTO
* Description: 聊天记录
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRecordDTO {

   /**
    * 聊天记录
    */
   private List<ChatRecord> chatRecordList;

   /**
    * ip地址
    */
   private String ipAddress;

   /**
    * ip来源
    */
   private String ipSource;

}
