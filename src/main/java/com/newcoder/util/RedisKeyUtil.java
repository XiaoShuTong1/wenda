package com.newcoder.util;

public class RedisKeyUtil {
	private static String SPLIT="";
	private static String BIZ_LIKE="Like";
	private static String BIZ_DISLIKE="DISLIKE";
	private static String BIZ_EVENTQUEUE="EVENT_QUEUE";
	
	public static String getLikKey(int entityType,int entityId) {
		return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
	
	public static String getDisLikKey(int entityType,int entityId) {
		return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
	}
	public static String getEventQueueKey(){
		return BIZ_EVENTQUEUE;
	}

}
