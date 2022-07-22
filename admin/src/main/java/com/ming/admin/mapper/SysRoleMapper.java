package com.ming.admin.mapper;

import com.ming.admin.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select r.role_id from sys_user_role as ur inner join sys_role as r on r.role_id = ur.role_id where ur.user_id = #{userid}")
    List<String> selectRoleNameByUserId(Long userid);
}
