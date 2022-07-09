package com.ming.admin.service.impl;

import com.ming.admin.entity.SysMenu;
import com.ming.admin.mapper.SysMenuMapper;
import com.ming.admin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper menuMapper;
    @Override
    public List<String> findPermsByUserId(Long userid) {
        List<SysMenu> sysMenus = menuMapper.selectPermsByUserId(userid);
        List<String> collect = sysMenus.stream().map(m -> m.getPerms()).collect(Collectors.toList());
        return collect.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
