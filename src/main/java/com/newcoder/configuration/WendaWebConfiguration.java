package com.newcoder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.newcoder.interceptor.LoginRequredInterceptor;
import com.newcoder.interceptor.PassportInterceptor;
/**
 * 
 * @author Administrator
 *系统初始化的时候会自动配置这些拦截器
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	PassportInterceptor passPortInterceptor;
	
	@Autowired
	LoginRequredInterceptor loginRequredInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passPortInterceptor);//在这儿才是真正把拦截器注册到整个链路上
		registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");//拦截器的顺序不能乱，放在上一个拦截器之后
		//同时第二个拦截器使用了第一个拦截器的变量（hostHandler），他是通过第一个拦截器来设置的
		super.addInterceptors(registry);
	}

}
