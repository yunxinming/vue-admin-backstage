package com.ming.admin.service.impl;

import com.ming.admin.entity.SysRole;
import com.ming.admin.mapper.SysRoleMapper;
import com.ming.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-09
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<SysRole> findRoleAll() {
        return lambdaQuery().list();
    }
}
