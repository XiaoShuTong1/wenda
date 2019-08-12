package com.newcoder.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.newcoder.dao.LoginTicketDao;
import com.newcoder.dao.UserDao;
import com.newcoder.model.HostHolder;
import com.newcoder.model.LoginTicket;
import com.newcoder.model.User;
import com.newcoder.util.WendaUtil;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	@Autowired
	LoginTicketDao loginTicketDao;
	
	public User getUser(int id){
		return userDao.selectById(id);
	}
	
	public Map<String,String> register(String username,String password){
		Map<String,String>map=new HashMap<String,String>();
		if(StringUtils.isEmpty(username)) {
			map.put("msg", "用户名不能为空！");
			return map;
		}
		if(StringUtils.isEmpty(password)) {
			map.put("msg", "密码不能为空！");
			return map;
		}
		
		User user=userDao.selectByName(username);
		if(user!=null){
			map.put("msg","用户名已经存在");
			return map;
		}
		
		user=new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		user.setHeadurl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setPassword(WendaUtil.MD5(password+user.getSalt()));

		userDao.addUser(user);
		String ticket=addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}

	public Map<String, String> login(String username, String password) {
		Map<String,String>map=new HashMap<String,String>();
		if(StringUtils.isEmpty(username)) {
			map.put("msg", "用户名不能为空！");
			return map;
		}
		if(StringUtils.isEmpty(password)) {
			map.put("msg", "密码不能为空！");
			return map;
		}
		
		User user=userDao.selectByName(username);
		if(user==null){
			map.put("msg","用户名不存在");
			return map;
		}
		if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
			map.put("msg","密码错误");
			return map;
		}
		
		String ticket=addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}
	
	public String addLoginTicket(int userId){
		LoginTicket loginTicket=new LoginTicket();
		loginTicket.setUserId(userId);
		Date now=new Date();
		now.setTime(new Date().getTime()+3600*24*100);
		loginTicket.setExpired(now);
		loginTicket.setStatus(0);
		loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
		
		loginTicketDao.addTicket(loginTicket);
		
		return loginTicket.getTicket();
	}

	public void logout(String ticket) {
		loginTicketDao.updateStatus(ticket, 1);//在拦截器中我们设置过当状态为1的时候，拦截器返回true 不回从后台直接获取user的对象以及数据
		
	}
	
	 public User selectByName(String name) {
	        return userDao.selectByName(name);
	    }

	public User selectUserById(int targetId) {
		
		return userDao.selectById(targetId);
	}

}
