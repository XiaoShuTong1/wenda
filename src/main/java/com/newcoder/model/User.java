package com.newcoder.model;

public class User {
	private int id;
	private String name;
	private String password;
	private String salt;
	private String headUrl;
	public User() {
		
	}
	
	public User(int id, String name, String password, String salt, String headurl) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.salt = salt;
		this.setHeadUrl(headUrl);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHeadurl() {
		return getHeadUrl();
	}
	public void setHeadurl(String headurl) {
		this.setHeadUrl(headurl);
	}
	public User(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return "This is "+name+"!";
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

}
