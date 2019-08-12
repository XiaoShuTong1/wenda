package com.newcoder;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newcoder.dao.QuestionDao;
import com.newcoder.dao.UserDao;
import com.newcoder.model.Question;
import com.newcoder.model.User;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Wenda6Application.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;
	
	
	@Test
	public void initDatabase(){
		Random ran=new Random();
		for (int i = 0; i <11; ++i) {
			User user=new User();
			user.setHeadurl(String.format("http://images.nowcoder.com/head/%dt.png", ran.nextInt(1000)));
			user.setName(String.format("USER1%d",i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);
			
			user.setPassword("xx");
			userDao.updatePassword(user);
			
			Question question=new Question();
			question.setCommentCount(i);
			question.setContent(String.format("Balalalal%d",i));
			Date date=new Date();
			date.setTime(date.getTime()+1000*36*i);
			question.setCreatedDate(date);
			question.settitle(String.format("TITLE%d", i));
			question.setUserId(i+1);
			
			questionDao.addQuestion(question);
			
			
		}
		//Assert.assertEquals("xx", userDao.selectById(1).getPassword());
		//userDao.deleteById(1);
		//Assert.assertNull(userDao.selectById(1));
		
		System.out.println(questionDao.selectLastedQuestions(0, 0, 10));
	}
	@Test
	public void initDatabase1(){
		Random ran=new Random();
		User user=new User();
		user.setHeadurl(String.format("http://images.nowcoder.com/head/%dt.png",ran.nextInt(1000)));
		user.setName(String.format("USER1%d",1));
		user.setPassword("123");
		user.setSalt("你好");
		userDao.addUser(user);
	}
	

}
