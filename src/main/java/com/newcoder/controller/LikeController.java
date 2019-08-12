
package com.newcoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Entity;

import com.newcoder.async.EventModel;
import com.newcoder.async.EventProducer;
import com.newcoder.async.EventType;
import com.newcoder.model.Comment;
import com.newcoder.model.EntityType;
import com.newcoder.model.HostHolder;
import com.newcoder.service.CommentService;
import com.newcoder.service.LikeService;
import com.newcoder.util.WendaUtil;

@Controller


public class LikeController {
	
	@Autowired
	HostHolder  hostHolder;
	@Autowired
	LikeService  likeService;
	@Autowired
	CommentService  commentService;
	@Autowired
	EventProducer eventProducer;
	  
	
	
	  @RequestMapping(path= {"/like"},method= {RequestMethod.POST})
	  @ResponseBody
	   public String like(@RequestParam("commentId") int commentId) {
		  if(hostHolder.getUser()==null) {
			  return WendaUtil.getJOSNString(999);
		  }
		  
		  Comment comment=commentService.getCommentById(commentId);
		  eventProducer.fireEvent(new EventModel(EventType.LIKE).
				  setActorId(hostHolder.getUser().getId()).setEntityId(commentId).
				  setEntityType(EntityType.ENTITY_COMMENT).
				  setExt("questionId",String.valueOf(comment.getEntityId())).
				  setEntityOwnerId(comment.getUserId()));
		  long likeCount =likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		  return WendaUtil.getJOSNString(0, String.valueOf(likeCount));
	  }
	  
	  
	  @RequestMapping(path= {"/dislike"},method= {RequestMethod.POST})
	  @ResponseBody
	  public String dislike(@RequestParam("commentId") int commentId) {
		  if(hostHolder.getUser()==null) {
			  return WendaUtil.getJOSNString(999);
		  }
		  long dislikeCount =likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		  System.out.println( WendaUtil.getJOSNString(0, String.valueOf(dislikeCount)));
		  return WendaUtil.getJOSNString(0, String.valueOf(dislikeCount));
	  }
	  

}
