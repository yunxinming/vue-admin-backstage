package com.ming.admin.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.ming.admin.entity.SysLogininfor;
import com.ming.admin.mapper.SysLogininforMapper;
import com.ming.admin.service.ISysLogininforService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author 云欣名
 * @since 2022-07-14
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements ISysLogininforService {

}
