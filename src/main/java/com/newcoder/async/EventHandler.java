package com.newcoder.async;

import java.util.List;

import com.newcoder.async.EventType;

public interface EventHandler {
	void doHandler(EventModel model);
	List<EventType> getSupportEventTypes();
}
