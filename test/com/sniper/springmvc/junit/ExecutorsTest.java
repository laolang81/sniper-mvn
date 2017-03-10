package com.sniper.springmvc.junit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * newFixedThreadPool测试
 * 
 * @author suzhen
 * 
 */
public class ExecutorsTest {
	int j = 0;

	class Sniper extends Thread {

		@Override
		public void run() {
			synchronized (Integer.class) {
				j++;
			}
			System.out.println(j);
		}
	}

	@Test
	public void fixTest() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 1000; i++) {
			executor.execute(new Sniper());
		}
		try {
			// 向学生传达“问题解答完毕后请举手示意！”
			executor.shutdown();
			// 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
			// (所有的任务都结束的时候，返回TRUE)
			if (!executor.awaitTermination(1 * 1000, TimeUnit.MILLISECONDS)) {
				// 超时的时候向线程池中所有的线程发出中断(interrupted)。
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			// awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
			System.out.println("awaitTermination interrupted: " + e);
			executor.shutdownNow();
		}
	}
}
