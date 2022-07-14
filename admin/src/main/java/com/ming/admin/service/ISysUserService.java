package com.ming.admin.service;

import com.ming.admin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.admin.util.Ajax;

import javax.servlet.http.HttpServletRequest;

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
}
