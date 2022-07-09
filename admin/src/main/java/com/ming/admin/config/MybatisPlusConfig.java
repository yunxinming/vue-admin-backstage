package com.ming.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ming.admin.mapper")
public class MybatisPlusConfig {
}
