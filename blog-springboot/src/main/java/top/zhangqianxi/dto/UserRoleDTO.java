package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: UserRoleDTO
 * Description: 返回用户角色信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;
}
