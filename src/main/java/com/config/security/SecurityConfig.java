package com.config.security;

import com.config.MyPasswordEncoder;
import com.config.security.handle.JwtAccessDeniedHandler;
import com.config.security.handle.JwtAuthenticationEntryPoint;
import com.config.security.handle.JwtAuthenticationFilter;
import com.constant.SpringSecurityConstant;
import com.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author kai
 * @create 2022-09-24 11:52
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;


    /**
     *
     * 一般用于配置白名单
     * 白名单：可以没有权限访问资源
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers(SpringSecurityConstant.WHITE_LIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用jwt，关闭csrf
        http.csrf().disable();
        //基于token认证，关闭session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //配置白名单  除白名单以外的都进行认证
        http.authorizeRequests().anyRequest().authenticated();
        //禁用缓存
        http.headers().cacheControl();
        //添加jwt登录授权的过滤器
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //添加未授权和未登录的返回结果
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);

    }

    /**
     * AuthenticationManager:认证的核心接口
     * AuthenticationManagerBuilder：用于构建AuthenticationManager对象的工具
     * ProviderManager:AuthenticationManager接口的默认实现类
     * 自定义登陆逻辑的配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内置的认证规则
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

//    /**
//     * 使用MD5加密
//     * @return
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new MyPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
