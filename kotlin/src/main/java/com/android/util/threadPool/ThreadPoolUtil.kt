package com.android.util.threadPool

import androidx.annotation.IntDef
import java.util.concurrent.*

/**
 * Created by xuzhb on 2020/12/8
 * Desc:线程池工具类
 */
class ThreadPoolUtil @JvmOverloads constructor(@Type type: Int = CachedThread, corePoolSize: Int = 10) {

    companion object {
        const val FixedThread = 0
        const val CachedThread = 1
        const val SingleThread = 2
        const val ScheduledThread = 3
    }

    @IntDef(FixedThread, CachedThread, SingleThread, ScheduledThread)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Type

    private var mExecutor: ExecutorService? = null
    private var mScheduledExecutor: ScheduledExecutorService? = null

    /**
     * 线程池构造函数
     *
     * @param type         线程池类型
     * @param corePoolSize 核心线程数
     */
    init {
        when (type) {
            FixedThread -> {
                //线程数量固定，只有核心线程并且核心线程不会被回收，这意味着它能够更加快速地响应外界的请求。
                //核心线程没有超时机制，另外任务队列也是没有大小限制的。
                //当线程处于空闲状态时，它们并不会被回收，除非线程池被关闭了。当所有的线程都处于活动状态时，新任务都会处于等待状态，直到有线程空闲出来。
                //ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
                mExecutor = Executors.newFixedThreadPool(corePoolSize)
            }
            CachedThread -> {
                //线程数量不定，只有非核心线程，并且其最大线程数为 Integer.MAX_VALUE。
                //空闲线程都有超时机制，为60秒，超过60秒闲置线程就会被回收；任务队列相当于一个空集合，这将导致任何任务都会立即被执行。
                //当线程池中的线程都处于活动状态时，线程池会创建新的线程来处理新任务，否则就会利用空闲的线程来处理新任务。几乎是不占用任何系统资源的，比较适合执行大量的耗时较少的任务。
                //ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>())
                mExecutor = Executors.newCachedThreadPool()
            }
            SingleThread -> {
                //只有一个核心线程，确保所有的任务都在同一个线程中按顺序执行。
                //意义在于统一所有的外界任务到一个线程中，这使得在这些任务之间不需要处理线程同步的问题。
                //ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
                mExecutor = Executors.newSingleThreadExecutor()
            }
            ScheduledThread -> {
                //核心线程数量是固定的，而非核心线程数是没有限制的，并且当非核心线程闲置时会被立即回收。
                //主要用于执行定时任务和具有固定周期的重复任务。
                //super(corePoolSize, Integer.MAX_VALUE, DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS, new DelayedWorkQueue())
                mScheduledExecutor = Executors.newScheduledThreadPool(corePoolSize)
            }
        }
    }

    //执行一个任务
    fun execute(command: Runnable) {
        if (isScheduledThread()) {
            mScheduledExecutor?.execute(command)
        } else {
            mExecutor?.execute(command)
        }
    }

    //执行指定的任务列表
    fun execute(commands: List<Runnable>) {
        for (command in commands) {
            execute(command)
        }
    }

    /**
     * 执行一个Callable任务
     *
     * @param task 执行的任务
     * @param <T>  任务执行完成后返回结果的类型
     * @return 表示任务等待完成的Future，可以通过Future的get()方法获取任务执行完成后的结果
    </T> */
    fun <T> submit(task: Callable<T>): Future<T>? {
        return if (isScheduledThread()) mScheduledExecutor?.submit(task)
        else mExecutor?.submit(task)
    }

    /**
     * 执行一个Runnable任务
     *
     * @param task   执行的任务
     * @param result 返回的结果
     * @param <T>    返回结果的类型
     * @return 表示任务等待完成的Future，可以通过Future的get方法获取任务执行完成的结果
    </T> */
    fun <T> submit(task: Runnable, result: T): Future<T>? {
        return if (isScheduledThread()) mScheduledExecutor?.submit(task, result)
        else mExecutor?.submit(task, result)
    }

    /**
     * 执行一个Runnable任务
     *
     * @param task 执行的任务
     * @return 表示任务等待完成的Future，该Future的get方法在任务成功完成后会返回null
     */
    fun submit(task: Runnable): Future<*>? {
        if (isScheduledThread()) mScheduledExecutor?.submit(task)
        return mExecutor?.submit(task)
    }

    /**
     * 执行给定的任务，当所有任务完成时，返回保持任务状态和结果的Future列表，列表中每个Future的isDone为true
     * 可以正常地终止或是通过抛出异常来终止已完成的任务
     *
     * @param tasks 任务集合
     * @param <T>   任务执行结果的类型
     * @return 表示任务的Future列表，其中每个任务都已完成
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
    </T> */
    @Throws(InterruptedException::class)
    fun <T> invokeAll(tasks: Collection<Callable<T>>): List<Future<T>>? {
        return if (isScheduledThread()) mScheduledExecutor?.invokeAll(tasks)
        else mExecutor?.invokeAll(tasks)
    }

    /**
     * 执行给定的任务，当所有任务完成或者超时了，返回保持任务状态和结果的Future列表，列表中每个Future的isDone为true
     * 一旦返回结果，所有尚未完成的任务都会被取消，可以正常地终止或是通过抛出异常来终止已完成的任务
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     任务执行结果的类型
     * @return 表示任务的Future列表，其中每个任务都已完成
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
    </T> */
    @Throws(InterruptedException::class)
    fun <T> invokeAll(
        tasks: Collection<Callable<T>>,
        timeout: Long,
        unit: TimeUnit
    ): List<Future<T>>? {
        return if (isScheduledThread()) mScheduledExecutor?.invokeAll(tasks, timeout, unit)
        else mExecutor?.invokeAll(tasks, timeout, unit)
    }

