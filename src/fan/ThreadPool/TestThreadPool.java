package fan.ThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TestThreadPool {
	
	private final static ThreadPool minuteKlineHistoryBuildPool = ThreadPoolFactory.newPool("minuteKlineHistoryBuildCache", "分钟K线历史数据处理线程池", 10);
	int threadNum = 5; 
	public static void main( String args[] ){
		 
		 TestThreadPool test = new TestThreadPool();
		 //test.testThreadPool();
		// test.testNewThread();
		 test.testNewPool();
	 }
	 
	 public void testThreadPool(){
		 RunTask r_task = new RunTask();
		 for (int i = 0; i < threadNum; i++) {
			 minuteKlineHistoryBuildPool.execute(r_task);
			}
		// minuteKlineHistoryBuildPool.execute(r_task);
	 }
	/* testThreadPool 输出； 封装的线程池可见execute时setname,而且是set的封装线程池的名字，又可见线程可重名
	 * thread exit : 13   name: 分钟K线历史数据处理线程池
	 thread exit : 11   name: 分钟K线历史数据处理线程池
	 thread exit : 10   name: 分钟K线历史数据处理线程池
	 thread exit : 9   name: 分钟K线历史数据处理线程池
	 thread exit : 12   name: 分钟K线历史数据处理线程池*/
	 
	 public void testNewThread(){
		 RunTask r_task = new RunTask();
		//10个线程
		for(int i = 0; i < threadNum; i++) {
			new Thread(r_task).start();
		}
	 }
	 /*testNewThread输出
	  * thread exit : 13   name: Thread-4
	 thread exit : 9   name: Thread-0
	 thread exit : 10   name: Thread-1
	 thread exit : 12   name: Thread-3
	 thread exit : 11   name: Thread-2*/
	 
	 public void testNewPool(){
		 RunTask r_task = new RunTask();
		 
		 ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadNum, threadNum*2,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		 //execute一次只有一个线程；要理解线程池是里面有多个线程，给一个任务一个线程去执行；
		 //threadPool.execute(r_task);
		 for (int i = 0; i < threadNum; i++) {
				threadPool.execute(r_task);
			}
		 //手动关闭；不手动关闭，通过java visualVM查看线程输出thread exit（只是任务结束完了，不是线程终止了） 后线程一直处于监视状态
		 threadPool.shutdown();
	 }
	/* testNewPool输出
	 * thread exit : 13   name: pool-2-thread-5
	 thread exit : 9   name: pool-2-thread-1
	 thread exit : 12   name: pool-2-thread-4
	 thread exit : 11   name: pool-2-thread-3
	 thread exit : 10   name: pool-2-thread-2*/

	 
	 
	 
	 
}
