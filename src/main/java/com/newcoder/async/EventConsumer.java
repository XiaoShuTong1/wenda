package com.newcoder.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.newcoder.async.EventHandler;
import com.newcoder.async.EventType;
import com.newcoder.async.EventModel;
import com.newcoder.util.JedisAdpater;
import com.newcoder.util.RedisKeyUtil;

@Service
public class EventConsumer implements InitializingBean ,ApplicationContextAware{
	private static final Logger logger=LoggerFactory.getLogger(EventConsumer.class);
	@Autowired
	JedisAdpater jedisAdpater;
	
	private Map<EventType,List<EventHandler>> config=new HashMap<EventType,List<EventHandler>>();
	private ApplicationContext applicationContext;
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
		if(beans!=null) {
			for(Map.Entry<String, EventHandler> entry:beans.entrySet()) {
				List<EventType> listEventType=entry.getValue().getSupportEventTypes();
				
				for (EventType eventType : listEventType) {
					if(!config.containsKey(eventType)) {
						config.put(eventType, new ArrayList<EventHandler>());
					}
					config.get(eventType).add(entry.getValue());
				}
			}
		}
		
		
Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					String key= RedisKeyUtil.getEventQueueKey();
					List<String> events = jedisAdpater.brpop(0,key);
					for (String message : events) {
						if(message.equals(key)) {
							continue;
						}
						EventModel eventModel=JSON.parseObject(message,EventModel.class);
						if(!config.containsKey(eventModel.getType())) {
							logger.error("不能识别的事件");
							continue;
						}
						for(EventHandler eventHandler :config.get(eventModel.getType())) {
							eventHandler.doHandler(eventModel);
						}
					}
				}
			}
		});
		
		thread.start();
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	} 

}
