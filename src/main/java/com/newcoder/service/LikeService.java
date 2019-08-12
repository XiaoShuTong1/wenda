package com.newcoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcoder.util.JedisAdpater;
import com.newcoder.util.RedisKeyUtil;

@Service
public class LikeService {
	@Autowired
	JedisAdpater jedisAdpater;
	public long getLikeCount(int entityType,int entityId) {
		 String likeKey=RedisKeyUtil.getLikKey(entityType, entityId);
		  return jedisAdpater.scard(likeKey);
	  }
	
	 public int getLikeStatus(int userId,int entityType,int entityId) {
		 String likeKey=RedisKeyUtil.getLikKey(entityType, entityId);
		  if(jedisAdpater.sismember(likeKey, String.valueOf(userId))) {
			  return 1;
		  }
		  String dislikeKey=RedisKeyUtil.getDisLikKey(entityType, entityId);
		 return jedisAdpater.sismember(dislikeKey, String.valueOf(userId))?-1:0;
	  }
	
	public long like(int userId,int entityType,int entityId){
		String likeKey=RedisKeyUtil.getLikKey(entityType, entityId);
		jedisAdpater.sadd(likeKey, String.valueOf(userId));
		
		String dislikeKey=RedisKeyUtil.getDisLikKey(entityType, entityId);
		jedisAdpater.srem(dislikeKey, String.valueOf(userId));
		return jedisAdpater.scard(likeKey);
	}
	
	public long disLike(int userId,int entityType,int entityId){
		String dislikeKey=RedisKeyUtil.getDisLikKey(entityType, entityId);
		jedisAdpater.sadd(dislikeKey, String.valueOf(userId));
		
		String likeKey=RedisKeyUtil.getLikKey(entityType, entityId);
		jedisAdpater.srem(likeKey, String.valueOf(userId));
		
		
		return jedisAdpater.scard(likeKey);
	}

}
