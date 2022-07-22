package com.ming.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.admin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.admin.util.Ajax;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
public interface ISysUserService extends IService<SysUser> {
    Ajax login(SysUser user, HttpServletRequest request);

    SysUser findUserByUsername(String username);

    Ajax findCurrentUserInfo();

    Ajax editUser(SysUser user);

    Ajax changeUserPassword(String password, String newPassword);

    Page<SysUser> findAllUser(Integer currentPage, Integer pageSize, String UserName);

    Ajax saveUsers(List<SysUser> users);

    Ajax updateUsers(List<SysUser> users);

    Ajax deleteUsers(List<SysUser> users);

    Ajax resetUserPassword(List<SysUser> users);
}
