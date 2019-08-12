package com.newcoder.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.newcoder.model.HostHolder;
import com.newcoder.model.Question;
import com.newcoder.model.ViewObject;
import com.newcoder.service.QuestionService;
import com.newcoder.service.UserService;


@Controller
public class HomeController {
	private static final Logger logger=LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	UserService userService;
	@Autowired
	QuestionService questionService;
	
	@RequestMapping(path={"/","/index"},method= {RequestMethod.GET})
	public String indexof(Model model) {
		List<Question> questionList = questionService.getLastedQuestions(0, 0, 10);
		List<ViewObject> vos=new ArrayList<ViewObject>();
		for(Question question:questionList){
			ViewObject vo=new ViewObject();
			vo.set("question",question);
			vo.set("user", userService.getUser(question.getUserId()));
									  
			vos.add(vo);
		}
		model.addAttribute("vos",vos);
		
		return "index";
	}
	
	@RequestMapping(path= {"/user/{userId}"},method={RequestMethod.GET,RequestMethod.POST})
	public String userIdIndex(Model model,@PathVariable("userId") int userId){
		List<Question> questionList = questionService.getLastedQuestions(userId, 0, 10);
		List<ViewObject> vos=new ArrayList<ViewObject>();
		for(Question question:questionList){
			ViewObject vo=new ViewObject();
			vo.set("question",question);
			vo.set("user", userService.getUser(question.getUserId()));
									  
			vos.add(vo);
		}
		
		model.addAttribute("vos",vos);
		return "index";
	}
	


}
