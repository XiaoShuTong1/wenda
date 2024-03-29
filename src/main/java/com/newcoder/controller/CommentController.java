package com.newcoder.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newcoder.model.Comment;
import com.newcoder.model.EntityType;
import com.newcoder.model.HostHolder;
import com.newcoder.service.CommentService;
import com.newcoder.service.QuestionService;
import com.newcoder.util.WendaUtil;

@Controller
public class CommentController {
	
	private static final Logger logger=LoggerFactory.getLogger(CommentController.class);
	@Autowired
	   CommentService  commentService;
	   @Autowired
	   HostHolder  hostHolder;
	   @Autowired
	   QuestionService  questionService;

	   @RequestMapping(path= {"/addComment"})
	   public String addComment(@RequestParam("questionId") int questionId,
			              @RequestParam("content")  String content  ) {
		try {
		   Comment comment=new Comment();
		   comment.setContent(content);
		   comment.setCreatedDate(new Date());
		   comment.setEntityId(questionId);
		   comment.setEntityType(EntityType.ENTITY_QUESTION);
		   comment.setStatus(0);
		   if(hostHolder.getUser()!=null) {
			   comment.setUserId(hostHolder.getUser().getId());
		   }else {
			   comment.setUserId(WendaUtil.ANONYMOUS_USERID);
			   //return "redirect:/reglogin";
		   }
		   System.out.println("comment"+comment);
		   commentService.addComment(comment);
		   int count=commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
		   questionService.updateCommentCount(comment.getEntityId(), count);
		   
		}catch(Exception e) {
			logger.error("增加评论失败"+e.getMessage());
		}
		   return "redirect:/question/"+String.valueOf(questionId);
	   }

}
