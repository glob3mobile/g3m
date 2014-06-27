

package org.glob3.mobile.tools;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class GConcurrentService {

   public static final boolean      VERBOSE        = false;
   public static final long         ALIVE_PERIOD   = 4000;
   private static final int         SCALE_FACTOR   = 2;

   private final static AtomicLong  _threadCounter = new AtomicLong(0);
   private final static AtomicLong  _taskCounter   = new AtomicLong(0);
   private final ThreadPoolExecutor _executor;
   private int                      _maxThreads    = Runtime.getRuntime().availableProcessors();


   public interface ThreadCompleteListener {
      void threadCompleted(final Thread thread);
   }


   public static class SmartThread
            extends
               Thread {

      private final Set<ThreadCompleteListener> _listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();


      public SmartThread(final ThreadGroup group,
                         final Runnable target,
                         final String name,
                         final long stackSize) {
         super(group, target, name, stackSize);
      }


      public final void addListener(final ThreadCompleteListener listener) {
         _listeners.add(listener);
      }


      public final void removeListener(final ThreadCompleteListener listener) {
         _listeners.remove(listener);
      }


      private final void notifyListeners() {
         for (final ThreadCompleteListener listener : _listeners) {
            listener.threadCompleted(this);
         }
      }


      @Override
      public final void run() {
         try {
            super.run();
         }
         finally {
            notifyListeners();
         }
      }
   }

   public interface TaskCompleteListener {
      void taskCompleted(final Runnable task);
   }

   public static class SmartTask
            implements
               Runnable {

      final Runnable                          _task;

      private final Set<TaskCompleteListener> _listeners = new CopyOnWriteArraySet<TaskCompleteListener>();


      public SmartTask(final Runnable task) {
         _task = task;

         _taskCounter.incrementAndGet();
         //System.out.println("Pending: " + _taskCounter.get());

         final TaskCompleteListener listener = new TaskCompleteListener() {
            @Override
            public void taskCompleted(final Runnable ltask) {
               _taskCounter.decrementAndGet();
               //System.out.println("Ending.. " + _taskCounter.get());
            }
         };

         this.addListener(listener);
      }


      private void doRun() {
         _task.run();
      }


      public final void addListener(final TaskCompleteListener listener) {
         _listeners.add(listener);
      }


      public final void removeListener(final TaskCompleteListener listener) {
         _listeners.remove(listener);
      }


      private final void notifyListeners() {
         for (final TaskCompleteListener listener : _listeners) {
            listener.taskCompleted(this);
         }
      }


      @Override
      public final void run() {
         try {
            doRun();
         }
         finally {
            notifyListeners();
         }
      }
   }


   private static class DefaultThreadFactory
            implements
               ThreadFactory {
      private static final AtomicInteger poolNumber    = new AtomicInteger(1);

      private final ThreadGroup          _group;
      private final AtomicInteger        _threadNumber = new AtomicInteger(1);
      private final String               _namePrefix;
      private final int                  _threadPriority;


      DefaultThreadFactory(final int threadPriority) {
         final SecurityManager s = System.getSecurityManager();
         _group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
         _namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
         _threadPriority = threadPriority;
      }


      @Override
      public Thread newThread(final Runnable runnable) {

         final SmartThread t = new SmartThread(_group, runnable, _namePrefix + _threadNumber.getAndIncrement(), 0);

         _threadCounter.incrementAndGet();
         //System.out.println("Started: " + _taskCounter.incrementAndGet());

         final ThreadCompleteListener listener = new ThreadCompleteListener() {
            @Override
            public void threadCompleted(final Thread thread) {
               _threadCounter.decrementAndGet();
               //System.out.println("Pending.. " + _taskCounter.decrementAndGet());
               //System.out.println("Ending: " + Thread.currentThread().getName());
            }
         };

         t.addListener(listener);
         t.setDaemon(false);
         t.setPriority(_threadPriority);

         return t;
      }

   }


   private static ThreadFactory defaultThreadFactory(final int threadPriority) {
      return new DefaultThreadFactory(threadPriority);
   }


   public GConcurrentService() {

      this(SCALE_FACTOR);
   }


   public GConcurrentService(final int scaleFactor) {

      final int cpus = Runtime.getRuntime().availableProcessors();
      _maxThreads = Math.max(cpus * scaleFactor, 1);

      _executor = new ThreadPoolExecutor(0, _maxThreads, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
               defaultThreadFactory(Thread.NORM_PRIORITY));
      _executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
   }


   public void execute(final Runnable task) {
      //System.out.println("Started: " + _taskCounter.incrementAndGet());
      final SmartTask smartTask = new SmartTask(task);
      _executor.execute(smartTask);
   }


   public void awaitTermination() {

      try {
         while (_taskCounter.get() > 0) {
            //System.out.println("Waiting at thread: " + Thread.currentThread().getName());
            Thread.sleep(ALIVE_PERIOD);
         }

         _executor.shutdown();
         _executor.awaitTermination(1, TimeUnit.DAYS);
      }
      catch (final InterruptedException e) {
         throw new RuntimeException(e);
      }
   }


   public int getThreadsNumber() {
      return _maxThreads;
   }

}
