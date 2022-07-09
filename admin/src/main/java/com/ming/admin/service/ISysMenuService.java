package com.ming.admin.service;

import com.ming.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface ISysMenuService extends IService<SysMenu> {
    List<String> findPermsByUserId(Long userid);
}
