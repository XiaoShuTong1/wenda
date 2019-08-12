package com.newcoder.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newcoder.model.User;
//@Controller
public class IndexController {
	
	/**
	 * Freemark的默认目录显示路径/wenda4/src/main/resources/templates  默认文件格式：.ftl
	 * @return
	 */
	@RequestMapping("/Ihello")
	@SuppressWarnings({"rawtypes","unchecked"})
	public String index1() {
		return "first";
	}
	
	/**
	 * Freemark的配置文件application.properties中配置freemark后缀名以及路径
	 * @return
	 */
	@RequestMapping("/home")
	@SuppressWarnings({"rawtypes","unchecked"})
	public String index2() {
		return "home";
	}
	
	
	/*
	 * @RequestMapping(path= {"/","/index"},method= {RequestMethod.GET})
	 * 
	 * @ResponseBody public String index() { return "Hello NowCoder"; }
	 */
	
	@RequestMapping(path= {"/profile/{groupId}/{useId}"})
	@ResponseBody
	public String profile(@PathVariable("useId") int useId,
			@PathVariable("groupId") String groupId,
			@RequestParam(value="key",defaultValue="1") String key,
			@RequestParam(value="value",required=false) String value
			) {
		return String.format("Profile Page of %s %d t:%s k:%s",groupId,useId,key,value);
	}
	
	@RequestMapping(path= {"/vm"})
	public String template(Model model) {
		model.addAttribute("value", "vvvvv");
		List<String> colors=Arrays.asList(new String[] {"red","yellow","blue"});
		model.addAttribute("colors", colors);
		
		Map<String,String> map=new HashMap<String,String>();
		for(int i=0;i<4;i++){
			map.put(String.valueOf(i), String.valueOf(i*i));
		}
		model.addAttribute("map",map);
		
		User user=new User("王老二");
		model.addAttribute("user",user);
		return "home";
	}
	
	@RequestMapping("/index")
	public String index3(){
		return "index1";
	}
	
	@RequestMapping("/request")
	@ResponseBody
	public String request(Model model,HttpServletRequest request,HttpServletResponse response
			,HttpSession esssion){
		StringBuilder sb=new StringBuilder();
		sb.append(request.getMethod()+"<br>");
		sb.append(request.getQueryString()+"<br>");
		sb.append(request.getPathInfo()+"<br>");
		sb.append(request.getRequestURI()+"<br>");
		return sb.toString();
	}
	
	

}
