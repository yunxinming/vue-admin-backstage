package com.ming.admin.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.ming.admin.entity.SysUser;
import com.ming.admin.service.ISysUserService;
import com.ming.admin.util.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public Ajax login(@RequestBody SysUser user, HttpServletRequest request){
        String clientIP = ServletUtil.getClientIP(request);
        user.setLoginIp(clientIP);
        return userService.login(user, request);
    }

    @PreAuthorize("hasAuthority('tool:gen:code')")
    @GetMapping("/test")
    public Ajax test() {
        return Ajax.success("Ok");
    }
}
