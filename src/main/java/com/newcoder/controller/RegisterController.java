package com.newcoder.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.newcoder.Wenda6Application;
import com.newcoder.service.UserService;

import freemarker.template.utility.StringUtil;

@Controller
public class RegisterController {
	public static final Logger logger=LoggerFactory.getLogger(RegisterController.class);
	@Autowired
	UserService userService;
	
	
	@RequestMapping(path = { "/reg/" }, method = { RequestMethod.POST })
	public String reg(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
			@RequestParam(value = "next", required = false) String next, HttpServletResponse response) {

		try {
			Map<String, String> map = userService.register(username, password);
			if (map.containsKey("ticket")) {
				//System.out.println(map.get("ticket").toString());
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 5);
				}
				response.addCookie(cookie);
				if (!StringUtils.isEmpty(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			} else {

				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
		} catch (Exception e) {
			logger.error("注册异常" + e.getMessage());
			model.addAttribute("msg", "服务器异常");
			return "login";
		}
	}
	 
	
	
	
	
	
	@RequestMapping(path= {"/reglogin"},method={RequestMethod.GET})
	public String reg(Model model, @RequestParam (value="next",required=false) String next){
		model.addAttribute("next",next);
		return "login";
	}
	
	@RequestMapping(path= {"/login/"},method={RequestMethod.POST})
	public String login(Model model,@RequestParam ("username") String username,
			@RequestParam("password") String password,
			@RequestParam (value="next",required=false) String next,
			@RequestParam(value="rememberme",defaultValue="false") boolean rememberme,
			HttpServletResponse response){
		try {
			Map<String,String> map=userService.login(username, password);
			if(map.containsKey("ticket")){
				//System.out.println(map.get("ticket").toString());
				Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
				cookie.setPath("/");
				if(rememberme){
					cookie.setMaxAge(3600*24*5);
				}
				response.addCookie(cookie);
				if(!StringUtils.isEmpty(next)){
					return "redirect:"+next;
				}
				return "redirect:/";
			}else {
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}
			
		} catch (Exception e) {
			logger.error("登录异常~"+e.getMessage());
			return "login";
		}
	}
	@RequestMapping(path={"/logout"},method= {RequestMethod.GET})
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);//登出之后，cookie无效，服务器将不能认识id是谁！
		return "redirect:/";
	}

	

}
