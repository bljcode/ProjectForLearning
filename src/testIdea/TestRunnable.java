package testIdea;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestRunnable implements Runnable {
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	@Override
	public void run() {
		// TODO Auto-generated method
		//�����queue��Ϊ��BlockingQueue�������̰߳�ȫ��
		while(queue != null && (!queue.isEmpty())){
			String str = queue.poll();
			//��һ����Ϊ�˿����ʱ���������̵߳Ĳ��룬�ñ��
			System.out.println("thread : " + Thread.currentThread().getId() + " running " + " get str : " + str);
		}
	}
	public void setQueue(BlockingQueue<String> strqueue) {
		while(!strqueue.isEmpty()){
			queue.add(strqueue.poll());
		}
	}
	

}

