package com.ming.admin.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.ming.admin.entity.SysLogininfor;
import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysUserMapper;
import com.ming.admin.service.ISysLogininforService;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    @Autowired
    private ISysLogininforService logininforService;

    @Override
    public Ajax login(SysUser user, HttpServletRequest request) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        if (userDetails == null) {
            return Ajax.error(4002, "用户不存在");
        }
        SysLogininfor loginInfo = new SysLogininfor();
        loginInfo.setLoginTime(LocalDateTime.now());
        String header = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(header);
        String clientIP = ServletUtil.getClientIP(request);
        String browser = ua.getBrowser().toString();
        String os = ua.getOs().toString();
        loginInfo.setLoginLocation(clientIP);
        loginInfo.setBrowser(browser);
        loginInfo.setOs(os);
        loginInfo.setIpaddr(clientIP);
        loginInfo.setUserName(userDetails.getUsername());
        if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            loginInfo.setMsg("密码错误");
            loginInfo.setStatus("1");
            logininforService.save(loginInfo);
            return Ajax.error(4002, "用户密码错误");
        }
        this.lambdaUpdate().eq(SysUser::getUserName, userDetails.getUsername()).set(SysUser::getLoginIp, user.getLoginIp()).update();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtTokenUtil.generateToken(userDetails);
        loginInfo.setMsg("登录成功");
        logininforService.save(loginInfo);
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
