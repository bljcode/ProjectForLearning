package testIdea;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestRunnable implements Runnable {
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	@Override
	public void run() {
		// TODO Auto-generated method
		//这里的queue因为是BlockingQueue所以是线程安全的
		while(queue != null && (!queue.isEmpty())){
			String str = queue.poll();
			//放一起是为了看输出时不被其他线程的插入，好辨别
			System.out.println("thread : " + Thread.currentThread().getId() + " running " + " get str : " + str);
		}
	}
	public void setQueue(BlockingQueue<String> strqueue) {
		while(!strqueue.isEmpty()){
			queue.add(strqueue.poll());
		}
	}
	

}

