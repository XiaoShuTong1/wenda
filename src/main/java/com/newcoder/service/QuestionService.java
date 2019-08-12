package com.newcoder.service;

import java.util.List;

import javax.servlet.http.HttpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.newcoder.dao.QuestionDao;
import com.newcoder.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDao questionDao;
	
	@Autowired
	SensitiveService sensitiveService;
	
	
	public Question getById(int id) {
        return questionDao.getById(id);
    }
	public int addQuestion(Question question){
		
		  question.settitle(HtmlUtils.htmlEscape(question.gettitle()));
		  question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		//敏感词过滤
		  question.settitle(sensitiveService.filter(question.gettitle()));
		  question.setContent(sensitiveService.filter(question.getContent()));
		return questionDao.addQuestion(question)>0? question.getId():0;
		
	}
	
	public List<Question> getLastedQuestions(int userId,int offset,int limit){
		return questionDao.selectLastedQuestions(userId, offset, limit);
	}
	
	public int updateCommentCount(int id, int commentId ) {
		return questionDao.updateCommentCount(id, commentId);
	}

}
