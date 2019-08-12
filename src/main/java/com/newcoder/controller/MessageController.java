package com.newcoder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newcoder.model.HostHolder;
import com.newcoder.model.Message;
import com.newcoder.model.User;
import com.newcoder.model.ViewObject;
import com.newcoder.service.MessageService;
import com.newcoder.service.UserService;
import com.newcoder.util.WendaUtil;

@Controller
public class MessageController {
	private static final Logger logger=LoggerFactory.getLogger(MessageController.class);

	@Autowired
	HostHolder hostHolder;
	@Autowired
	UserService userService;
	@Autowired
	MessageService messageService;
	
	
	
	@RequestMapping(path= {"/msg/list"})
	public String getConversationList(Model model) {
		try {
			if(hostHolder.getUser()==null) {
				return "redirect:/reglogin";
			}
			int localUserId=hostHolder.getUser().getId();
//			System.out.println(localUserId);
		    List<Message> listMessage=messageService.getConversationList(localUserId, 0, 10);
		    List<ViewObject> messagevos=new ArrayList<ViewObject>();
			for(Message message: listMessage) {
				ViewObject viewObject=new ViewObject();
				viewObject.set("conversation", message);
				//对方的id是谁
				int targetId=message.getFromId()==localUserId?message.getToId():message.getFromId();
				//想看到的是对方用户的信息
				viewObject.set("user",userService.selectUserById(targetId));
				viewObject.set("unread", messageService.getConversationUnreadCount(message.getConversationId(), localUserId));
				messagevos.add(viewObject);
			}
			model.addAttribute("conversations",messagevos);
			
		}catch(Exception e) {
			logger.error("获取详情失败"+e.getMessage());
		}
		return "letter";
	}
	 
	
	@RequestMapping(path = { "/msg/detail" }) public String getConversationDetail(Model model,
			@RequestParam("conversationId") String conversationId) {
		  try { 
			  List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
			  List<ViewObject> messages=new ArrayList<ViewObject>();
			  for(Message message:messageList){
				  ViewObject vo=new ViewObject();
				  vo.set("message",message);
				  vo.set("user",userService.getUser(message.getFromId()));
				  messages.add(vo);
			  }
			  model.addAttribute("messages",messages);
		  
		  } catch (Exception e) { 
			  logger.error("获取详情失败" + e.getMessage());
		  }
		  
		  	return "letterDetail";
		  
	}
		 
	 
	
	@RequestMapping(path= {"/msg/addMessage"})
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName,
			              @RequestParam("content") String content) {
		try {
			if(hostHolder.getUser()==null) {
				return WendaUtil.getJOSNString(999,"未登录");
			}
			//userService.selectByName(toName) 由特定的name查询到的userid
			User user=userService.selectByName(toName);
			if(user==null) {
				return WendaUtil.getJOSNString(1,"用户不存在");
			}
			Message message=new Message();
			message.setContent(content);
			message.setCreatedDate(new Date());
			//hostHolder.getUser().getId() 为当前的用户id
			message.setFromId(hostHolder.getUser().getId());
			message.setToId(user.getId());
		    messageService.addMessage(message);
			return WendaUtil.getJOSNString(0);
			
		}catch(Exception e) {
			logger.error("发送消息失败"+e.getMessage());
			return WendaUtil.getJOSNString(1,"发信失败");
		}
	}
}
