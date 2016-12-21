package fan.multithread.threadpool;

/**
 * Runnable包装类.
 * <p>线程运行时可以设置线程池当前运行的业务线程数{@linkplain RunnableFade run}方法  </p>
 */
public class RunnableFade implements Runnable {
	
	/** 保存包装的线程. */
	private Runnable task;
	
	
	/** 任务运行的线程池. */
	private ThreadPool parent;




	/**
	 * Instantiates a new runnable fade.
	 *
	 * @param task the task
	 * @param parent the parent
	 */
	public RunnableFade(Runnable task, ThreadPool parent) {
		super();
		this.task = task;
		this.parent = parent;
	}




	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//这里设置名字了
		Thread.currentThread().setName(parent.getName());
		//运行线程池活动线程数加1
		parent.activeBusinessThreadCount.incrementAndGet();
		try {
			long startTime = System.currentTimeMillis();
			task.run();
			parent.logUsedTime(System.currentTimeMillis() - startTime);
		} finally {
			//运行线程池活动线程数减1
			parent.activeBusinessThreadCount.decrementAndGet();
		}

	}
	

}
