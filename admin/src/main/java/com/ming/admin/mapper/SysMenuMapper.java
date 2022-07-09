package com.ming.admin.mapper;

import com.ming.admin.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select("select m.perms from sys_user_role as ur inner join sys_role as r on r.role_id = ur.role_id inner join sys_role_menu as rm on rm.role_id = r.role_id inner join sys_menu as m on rm.menu_id = m.menu_id where ur.user_id = #{userid}")
    List<SysMenu> selectPermsByUserId(Long userid);
}
