package fan.ThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TestThreadPool {
	
	private final static ThreadPool minuteKlineHistoryBuildPool = ThreadPoolFactory.newPool("minuteKlineHistoryBuildCache", "����K����ʷ���ݴ����̳߳�", 10);
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
	/* testThreadPool ����� ��װ���̳߳ؿɼ�executeʱsetname,������set�ķ�װ�̳߳ص����֣��ֿɼ��߳̿�����
	 * thread exit : 13   name: ����K����ʷ���ݴ����̳߳�
	 thread exit : 11   name: ����K����ʷ���ݴ����̳߳�
	 thread exit : 10   name: ����K����ʷ���ݴ����̳߳�
	 thread exit : 9   name: ����K����ʷ���ݴ����̳߳�
	 thread exit : 12   name: ����K����ʷ���ݴ����̳߳�*/
	 
	 public void testNewThread(){
		 RunTask r_task = new RunTask();
		//10���߳�
		for(int i = 0; i < threadNum; i++) {
			new Thread(r_task).start();
		}
	 }
	 /*testNewThread���
	  * thread exit : 13   name: Thread-4
	 thread exit : 9   name: Thread-0
	 thread exit : 10   name: Thread-1
	 thread exit : 12   name: Thread-3
	 thread exit : 11   name: Thread-2*/
	 
	 public void testNewPool(){
		 RunTask r_task = new RunTask();
		 
		 ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadNum, threadNum*2,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		 //executeһ��ֻ��һ���̣߳�Ҫ����̳߳��������ж���̣߳���һ������һ���߳�ȥִ�У�
		 //threadPool.execute(r_task);
		 for (int i = 0; i < threadNum; i++) {
				threadPool.execute(r_task);
			}
		 //�ֶ��رգ����ֶ��رգ�ͨ��java visualVM�鿴�߳����thread exit��ֻ������������ˣ������߳���ֹ�ˣ� ���߳�һֱ���ڼ���״̬
		 threadPool.shutdown();
	 }
	/* testNewPool���
	 * thread exit : 13   name: pool-2-thread-5
	 thread exit : 9   name: pool-2-thread-1
	 thread exit : 12   name: pool-2-thread-4
	 thread exit : 11   name: pool-2-thread-3
	 thread exit : 10   name: pool-2-thread-2*/

	 
	 
	 
	 
}
