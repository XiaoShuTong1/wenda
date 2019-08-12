package com.newcoder.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newcoder.controller.MessageController;
import com.newcoder.model.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;


@Service
public class JedisAdpater implements InitializingBean{
	private static final Logger logger=LoggerFactory.getLogger(JedisAdpater.class);
	private JedisPool pool;
	public static void print(int index,Object obj){
		System.out.println(String.format("[%d] %s",index,obj.toString()));
	}
	public static void main(String[] args) {
		Jedis jedis=new Jedis("redis://localhost:6379/9");
		jedis.flushDB();
		 jedis.set("hello","world");
		 
		 print(1,jedis.get("hello"));
		 jedis.rename("hello","newhello");
		 
		
		 
		 jedis.setex("hello2", 30, "world2");//可以设置过期时间
		 
		 jedis.set("pv","120");
		 jedis.incr("pv");
		 print(2,jedis.get("pv"));//自动加一
		 jedis.incrBy("pv", 5);
		 print(3,jedis.get("pv"));//自动加5
		 jedis.decr("pv");
		 print(4,jedis.get("pv"));//自动减一
		 
		 jedis.decrBy("pv",3);
		 print(5,jedis.get("pv"));//自动减三
		 
		 print(6,jedis.keys("*"));
		 
		 String listName="list";
		 jedis.del(listName);
		 for (int i = 0; i <10; i++) {
			jedis.lpush(listName,"a"+String.valueOf(i));
		} 
		 print(7,jedis.lrange(listName, 0, 12));
		 print(8,jedis.lrange(listName, 0,4));
		 print(9,jedis.llen(listName));
		 print(10,jedis.lpop(listName));
		 print(11,jedis.llen(listName));
		// print(12,jedis.linsert(listName, BinaryClient.LIST_POSITION.After, pivot, value));
		 //hash值
		 
		 String userkey="xx";
		 jedis.hset(userkey, "name","tom");
		 jedis.hset(userkey, "age","12");
		 jedis.hset(userkey, "sex","man");
		 jedis.hset(userkey, "phone","134257817238");
		 
		 print(12,jedis.hgetAll(userkey));
		 jedis.hdel(userkey, "sex");
		 print(13,jedis.hgetAll(userkey));
		 print(14,jedis.hexists(userkey, "phone"));
		 print(14,jedis.hexists(userkey, "sex"));
		 
		 System.out.println("==========set集合=======");  
		 //set集合
		 String likekey1="commentLike1";
		 String likekey2="commentLike2";
		 for (int i = 0; i <10; i++) {
			 jedis.sadd(likekey1,String.valueOf(i));
			 jedis.sadd(likekey2,String.valueOf(i*i));
		}
		 print(15,jedis.smembers(likekey1));
		 print(16,jedis.smembers(likekey2));

		 print(17,jedis.sunion(likekey1, likekey2));//求并
		 print(18,jedis.sdiff(likekey1,likekey2));//  第一个集合有的，第二个集合没有的
		 print(19,jedis.sdiff(likekey2,likekey1));// 第一个集合有的，第二个集合没有的
		 print(20,jedis.sinter(likekey1,likekey2));//求交
		 print(21,jedis.sismember(likekey1,"0"));//求是否是成员
		 print(22,jedis.sismember(likekey2,"99"));//求是否是成员
		 jedis.srem(likekey1, "0");
		 print(23,jedis.smembers(likekey1));
		 
		 
		 jedis.smove(likekey2, likekey1, "25");//移动元素
		 print(24,jedis.smembers(likekey1));
		  print(25,jedis.smembers(likekey2));
		  print(25,jedis.scard(likekey1));//统计个数
		  print(25,jedis.scard(likekey2));
		  
		System.out.println("==========优先队列=======");  
		  //优先队列 有序集合 元素中有权重的概念
		  String rankkey="rankkey";
		jedis.zadd(rankkey,39,"Tom") ;
		jedis.zadd(rankkey,88,"Jim") ;
		jedis.zadd(rankkey,92,"Lucy") ;
		jedis.zadd(rankkey,76,"Goh") ;
		jedis.zadd(rankkey,77,"Alom") ;
		print(26,jedis.zcard(rankkey));//计数
		print(27,jedis.zcount(rankkey, 60, 100));//统计及格人数
		print(28,jedis.zscore(rankkey, "Lucy"));//查某个人的分数
		print(29,jedis.keys(rankkey));//查全部人的分数？？
		print(30,jedis.zincrby(rankkey, 4, "Lucy"));//给Lucy加4分
		print(31,jedis.zrange(rankkey, 0, 100));//0到100名
		print(32,jedis.zrange(rankkey, 1, 3));//1到3名 低到高
		print(33,jedis.zrevrange(rankkey, 0, 5));//0到5名 高到低
		print(33,jedis.zrevrange(rankkey, 1, 5));//1到5名 高到低
		 
		for(Tuple tuple:jedis.zrangeByScoreWithScores(rankkey, "60", "100")){
			print(34,tuple.getElement()+":"+tuple.getScore());
		}
		 
		print(35,jedis.zrank(rankkey,"Goh"));
		print(36,jedis.zrevrank(rankkey,"Goh"));
		 
		System.out.println("=======分子相同根据字典序排序=========="); 
		String setKey="zset";
		jedis.zadd(setKey,1,"a");
		jedis.zadd(setKey,1,"b");
		jedis.zadd(setKey,1,"c");
		jedis.zadd(setKey,1,"d");
		jedis.zadd(setKey,1,"e");
		jedis.zadd(setKey,1,"f");
		jedis.zadd(setKey,1,"g");
		
		print(37,jedis.zlexcount(setKey, "-", "+"));
		print(38,jedis.zlexcount(setKey, "[b", "[f"));
		print(39,jedis.zlexcount(setKey, "[a", "[e"));
		print(40,jedis.zrange(setKey, 0, 10));
		print(40,jedis.zrevrange(setKey, 0, 10));
		
		//jedis.sadd("pv", "112");
		//jedis.get("pv");
		print(41,jedis.get("pv"));
		System.out.println("=======Java中jedis的链接池功能=========="); 
		
		/*
		 * JedisPool jp = new JedisPool("9"); for (int i = 0; i < 10; ++i) { Jedis j =
		 * jp.getResource(); print(41, j.get("pv")); j.close(); }
		 */  //报错

		User user=new User();
		user.setHeadurl("a.png");
		user.setId(111);
		user.setName("usei1");
		user.setPassword("password1");
		user.setSalt("salt");
		jedis.set("user",JSONObject.toJSONString(user));//将对象序列化
		print(42,jedis.get("user"));
		
		
		User user2=JSON.parseObject(jedis.get("user"), User.class);//将json字符串反序列化成对象
		//System.out.println(user2);
		print(43,user2);
}
	@Override
	public void afterPropertiesSet() throws Exception {
		pool=new JedisPool("redis://localhost:6379/10");
	}
	public long sadd(String key,String value){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
		}finally {
			if(jedis!=null) {
			jedis.close();
			}
		}
		return 0;
	}
	
	
	public long srem(String key,String value){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.srem(key, value);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
		}finally {
			if(jedis!=null) {
			jedis.close();
			}
		}
		return 0;
	}
	
	
	public long scard(String key){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
		}finally {
			if(jedis!=null) {
			jedis.close();
			}
		}
		return 0;
	}
	
	public boolean sismember(String key,String value){
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("发生异常"+e.getMessage());
		}finally {
			if(jedis!=null) {
			jedis.close();
			}
		}
		return false;
	}
	
	
	
	public long lpush(String key,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.lpush(key,value);
		}catch(Exception e) {
			logger.error("发生异常"+e.getMessage());
		
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
		return 0;
	}
	
	
	public List<String> brpop(int  timeout,String key) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.brpop(timeout,key);
		}catch(Exception e) {
			logger.error("发生异常"+e.getMessage());
		
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
		return null;
	}
}
