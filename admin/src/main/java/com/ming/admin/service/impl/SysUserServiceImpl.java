package com.ming.admin.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.admin.entity.SysLogininfor;
import com.ming.admin.entity.SysRole;
import com.ming.admin.entity.SysUser;
import com.ming.admin.entity.SysUserRole;
import com.ming.admin.mapper.SysRoleMapper;
import com.ming.admin.mapper.SysUserMapper;
import com.ming.admin.service.ISysLogininforService;
import com.ming.admin.service.ISysMenuService;
import com.ming.admin.service.ISysUserRoleService;
import com.ming.admin.service.ISysUserService;
import com.ming.admin.util.Ajax;
import com.ming.admin.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.stream.Collectors;

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

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

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
     *
     * @return AJAX用户信息
     */
    @Override
    public Ajax findCurrentUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser user = (SysUser) principal;
        SysUser one = lambdaQuery().eq(SysUser::getUserId, user.getUserId()).one();
        one.setPassword(DesensitizedUtil.password(one.getPassword()));
        return Ajax.success(one);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return 是否修改成功信息
     */

    @Override
    public Ajax editUser(SysUser user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        user.setUserId(((SysUser) principal).getUserId());
        user.setPassword(null);
        lambdaUpdate().eq(SysUser::getUserId, user.getUserId()).update(user);
        return Ajax.success("修改成功");
    }

    /**
     * 修改当前用户密码
     *
     * @param password    旧密码
     * @param newPassword 新密码
     * @return 成功或失败的信息
     */
    @Override
    public Ajax changeUserPassword(String password, String newPassword) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
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

    /**
     * 查询所有用户
     *
     * @return 所有用户数据
     */
    @Override
    public Page<SysUser> findAllUser(Integer currentPage, Integer pageSize, String UserName) {
        AbstractWrapper<SysUser, SFunction<SysUser, ?>, LambdaQueryWrapper<SysUser>> wrapper = lambdaQuery().getWrapper();
        wrapper.like(SysUser::getUserName, UserName);
        Page<SysUser> page = page(new Page<>(currentPage, pageSize), UserName == null ? null: wrapper);
        List<SysUser> collect = page.getRecords().stream().peek(m -> {
            m.setPassword("********");
            List<String> sysRoles = roleMapper.selectRoleNameByUserId(m.getUserId());
            m.setRoleIds(sysRoles);
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return page;
    }

    @Override
    public Ajax updateUsers(List<SysUser> users) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser currentUser = (SysUser) principal;
        List<SysUser> userList = users.stream().peek(u -> {
            u.setPassword(null);
            if (u.getUserId() != null) {
                u.setUserName(null);
                u.setUpdateBy(currentUser.getUserName());
                u.setUpdateTime(LocalDateTime.now());
            }
        }).collect(Collectors.toList());
        boolean b = updateBatchById(userList);
        if (!b) return Ajax.error("更新失败");
        handleUserRoleAddOrUpdate(userList);
        return Ajax.success("更新成功");
    }

    private void handleUserRoleAddOrUpdate(List<SysUser> userList) {
        userList.forEach(u -> {
            if (u.getRoleIds() != null) {
                userRoleService.lambdaUpdate().eq(SysUserRole::getUserId, u.getUserId()).remove();
                List<SysUserRole> collect = u.getRoleIds().stream().map(ur -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(u.getUserId());
                    userRole.setRoleId(Long.valueOf(ur));
                    return userRole;
                }).collect(Collectors.toList());
                userRoleService.saveBatch(collect);
            }
        });
    }


    @Override
    public Ajax saveUsers(List<SysUser> users) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser currentUser = (SysUser) principal;
        List<SysUser> userList = users.stream().peek(u -> {
            u.setPassword(null);
            if (u.getUserId() == null) {
                u.setPassword(passwordEncoder.encode("admin"));
                u.setCreateBy(currentUser.getUserName());
                u.setCreateTime(LocalDateTime.now());
            }
        }).collect(Collectors.toList());
        boolean b = saveBatch(userList);
        if (!b) {
            return Ajax.error("操作失败");
        }
        handleUserRoleAddOrUpdate(userList);
        return Ajax.success("操作成功");
    }

    @Override
    public Ajax deleteUsers(List<SysUser> users) {
        boolean b = removeBatchByIds(users);
        if (!b) return Ajax.error("删除失败");
        users.forEach(u -> {
            userRoleService.lambdaUpdate().eq(SysUserRole::getUserId, u.getUserId()).remove();
        });
        return Ajax.success("删除成功");
    }

    @Override
    public Ajax resetUserPassword(List<SysUser> users) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser)) {
            return Ajax.error("用户未登录");
        }
        SysUser currentUser = (SysUser) principal;
        List<SysUser> userList = users.stream().peek(u -> {
            String encode = passwordEncoder.encode(u.getPassword());
            u.setUpdateBy(currentUser.getUserName());
            u.setUpdateTime(LocalDateTime.now());
            u.setPassword(encode);
            if (u.getUserId() == 1 || u.getUserId() == 2) {
                u.setPassword(null);
            }
        }).collect(Collectors.toList());
        boolean b = updateBatchById(userList);
        if (!b) return Ajax.error("重置密码失败！");
        return Ajax.success("重置密码成功！");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }
}
