package com.startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @Author: XiaHui
 * @Date: 2015年12月24日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月24日
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	protected final Logger logger = LogManager.getLogger(this.getClass());
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        logger.debug(WebSecurityConfig.class.getName());
    }
}
