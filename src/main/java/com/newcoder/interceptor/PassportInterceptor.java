package com.newcoder.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.newcoder.dao.LoginTicketDao;
import com.newcoder.dao.UserDao;
import com.newcoder.model.HostHolder;
import com.newcoder.model.LoginTicket;
import com.newcoder.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor {
	
	

	@Autowired
	LoginTicketDao loginTicketDao;
	@Autowired
	UserDao userDao;
	@Autowired
	HostHolder hostHolder;//每个线程可以从它里面取user

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	
		//if(2>1) {	return false;}//返回false整个请求就结束了
		String ticket=null;
		if(request.getCookies()!=null){
			for (Cookie cookie:request.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket=cookie.getValue();
					break;
				}
			}
		}
		if(ticket!=null){
			LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);
			if(loginTicket==null||loginTicket.getExpired().before(new Date())
					||loginTicket.getStatus()!=0){
				return true;
			}
			
			User user=userDao.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null){
			modelAndView.addObject("user",hostHolder.getUser());
		}
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		hostHolder.clear();
	}

}
