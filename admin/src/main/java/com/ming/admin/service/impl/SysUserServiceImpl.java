package com.ming.admin.service.impl;

import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysUserMapper;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.util.Ajax;
import com.ming.admin.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService, UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ISysMenuService menuService;

    @Override
    public Ajax login(SysUser user) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        if (userDetails == null) {
            return Ajax.error(4002, "用户不存在");
        }
        if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            return Ajax.error(4002, "用户密码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtTokenUtil.generateToken(userDetails);
        return Ajax.success("登录成功", token);
    }

    @Override
    public SysUser findUserByUsername(String username) {
        SysUser user = this.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (user == null) return null;
        List<String> perms = menuService.findPermsByUserId(user.getUserId());
        user.setPermission(perms);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }
}
