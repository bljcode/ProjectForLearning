package fan.multithread.threadpool;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * �̳߳ع�����
 *
 */
public class ThreadPoolFactory {

	/**�̳߳ش�С������δ��������£�*/
	public static final int DEFAULT_COREPOOLSIZE = 10;

	/**�̳߳�������߳�����������������£�*/
	public static final int DEFAULT_MAXIMUMPOOLSIZE = 10;

	/**�̵߳ȴ�ʱ�䣬��ʱ���̻߳����*/
	public static final int DEFAULT_KEEPALIVETIME = 60000;

	/**��ʱ���õ�ʱ�䵥λ*/
	public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.MILLISECONDS;

	/**Ĭ�϶��д�С*/
	public static final int DEFAULT_WORKQUEUESIZE = 50;
	
	
	protected static final Map<String, ThreadPool> CreatedThreadPool = new ConcurrentHashMap<String, ThreadPool>();
	

	/**
	 * �����޽�����̳߳�
	 * @param key �̳߳ر�ʶ��Ҫ��ϵͳ��Ψһ
	 * @param name ����
	 * @return
	 */
	public static ThreadPool newDefaultSizePool(String key, String name) {

		
		return newPool(key, name, DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new LinkedBlockingQueue<Runnable>(), new AbortPolicy());
	}

	
	/**
	 * �����н�����̳߳�
	 * @param key �̳߳ر�ʶ��Ҫ��ϵͳ��Ψһ
	 * @param name ����
	 * @param queueSize ������д�С
	 * @return
	 */
	public static ThreadPool newDefaultSizePool(String key, String name, int queueSize) {

		return newPool(key, name, DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new ArrayBlockingQueue<Runnable>(queueSize), new AbortPolicy());
	}

	
	/**
	 * �����޽�����̳߳�
	 * @param key �̳߳ر�ʶ��Ҫ��ϵͳ��Ψһ
	 * @param name ����
	 * @param corePoolSize �̳߳ش�С
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize) {

		return newPool(key, name, corePoolSize, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new LinkedBlockingQueue<Runnable>(), new AbortPolicy());
	}
	
	
	/**
	 * �����н�����̳߳�
	 * @param key �̳߳ر�ʶ��Ҫ��ϵͳ��Ψһ
	 * @param name ����
	 * @param corePoolSize �̳߳ش�С������δ��������£�
	 * @param maximumPoolSize  �̳߳�������߳�����������������£�
	 * @param queueSize ���д�С
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize, int maximumPoolSize, int queueSize) {
		
		return newPool(key, name, corePoolSize, maximumPoolSize, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new ArrayBlockingQueue<Runnable>(queueSize), new AbortPolicy());
	}

	
	/**
	 * �����̳߳أ��̳߳صĲ������ɿ��ơ�
	 * @param key �̳߳ر�ʶ��Ҫ��ϵͳ��Ψһ
	 * @param name ����
	 * @param corePoolSize �̳߳ش�С������δ��������£�
	 * @param maximumPoolSize  �̳߳�������߳�����������������£�
	 * @param keepAliveTime �̵߳ȴ�ʱ�䣬��ʱ���̻߳������
	 * @param unit ��ʱ���õ�ʱ�䵥λ
	 * @param workQueue ����
	 * @param handler ���񱻾ܾ��Ļص���
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {

		if(CreatedThreadPool.containsKey(key))
			throw new ThreadPoolCreateException(key + "�̳߳��ѱ��������������ظ�����");
		
		//ԭ����maximumPoolSize = corePoolSize; ֱ��ע�͵���ThreadPools��newPool��ʽ���߳���32���̵߳ģ���Ĭ��DEFAULT_MAXIMUMPOOLSIZE=10�󣬻ᱨ�Ƿ������Ĵ������ԼӸ��Ƚ�
		if(maximumPoolSize < corePoolSize){
			maximumPoolSize = corePoolSize;
		}
		ThreadPool pool = new ThreadPool(key, name, corePoolSize, maximumPoolSize, keepAliveTime, unit,
				workQueue, handler);
		pool.setCreator(CreatedThreadPool);
		CreatedThreadPool.put(key, pool);
		return pool;
	}
	
}

