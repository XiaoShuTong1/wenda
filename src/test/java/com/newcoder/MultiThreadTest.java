package com.newcoder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;




class Consumer implements Runnable{
	private BlockingQueue<String> block;
	public Consumer(BlockingQueue<String> block) {
		super();
		this.block = block;
	}
	@Override
	public void run() {
		try {
			while(true) {
				System.out.println(Thread.currentThread().getName()+":"+block.take());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}

class Producer implements Runnable{
	private BlockingQueue<String> block;
	public Producer(BlockingQueue<String> block) {
		super();
		this.block = block;
	}
	@Override
	public void run() {
		try {
			for (int i = 0; i <100; i++) {
				Thread.sleep(1000);
			block.put(String.valueOf(i));
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}

public class MultiThreadTest {

public static void testBlockingQueue() {
	BlockingQueue<String> block=new ArrayBlockingQueue<String>(10);
	new Thread(new Producer(block)).start();
	new Thread(new Consumer(block),"consumer1").start();
	new Thread(new Consumer(block),"consumer2").start();
}
public static void main(String[] args) {
	testBlockingQueue();
}

}
