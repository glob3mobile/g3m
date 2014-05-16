

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

   public static final boolean      VERBOSE      = false;
   public static final long         ALIVE_PERIOD = 5000;

   private final ThreadPoolExecutor _executor;
   private final static AtomicLong  _taskCounter = new AtomicLong(0);


   public interface TaskCompleteListener {
      void taskCompleted(final Thread thread);
   }

   public static class SmartThread
            extends
               Thread {

      private final Set<TaskCompleteListener> _listeners = new CopyOnWriteArraySet<TaskCompleteListener>();


      public SmartThread(final ThreadGroup group,
                         final Runnable target,
                         final String name,
                         final long stackSize) {
         super(group, target, name, stackSize);
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
            super.run();
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

         _taskCounter.incrementAndGet();
         //System.out.println("Started: " + _taskCounter.incrementAndGet());

         final TaskCompleteListener listener = new TaskCompleteListener() {
            @Override
            public void taskCompleted(final Thread thread) {
               _taskCounter.decrementAndGet();
               //System.out.println("Pending.. " + _taskCounter.decrementAndGet());
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

      final int scaleFactor = 2;
      final int cpus = Runtime.getRuntime().availableProcessors();
      final int maxThreads = Math.max(cpus * scaleFactor, 1);

      _executor = new ThreadPoolExecutor(0, maxThreads, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
               defaultThreadFactory(Thread.NORM_PRIORITY));
      _executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
   }


   public void execute(final Runnable task) {
      //System.out.println("Started: " + _taskCounter.incrementAndGet());
      _executor.execute(task);
   }


   public void awaitTermination() {

      try {
         while (_taskCounter.get() > 0) {
            Thread.sleep(ALIVE_PERIOD);
         }

         _executor.shutdown();
         _executor.awaitTermination(4, TimeUnit.DAYS);
      }
      catch (final InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

}
