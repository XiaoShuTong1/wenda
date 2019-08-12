package com.newcoder.async;

import java.util.HashMap;
import java.util.Map;

import com.newcoder.async.EventType;
import com.newcoder.async.EventModel;

public class EventModel {
	   private EventType type;//事件发生的现场  点赞
	   private int actorId;//事件触发者  谁点的赞
	   private int entityType;//事件的载体 给什么东西点赞
	   private int entityId;//事件的载体 给什么东西点赞
	   private int entityOwnerId;//事件载体关联的ID
	 //扩展 字段，上面的字段其实也可以存到这个里面的，但是这些字段通用所以直接定义
	   private Map<String, String> exts = new HashMap<String, String>();

	    public EventModel() {

	    }

	    public EventModel setExt(String key, String value) {
	        exts.put(key, value);
	        return this;
	    }

	    public EventModel(EventType type) {
	        this.type = type;
	    }

	    public String getExt(String key) {
	        return exts.get(key);
	    }


	    public EventType getType() {
	        return type;
	    }

	    public EventModel setType(EventType type) {
	        this.type = type;
	        return this;
	    }

	    public int getActorId() {
	        return actorId;
	    }

	    public EventModel setActorId(int actorId) {
	        this.actorId = actorId;
	        return this;
	    }

	    public int getEntityType() {
	        return entityType;
	    }

	    public EventModel setEntityType(int entityType) {
	        this.entityType = entityType;
	        return this;
	    }

	    public int getEntityId() {
	        return entityId;
	    }

	    public EventModel setEntityId(int entityId) {
	        this.entityId = entityId;
	        return this;
	    }

	    public int getEntityOwnerId() {
	        return entityOwnerId;
	    }

	    public EventModel setEntityOwnerId(int entityOwnerId) {
	        this.entityOwnerId = entityOwnerId;
	        return this;
	    }

	    public Map<String, String> getExts() {
	        return exts;
	    }

	    public EventModel setExts(Map<String, String> exts) {
	        this.exts = exts;
	        return this;
	    }
	
}
