package com.ming.admin.controller;

import com.ming.admin.entity.SysRole;
import com.ming.admin.service.ISysRoleService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/admin")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;
    @PreAuthorize("hasAnyAuthority('system:user:list','system:user:query')")
    @GetMapping("user/role")
    public Ajax getUserRole(){
        List<SysRole> roleAll = roleService.findRoleAll();
        if (roleAll == null) {
            return Ajax.error("获取失败");
        }
        return Ajax.success(roleAll);
    }
}
