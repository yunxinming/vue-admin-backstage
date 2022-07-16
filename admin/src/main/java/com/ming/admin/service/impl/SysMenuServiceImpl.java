package com.ming.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.entity.SysMenu;
import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysMenuMapper;
import com.ming.admin.service.ISysMenuService;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return sysMenus.stream().map(SysMenu::getPerms).filter(Strings::hasText).collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> findMenus() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SysMenu> menus = null;
        if (principal instanceof SysUser) {
            SysUser user = (SysUser) principal;
            menus = menuMapper.selectMenusByUserID(user.getUserId());
        }
        assert menus != null;
        return menus.stream().distinct().collect(Collectors.toList());
    }
}
