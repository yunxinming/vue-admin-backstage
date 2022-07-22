package com.ming.admin.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.admin.entity.SysUser;
import com.ming.admin.service.ISysUserService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/admin")
public class SysUserController {

    @Autowired
    private ISysUserService userService;


    @PostMapping("/login")
    public Ajax login(@RequestBody SysUser user, HttpServletRequest request) {
        String clientIP = ServletUtil.getClientIP(request);
        user.setLoginIp(clientIP);
        return userService.login(user, request);
    }

    /**
     * 获取当前用户信息
     *
     * @return 用户信息对象
     */
    @GetMapping("/current/user")
    public Ajax findUserOne() {
        return userService.findCurrentUserInfo();
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return 成功或失败信息
     */

    @PostMapping("/update/user")
    public Ajax update(@RequestBody SysUser user) {
        return userService.editUser(user);
    }

    @PostMapping("/change/user/password")
    public Ajax changeUserPassword(@RequestBody Map<String, String> map) {
        String password = map.get("password");
        String newPassword = map.get("newPassword");
        return userService.changeUserPassword(password, newPassword);
    }

    @PreAuthorize("hasAnyAuthority('system:user:list','system:user:query')")
    @GetMapping("/user/{currentPage}/{pageSize}")
    public Ajax getUserList(@PathVariable Integer currentPage, @PathVariable Integer pageSize, String username) {
        Page<SysUser> allUser = userService.findAllUser(currentPage, pageSize, username);
        if (allUser == null) {
            return Ajax.error("查询用户列表失败");
        }
        return Ajax.success(allUser);
    }

    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping("/user")
    public Ajax saveUsers(@RequestBody List<SysUser> users){
        return userService.saveUsers(users);
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/user")
    public Ajax updateUsers(@RequestBody List<SysUser> users){
        return userService.updateUsers(users);
    }

    @PreAuthorize("hasAuthority('system:user:remove')")
    @DeleteMapping("/user")
    public Ajax removeUsers(@RequestBody List<SysUser> users){
        return userService.deleteUsers(users);
    }

    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    @PostMapping("/rest/password")
    public Ajax restPassword(@RequestBody List<SysUser> users){
        return userService.resetUserPassword(users);
    }
}
