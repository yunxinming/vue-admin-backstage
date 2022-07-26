package com.ming.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.admin.entity.RolePageOV;
import com.ming.admin.entity.SysRole;
import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysRoleMapper;
import com.ming.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.util.Ajax;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<SysRole> findRoleAll() {
        return lambdaQuery().list();
    }

    @Override
    public Ajax findRoleLists(RolePageOV roleInfo) {
        Page<SysRole> page = page(new Page<>(roleInfo.getCurrentPage(), roleInfo.getPageSize()));
        if (page == null) return Ajax.error("获取角色列表失败");
        return Ajax.success(page);
    }

    @Override
    public Ajax saveRoleLists(List<SysRole> roles) {
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SysRole> roleList = roles.stream().peek(role -> {
            role.setRoleId(null);
            role.setCreateBy(user.getUserName());
            role.setCreateTime(LocalDateTime.now());
        }).collect(Collectors.toList());
        boolean b = saveBatch(roleList);
        if (!b) return Ajax.error("添加角色失败");
        return Ajax.success("添加角色成功");
    }

    @Override
    public Ajax updateRoleLists(List<SysRole> roles) {
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SysRole> roleList = roles.stream().peek(role -> {
            role.setUpdateBy(user.getUserName());
            role.setUpdateTime(LocalDateTime.now());
        }).collect(Collectors.toList());
        boolean b = updateBatchById(roleList);
        if (!b) return Ajax.error("修改角色失败，请稍后再试");
        return Ajax.success("修改角色成功");
    }

    @Override
    public Ajax deleteRoleLists(List<SysRole> roles) {
        boolean b = removeBatchByIds(roles);
        if (!b) return Ajax.error("删除角色失败");
        return Ajax.success("删除角色成功");
    }
}