    /**
     * 执行给定的任务，如果某个任务已成功完成（未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务
     *
     * @param tasks 任务集合
     * @param <T>   任务执行结果的类型
     * @return 某个任务执行的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
    </T> */
    @Throws(InterruptedException::class, ExecutionException::class)
    fun <T> invokeAny(tasks: Collection<Callable<T>>): T? {
        if (isScheduledThread()) mScheduledExecutor?.invokeAny(tasks)
        return mExecutor?.invokeAny(tasks)
    }

    /**
     * 执行给定的任务，如果某个任务已成功完成或者超时了（未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     任务执行结果的类型
     * @return 某个任务执行的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     * @throws TimeoutException     如果在所有任务成功完成之前超时了
    </T> */
    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun <T> invokeAny(
        tasks: Collection<Callable<T>>,
        timeout: Long,
        unit: TimeUnit
    ): T? {
        return if (isScheduledThread()) mScheduledExecutor?.invokeAny(tasks, timeout, unit)
        else mExecutor?.invokeAny(tasks, timeout, unit)
    }

    //待以前提交的任务执行完毕后按顺序关闭线程池，而且不接受新任务，如果已经关闭，则调用无效
    fun shutDown() {
        if (isScheduledThread()) {
            mScheduledExecutor?.shutdown()
        } else {
            mExecutor?.shutdown()
        }
    }

    /**
     * 试图停止所有正在执行的任务，暂停处理正在等待的任务，并返回等待执行的任务列表
     * 无法保证能够停止正在处理的活动执行任务，但是会尽力尝试
     */
    fun shutdownNow(): List<Runnable>? {
        return if (isScheduledThread()) mScheduledExecutor?.shutdownNow()
        else mExecutor?.shutdownNow()
    }

    //判断线程池是否已关闭
    fun isShutDown(): Boolean {
        return if (isScheduledThread()) mScheduledExecutor!!.isShutdown
        else mExecutor!!.isShutdown
    }

    //关闭线程池后判断所有任务是否都已完成，注意：除非首先调用shutdown或shutdownNow，否则isTerminated永远为false
    fun isTerminated(): Boolean {
        return if (isScheduledThread()) mScheduledExecutor!!.isTerminated
        else mExecutor!!.isTerminated
    }

    /**
     * 阻塞当前线程一直到调用shutdown请求后所有任务执行完或等待超时了或是当前线程被中断
     * 一般和shutdown配合使用(在shutdown后调用)，用来判断线程池是否已经关闭
     *
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return true：线程池中的所有任务执行完毕，线程池关闭 false：超过timeout指定的时长后线程池还有任务未执行完
     */
    @Throws(InterruptedException::class)
    fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
        return if (isScheduledThread()) mScheduledExecutor!!.awaitTermination(timeout, unit)
        else mExecutor!!.awaitTermination(timeout, unit)
    }

    //判断构造函数传入的是ScheduledThread(ScheduledExecutorService类型)还是其他类型的线程(ExecutorService类型)
    private fun isScheduledThread(): Boolean {
        return mScheduledExecutor != null
    }

    /**
     * 延迟执行Runnable任务
     *
     * @param command 执行的线程
     * @param delay   延迟的时间
     * @param unit    时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其get()方法在完成后将返回null
     */
    fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*>? {
        return mScheduledExecutor?.schedule(command, delay, unit)
    }

    /**
     * 延迟执行Callable任务
     *
     * @param callable 执行的线程
     * @param delay    延迟的时间
     * @param unit     时间单位
     * @return 可用于提取结果或取消的ScheduledFuture
     */
    fun <V> schedule(
        callable: Callable<V>,
        delay: Long,
        unit: TimeUnit
    ): ScheduledFuture<V>? {
        return mScheduledExecutor?.schedule(callable, delay, unit)
    }

    /**
     * 延迟并以指定间隔时间循环执行任务
     *
     * @param command      执行的线程
     * @param initialDelay 首次执行的延迟时间
     * @param period       连续执行之间的周期
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其get()方法在取消后将抛出异常
     */
    fun scheduleWithFixedRate(
        command: Runnable,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit
    ): ScheduledFuture<*>? {
        return mScheduledExecutor?.scheduleAtFixedRate(command, initialDelay, period, unit)
    }

    /**
     * 延迟并以固定休息时间循环执行命令
     *
     * @param command      执行的线程
     * @param initialDelay 首次执行的延迟时间
     * @param delay        前一次执行结束到下一次执行开始的间隔时间
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其get()方法在取消后将抛出异常
     */
    fun scheduleWithFixedDelay(
        command: Runnable,
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit
    ): ScheduledFuture<*>? {
        return mScheduledExecutor?.scheduleWithFixedDelay(command, initialDelay, delay, unit)
    }

    //取消所有Future的执行
    fun <V> cancel(vararg futures: Future<V>) {
        for (f in futures) {
            f.cancel(true)
        }
    }

    //取消所有Future的执行
    fun <V> cancel(futures: List<Future<V>>) {
        for (f in futures) {
            f.cancel(true)
        }
    }

}
