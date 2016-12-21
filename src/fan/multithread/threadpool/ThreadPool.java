package fan.multithread.threadpool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <strong>JDK ThreadPoolExecutor�̳߳����ֲ���:</strong><br>
 * <ol>
 * <li>������е��߳����� corePoolSize����ֱ������µ��߳�ִ�С�</li>
 * <li>������е��̵߳��ڻ���� corePoolSize���� ʼ����ѡ�����������У���������µ��̡߳�</li>
 * <li>����޷������������У��򴴽��µ��̣߳����Ǵ������̳߳��� maximumPoolSize������������£����񽫱��ܾ���</li>
 * </ol>
 * ע�⵽��ô���̳߳��еĲ���maximumPoolSizeֻ�����̳߳صĶ������˲���Ч���� <br> <br> <br>
 * <strong>�Ŷ�������ͨ�ò���:</strong><br>
 * <ol>
 * <li>ֱ���ύ���������е�Ĭ��ѡ����
 * SynchronousQueue����������ֱ���ύ���̶߳����������ǡ��ڴˣ���������ڿ�������������������̣߳�����ͼ�����������н�ʧ�ܣ�
 * ��˻ṹ��һ���µ��̡߳��˲��Կ��Ա����ڴ�����ܾ����ڲ������Ե�����ʱ��������ֱ���ύͨ��Ҫ���޽� maximumPoolSizes
 * �Ա���ܾ����ύ�����񡣵������Գ����������ܴ����ƽ������������ʱ���˲��������޽��߳̾��������Ŀ����ԡ�</li>
 * <li>�޽���С�ʹ���޽���У����磬������Ԥ���������� LinkedBlockingQueue�������������� corePoolSize
 * �̶߳�æʱ�������ڶ����еȴ����������������߳̾Ͳ��ᳬ�� corePoolSize������ˣ�maximumPoolSize
 * ��ֵҲ����Ч�ˡ�����ÿ��������ȫ�������������񣬼�����ִ�л���Ӱ��ʱ���ʺ���ʹ���޽���У����磬�� Web
 * ҳ�������С������Ŷӿ����ڴ���˲̬ͻ�����󣬵������Գ����������ܴ����ƽ������������ʱ���˲��������޽��߳̾��������Ŀ����ԡ�</li>
 * <li>�н���С���ʹ�����޵� maximumPoolSizes ʱ���н���У���
 * ArrayBlockingQueue�������ڷ�ֹ��Դ�ľ������ǿ��ܽ��ѵ����Ϳ��ơ����д�С�����ش�С������Ҫ�໥���ԣ�
 * ʹ�ô��Ͷ��к�С�ͳؿ�������޶ȵؽ��� CPU ʹ���ʡ�����ϵͳ��Դ���������л����������ǿ��ܵ����˹��������������������Ƶ�����������磬���������
 * I/O �߽磩����ϵͳ����Ϊ��������ɵĸ����̰߳���ʱ�䡣ʹ��С�Ͷ���ͨ��Ҫ��ϴ�ĳش�С��CPU
 * ʹ���ʽϸߣ����ǿ����������ɽ��ܵĵ��ȿ���������Ҳ�ή����������</li>
 * </ol>.
 *
 */
public class ThreadPool extends ThreadPoolExecutor {

	/** �̳߳ر�ʶ. */
	private String key;

	/**  �̳߳�����. */
	private String name = null;

	/**  ������Ϣ. */
	private String description = null;

	protected Map<String, ThreadPool> creator;

	/**�����е�ҵ���߳���*/
	protected AtomicInteger activeBusinessThreadCount = new AtomicInteger(0);

	protected long maxTime;

	protected long minTime;

	protected AtomicLong sumTime = new AtomicLong(0);

	protected volatile double avgTime;

	/**
	 * Instantiates a new thread pool.
	 *
	 * @param name the name
	 * @param description the description
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param workQueue the work queue
	 * @param handler the handler
	 */
	protected ThreadPool(String key, String name, int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		init(key, name);
	}

	/**
	 * Instantiates a new thread pool.
	 *
	 * @param name the name
	 * @param description the description
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param workQueue the work queue
	 * @param threadFactory the thread factory
	 * @param handler the handler
	 */
	protected ThreadPool(String key, String name, int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		init(key, name);
	}

	/**
	 * Instantiates a new thread pool.
	 *
	 * @param the thre name
	 * @param description the description
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param workQueue the work queue
	 * @param threadFactory the thread factory
	 */
	protected ThreadPool(String key, String name, int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		init(key, name);
	}

	/**
	 * Instantiates a new thread pool.
	 *
	 * @param name the name
	 * @param description the description
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param workQueue the work queue
	 */
	protected ThreadPool(String key, String name, int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		init(key, name);
	}

	/**
	 * Inits the.
	 *
	 * @param name the name
	 * @param description the description
	 */
	protected void init(String key, String name) {
		this.key = key;
		this.name = name;

		if (this.key == null || this.key.trim().length() < 1)
			throw new RuntimeException("�̳߳�KEY����Ϊ�գ�");

		if (this.name == null || this.name.trim().length() < 1)
			throw new RuntimeException("�̳߳����Ʋ���Ϊ�գ�");
	}

	@Override
	public void execute(Runnable command) {
		final RunnableFade commandFade = new RunnableFade(command, this);
		super.execute(commandFade);

	}

	protected void logUsedTime(long usedTime) {
		sumTime.addAndGet(usedTime);
		synchronized (this) {
			if (usedTime > maxTime)
				maxTime = usedTime;
			else if (usedTime < minTime || minTime == 0)
				minTime = usedTime;
		}
	}

	@Override
	public void shutdown() {
		clearCreator();
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		clearCreator();
		return super.shutdownNow();
	}

	private void clearCreator() {
		creator.remove(key);
		creator = null;
	}

	protected void setCreator(Map<String, ThreadPool> creator) {
		this.creator = creator;
	}

	/**
	 * Gets the �̳߳ر�ʶ.
	 *
	 * @return the �̳߳ر�ʶ
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the �̳߳�����.
	 *
	 * @return the �̳߳�����
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the ������Ϣ.
	 *
	 * @return the ������Ϣ
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the ������Ϣ.
	 *
	 * @param description the new ������Ϣ
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the �����е�ҵ���߳���.
	 *
	 * @return the �����е�ҵ���߳���
	 */
	public AtomicInteger getActiveBusinessThreadCount() {
		return activeBusinessThreadCount;
	}

	/**
	 * 
	 */
	public void calAvgTime() {
		avgTime = this.getCompletedTaskCount() != 0 ? sumTime.get() / this.getCompletedTaskCount() : 0d;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public long getMinTime() {
		return minTime;
	}

	public double getAvgTime() {
		return avgTime;
	}

}
