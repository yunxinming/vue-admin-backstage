package com.ming.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.entity.SysMenu;
import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysMenuMapper;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.util.Ajax;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * 查询所有的菜单列表
     * @return
     */
    @Override
    public List<SysMenu> findAllMenu() {
        return lambdaQuery().list();
    }

    @Override
    public Ajax saveMenus(List<SysMenu> menus) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser user = (SysUser) principal;
        List<SysMenu> menuList = menus.stream().peek(m -> {
            m.setCreateBy(user.getUserName());
            m.setCreateTime(LocalDateTime.now());
            if ("M".equals(m.getMenuType())) {
                m.setComponent(null);
                m.setPerms(null);
            } else if ("F".equals(m.getMenuType())) {
                m.setComponent(null);
                m.setIcon(null);
            }
        }).collect(Collectors.toList());
        boolean b = saveBatch(menuList);
        if (!b) return Ajax.error("保存失败");
        return Ajax.success("保存成功");
    }

    @Override
    public Ajax updateMenus(List<SysMenu> menus) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser user = (SysUser) principal;
        List<SysMenu> menuList = menus.stream().peek(m -> {
            m.setUpdateBy(user.getUserName());
            m.setUpdateTime(LocalDateTime.now());
            if ("M".equals(m.getMenuType())) {
                m.setComponent(null);
                m.setPerms(null);
            } else if ("F".equals(m.getMenuType())) {
                m.setComponent(null);
                m.setIcon(null);
            }
        }).collect(Collectors.toList());
        boolean b = updateBatchById(menuList);
        if (!b) return Ajax.error("更新失败");
        return Ajax.success("更新成功");
    }

    @Override
    public Ajax deleteMenus(List<SysMenu> menus) {
        boolean b = removeBatchByIds(menus);
        if (!b) return Ajax.error("删除失败");
        return Ajax.success("删除成功");
    }
}
