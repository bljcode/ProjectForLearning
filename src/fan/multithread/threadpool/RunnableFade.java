package fan.multithread.threadpool;

/**
 * Runnable��װ��.
 * <p>�߳�����ʱ���������̳߳ص�ǰ���е�ҵ���߳���{@linkplain RunnableFade run}����  </p>
 */
public class RunnableFade implements Runnable {
	
	/** �����װ���߳�. */
	private Runnable task;
	
	
	/** �������е��̳߳�. */
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
		//��������������
		Thread.currentThread().setName(parent.getName());
		//�����̳߳ػ�߳�����1
		parent.activeBusinessThreadCount.incrementAndGet();
		try {
			long startTime = System.currentTimeMillis();
			task.run();
			parent.logUsedTime(System.currentTimeMillis() - startTime);
		} finally {
			//�����̳߳ػ�߳�����1
			parent.activeBusinessThreadCount.decrementAndGet();
		}

	}
	

}
