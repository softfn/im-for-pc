package com.oim.common.task;



import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * @author XiaHui
 * @date 2014年9月12日 下午2:17:36
 */

public class QueueTaskThread extends Thread {
	private final Logger logger = Logger.getLogger(this.getClass());
	private ConcurrentLinkedQueue<ExecuteTask> executeTaskQueue = new ConcurrentLinkedQueue<ExecuteTask>();// 任务队列
	private long sleepTime = 200;// 线程休眠时间
	private ExecutorService pool = Executors.newFixedThreadPool(10);

	public QueueTaskThread(){
		this.start();
	}
	/**
	 * 添加任务
	 * 
	 * @param executeTask
	 */
	public void add(ExecuteTask executeTask) {
		executeTaskQueue.add(executeTask);
	}

	@Override
	public void run() {
		while (true) {
			handleTask();// 处理任务
			threadSleep(sleepTime);
		}
	}

	private void threadSleep(long time) {
		try {
			sleep(time);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	/**
	 * 处理任务队列，检查其中是否有任务
	 */
	private void handleTask() {
		try {
			ExecuteTask executeTask;
			while (executeTaskQueue.peek() != null) {
				executeTask = executeTaskQueue.poll();
				handleTask(executeTask);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 执行任务操作
	 * 
	 * @param executeTask
	 */
	private void handleTask(ExecuteTask executeTask) {
		pool.execute(new ExecuteRunnable(executeTask));
	}

	class ExecuteRunnable implements Runnable {
		ExecuteTask executeTask;

		ExecuteRunnable(ExecuteTask executeTask) {
			this.executeTask = executeTask;
		}

		public void run() {
			executeTask.execute();
		}
	}
}
