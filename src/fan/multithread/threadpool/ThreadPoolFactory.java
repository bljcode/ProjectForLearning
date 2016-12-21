package fan.multithread.threadpool;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * 线程池工厂类
 *
 */
public class ThreadPoolFactory {

	/**线程池大小（队列未满的情况下）*/
	public static final int DEFAULT_COREPOOLSIZE = 10;

	/**线程池内最大线程数（队列满的情况下）*/
	public static final int DEFAULT_MAXIMUMPOOLSIZE = 10;

	/**线程等待时间，超时后线程会结束*/
	public static final int DEFAULT_KEEPALIVETIME = 60000;

	/**超时设置的时间单位*/
	public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.MILLISECONDS;

	/**默认队列大小*/
	public static final int DEFAULT_WORKQUEUESIZE = 50;
	
	
	protected static final Map<String, ThreadPool> CreatedThreadPool = new ConcurrentHashMap<String, ThreadPool>();
	

	/**
	 * 创建无界队列线程池
	 * @param key 线程池标识，要求系统内唯一
	 * @param name 名称
	 * @return
	 */
	public static ThreadPool newDefaultSizePool(String key, String name) {

		
		return newPool(key, name, DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new LinkedBlockingQueue<Runnable>(), new AbortPolicy());
	}

	
	/**
	 * 创建有界队列线程池
	 * @param key 线程池标识，要求系统内唯一
	 * @param name 名称
	 * @param queueSize 任务队列大小
	 * @return
	 */
	public static ThreadPool newDefaultSizePool(String key, String name, int queueSize) {

		return newPool(key, name, DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new ArrayBlockingQueue<Runnable>(queueSize), new AbortPolicy());
	}

	
	/**
	 * 创建无界队列线程池
	 * @param key 线程池标识，要求系统内唯一
	 * @param name 名称
	 * @param corePoolSize 线程池大小
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize) {

		return newPool(key, name, corePoolSize, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new LinkedBlockingQueue<Runnable>(), new AbortPolicy());
	}
	
	
	/**
	 * 创建有界队列线程池
	 * @param key 线程池标识，要求系统内唯一
	 * @param name 名称
	 * @param corePoolSize 线程池大小（队列未满的情况下）
	 * @param maximumPoolSize  线程池内最大线程数（队列满的情况下）
	 * @param queueSize 队列大小
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize, int maximumPoolSize, int queueSize) {
		
		return newPool(key, name, corePoolSize, maximumPoolSize, DEFAULT_KEEPALIVETIME, DEFAULT_TIMEUNIT, new ArrayBlockingQueue<Runnable>(queueSize), new AbortPolicy());
	}

	
	/**
	 * 创建线程池，线程池的参数均可控制。
	 * @param key 线程池标识，要求系统内唯一
	 * @param name 名称
	 * @param corePoolSize 线程池大小（队列未满的情况下）
	 * @param maximumPoolSize  线程池内最大线程数（队列满的情况下）
	 * @param keepAliveTime 线程等待时间，超时后线程会结束。
	 * @param unit 超时设置的时间单位
	 * @param workQueue 队列
	 * @param handler 任务被拒绝的回调器
	 * @return
	 */
	public static ThreadPool newPool(String key, String name, int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {

		if(CreatedThreadPool.containsKey(key))
			throw new ThreadPoolCreateException(key + "线程池已被创建过，不能重复创建");
		
		//原本：maximumPoolSize = corePoolSize; 直接注释掉后ThreadPools中newPool形式的线程有32个线程的，比默认DEFAULT_MAXIMUMPOOLSIZE=10大，会报非法参数的错误，所以加个比较
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

