package com.ming.admin.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.entity.SysLogininfor;
import com.ming.admin.entity.SysUser;
import com.ming.admin.mapper.SysUserMapper;
import com.ming.admin.service.ISysLogininforService;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.service.ISysUserService;
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
        if (!userDetails.isEnabled()) {
            return Ajax.error(4002, "用户账号被禁用");
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
        this.lambdaUpdate().eq(SysUser::getUserName, userDetails.getUsername()).set(SysUser::getLoginIp, user.getLoginIp()).set(SysUser::getLoginDate, LocalDateTime.now()).update();
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

    /**
     * 查询当前用户信息
     * @return AJAX用户信息
     */
    @Override
    public Ajax findCurrentUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof  SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser user = (SysUser) principal;
        SysUser one = lambdaQuery().eq(SysUser::getUserId, user.getUserId()).one();
        one.setPassword(DesensitizedUtil.password(one.getPassword()));
        return Ajax.success(one);
    }

    /**
     * 修改用户信息
     * @param user
     * @return 是否修改成功信息
     */

    @Override
    public Ajax editUser(SysUser user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof  SysUser)) {
            return Ajax.error("用户未登录");
        }
        user.setUserId(((SysUser)principal).getUserId());
        user.setPassword(null);
        lambdaUpdate().eq(SysUser::getUserId, user.getUserId()).update(user);
        return Ajax.success("修改成功");
    }

    /**
     * 修改当前用户密码
     * @param password 旧密码
     * @param newPassword 新密码
     * @return 成功或失败的信息
     */
    @Override
    public Ajax changeUserPassword(String password, String newPassword) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof  SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser user = (SysUser) principal;
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Ajax.error("原密码错误");
        }
        boolean update = lambdaUpdate().eq(SysUser::getUserId, user.getUserId()).set(SysUser::getPassword, passwordEncoder.encode(newPassword)).update();
        if (update) {
            return Ajax.success("修改密码成功");
        }
        return Ajax.error("修改失败");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }
}
