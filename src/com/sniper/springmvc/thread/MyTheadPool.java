package com.sniper.springmvc.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MyTheadPool {

	
	
public MyTheadPool() {
	Executors.newCachedThreadPool();
	Executors.newSingleThreadExecutor();
	Executors.newFixedThreadPool(20);
	
	ExecutorService service = Executors.newCachedThreadPool();
	//service.execute(command);
	ThreadFactory threadFactory = null;
	Executors.newCachedThreadPool(threadFactory);
	Executors.newScheduledThreadPool(10);
}	
}
