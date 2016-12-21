package fan.ThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

public class RunTask implements Runnable {
	private static AtomicInteger count = new AtomicInteger(100);
	@Override
	public void run() {
		
		while(count.intValue() > 0){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			count.decrementAndGet();
		}
		System.out.println("thread exit : " + Thread.currentThread().getId()
				+ "   name: " + Thread.currentThread().getName());
		
	}
}
