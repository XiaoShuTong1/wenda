package com.newcoder.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newcoder.async.EventHandler;
import com.newcoder.async.EventModel;
import com.newcoder.async.EventType;
import com.newcoder.model.Message;
import com.newcoder.model.User;
import com.newcoder.service.MessageService;
import com.newcoder.service.UserService;
import com.newcoder.util.WendaUtil;

@Component
public class LikeHandler implements EventHandler {

	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;
	
	@Override
	public void doHandler(EventModel model) {
		Message message =new Message();
		message.setFromId(WendaUtil.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		User user=userService.selectUserById(model.getActorId());
		message.setContent("用户"+user.getName()+"赞了你的评论, http://localhost:8080/question/"+model.getExt("questionId"));
		
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}

}
