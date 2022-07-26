package com.ming.admin.controller;

import com.ming.admin.entity.RolePageOV;
import com.ming.admin.entity.SysMenu;
import com.ming.admin.entity.SysRole;
import com.ming.admin.entity.SysRoleMenu;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.service.ISysRoleMenuService;
import com.ming.admin.service.ISysRoleService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysRoleMenuService roleMenuService;


    @PreAuthorize("hasAnyAuthority('system:user:list','system:user:query')")
    @GetMapping("user/role")
    public Ajax getUserRole() {
        List<SysRole> roleAll = roleService.findRoleAll();
        if (roleAll == null) {
            return Ajax.error("获取失败");
        }
        return Ajax.success(roleAll);
    }

    @PreAuthorize("hasAnyAuthority('system:role:list','system:role:query')")
    @GetMapping("/role/{currentPage}/{pageSize}")
    public Ajax getRoleList(@PathVariable Integer currentPage, @PathVariable Integer pageSize, RolePageOV rolePageOV) {
        rolePageOV.setCurrentPage(currentPage);
        rolePageOV.setPageSize(pageSize);
        return roleService.findRoleLists(rolePageOV);
    }

    @PreAuthorize("hasAnyAuthority('system:role:list','system:role:query')")
    @GetMapping("/role/menu")
    public Ajax getRomeMenu(){
        List<SysMenu> allMenu = menuService.findAllMenu();
        if (allMenu == null) return Ajax.error("获取菜单失败");
        return Ajax.success(allMenu);
    }

    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping("/role")
    public Ajax saveRoles(@RequestBody List<SysRole> roles) {
        return roleService.saveRoleLists(roles);
    }

    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/role")
    public Ajax editRoles(@RequestBody List<SysRole> roles) {
        return roleService.updateRoleLists(roles);
    }

    @PreAuthorize("hasAuthority('system:role:remove')")
    @DeleteMapping("/role")
    public Ajax removeRoles(@RequestBody List<SysRole> roles) {
        return roleService.deleteRoleLists(roles);
    }

    @GetMapping("/checked/{roleId}")
    public Ajax getChecked(@PathVariable Integer roleId){
        List<Long> collect = roleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list().stream().map(r -> r.getMenuId()).collect(Collectors.toList());
        return Ajax.success(collect);
    }

    @PreAuthorize("hasAuthority('system:role:edit')")
    @PostMapping("/update/roleOrMenu")
    public Ajax updateRoleOrMenu(@RequestBody List<SysRoleMenu> roleMenus){
        return roleMenuService.updateRoleMenus(roleMenus);
    }

    @PreAuthorize("hasAuthority('system:role:edit')")
    @PostMapping("/delete/roleOrMenu")
    public Ajax deleteRoleMenus(@RequestBody List<SysRoleMenu> roleMenus){
        return roleMenuService.deleteRoleMenus(roleMenus);
    }

}
