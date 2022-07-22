package com.ming.admin.service;

import com.ming.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface ISysRoleService extends IService<SysRole> {
    List<SysRole> findRoleAll();
}
