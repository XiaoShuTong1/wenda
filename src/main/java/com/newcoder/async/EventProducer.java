package com.newcoder.async;
/*
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.newcoder.util.JedisAdpater;
import com.newcoder.util.RedisKeyUtil;

@Service
public class EventProducer {
	@Autowired
	JedisAdpater jedisAdpater;
	
	public boolean fireEvent(EventModel eventModel){
		try {
			//以前的队列使用，我们在这儿使用redis
			//BlockingQueue<EventModel> q=new ArrayBlockingQueue<EventModel>(0);
			String json=JSONObject.toJSONString(eventModel);
			String key=RedisKeyUtil.getEventQueueKey();
			jedisAdpater.lpush(key, json);
			
			return true;
		} catch (Exception e) {
			return false;
		} 
		
	}

}
