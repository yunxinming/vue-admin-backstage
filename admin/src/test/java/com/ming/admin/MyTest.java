package com.ming.admin;

import com.ming.admin.entity.SysUser;
import com.ming.admin.service.ISysUserService;
import com.ming.admin.service.impl.SysUserServiceImpl;
import com.ming.admin.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MyTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SysUserServiceImpl userService;
    @Autowired
    JwtTokenUtil jwt;
    @Test
    public void test(){
        //System.out.println(passwordEncoder.encode("admin"));
        SysUser sysUser = new SysUser();
        sysUser.setUserId(1L);
        String admin = jwt.generateToken(sysUser);
        System.out.println(admin);

        String userid = jwt.getUserIdFromToken(admin);
        System.out.println(userid);
    }
}
