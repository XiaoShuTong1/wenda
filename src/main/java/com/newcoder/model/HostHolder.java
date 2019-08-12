package com.newcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
	/**
	 * 它会为每一个线程都分配一个对象
	 */
	private static ThreadLocal<User> users=new ThreadLocal<User>();
	
	
	/**
	 * 当当前线程取user的时候，他会为你取当前线程所关联的对象
	 * 底层类似于Map<ThreadId,User>
	 */
	public User getUser() {
		return users.get();
	}
	
	public void setUser(User user){
		users.set(user);
	}
	public void clear(){
		users.remove();
	}


}
