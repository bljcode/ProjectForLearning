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
 * <strong>JDK ThreadPoolExecutor线程池三种策略:</strong><br>
 * <ol>
 * <li>如果运行的线程少于 corePoolSize，则直接添加新的线程执行。</li>
 * <li>如果运行的线程等于或多于 corePoolSize，则 始终首选将请求加入队列，而不添加新的线程。</li>
 * <li>如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。</li>
 * </ol>
 * 注意到了么，线程池中的参数maximumPoolSize只有在线程池的队列满了才有效果。 <br> <br> <br>
 * <strong>排队有三种通用策略:</strong><br>
 * <ol>
 * <li>直接提交。工作队列的默认选项是
 * SynchronousQueue，它将任务直接提交给线程而不保持它们。在此，如果不存在可用于立即运行任务的线程，则试图把任务加入队列将失败，
 * 因此会构造一个新的线程。此策略可以避免在处理可能具有内部依赖性的请求集时出现锁。直接提交通常要求无界 maximumPoolSizes
 * 以避免拒绝新提交的任务。当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。</li>
 * <li>无界队列。使用无界队列（例如，不具有预定义容量的 LinkedBlockingQueue）将导致在所有 corePoolSize
 * 线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 corePoolSize。（因此，maximumPoolSize
 * 的值也就无效了。）当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web
 * 页服务器中。这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。</li>
 * <li>有界队列。当使用有限的 maximumPoolSizes 时，有界队列（如
 * ArrayBlockingQueue）有助于防止资源耗尽，但是可能较难调整和控制。队列大小和最大池大小可能需要相互折衷：
 * 使用大型队列和小型池可以最大限度地降低 CPU 使用率、操作系统资源和上下文切换开销，但是可能导致人工降低吞吐量。如果任务频繁阻塞（例如，如果它们是
 * I/O 边界），则系统可能为超过您许可的更多线程安排时间。使用小型队列通常要求较大的池大小，CPU
 * 使用率较高，但是可能遇到不可接受的调度开销，这样也会降低吞吐量。</li>
 * </ol>.
 *
 */
public class ThreadPool extends ThreadPoolExecutor {

	/** 线程池标识. */
	private String key;

	/**  线程池名称. */
	private String name = null;

	/**  描述信息. */
	private String description = null;

	protected Map<String, ThreadPool> creator;

	/**运行中的业务线程数*/
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
			throw new RuntimeException("线程池KEY不能为空！");

		if (this.name == null || this.name.trim().length() < 1)
			throw new RuntimeException("线程池名称不能为空！");
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
	 * Gets the 线程池标识.
	 *
	 * @return the 线程池标识
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the 线程池名称.
	 *
	 * @return the 线程池名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the 描述信息.
	 *
	 * @return the 描述信息
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the 描述信息.
	 *
	 * @param description the new 描述信息
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the 运行中的业务线程数.
	 *
	 * @return the 运行中的业务线程数
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
