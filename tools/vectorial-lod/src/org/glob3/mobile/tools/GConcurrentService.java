

package org.glob3.mobile.tools;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GConcurrentService {

   public static final int     DEFAULT_TASK_PRIORITY = Thread.NORM_PRIORITY;
   public static final int     DEFAULT_MAX_PRIORITY  = Thread.MAX_PRIORITY;
   public static final int     DEFAULT_MIN_PRIORITY  = Thread.MIN_PRIORITY;

   public static final int     DEFAULT_MAX_POOL_SIZE = 5;

   public static final long    TASK_WAIT_PERIOD      = 100;
   public static final long    ALIVE_PERIOD          = 2000;

   public static final boolean VERBOSE               = false;

   private final int           _groupsNumber;
   //private final int        _maxTaskPerGroup;
   private final TaskGroup     _tasksPool[];
   private final boolean       _groupSwapping;

   public static int           AVAILABLE_PROCESSORS  = Runtime.getRuntime().availableProcessors();


   public interface TaskCompleteListener {
      void taskCompleted(final Thread thread);
   }

   public static class SmartThread
            extends
               Thread {

      private final Set<TaskCompleteListener> _listeners = new CopyOnWriteArraySet<TaskCompleteListener>();


      public SmartThread(final Runnable runnable,
                         final String string) {
         super(runnable, string);
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
            //doRun();
         }
         finally {
            notifyListeners();
         }
      }

      //public abstract void doRun();
   }


   public interface GroupCompleteListener {
      void taskCompleted(final TaskGroup group);
   }


   public static class TaskGroup {

      private final AtomicInteger              _taskCounter;
      private final AtomicInteger              _maxPoolSize;
      private final AtomicInteger              _taskPriority;
      private final String                     _namePrefix;
      private final String                     _groupId;

      private final Set<GroupCompleteListener> _listeners = new CopyOnWriteArraySet<GroupCompleteListener>();


      private TaskGroup(final int maxPoolSize,
                        final int taskPriority,
                        final String namePrefix) {

         _maxPoolSize = new AtomicInteger(maxPoolSize);
         _namePrefix = namePrefix;
         _groupId = getGroupIndex();
         _taskPriority = new AtomicInteger(taskPriority);
         _taskCounter = new AtomicInteger(0);
      }


      public static TaskGroup createNewGroup(final int maximumPoolSize,
                                             final int taskPriority,
                                             final String namePrefix) {

         return new TaskGroup(maximumPoolSize, taskPriority, namePrefix);
      }


      public static TaskGroup createDefaultGroup(final String namePrefix) {

         return createNewGroup(DEFAULT_MAX_POOL_SIZE, DEFAULT_TASK_PRIORITY, namePrefix);
      }


      public void setPriority(final int newPriority) {

         _taskPriority.set(newPriority);
      }


      public void setMaxPoolSize(final int newMaxPoolSize) {

         _maxPoolSize.set(newMaxPoolSize);
      }


      public void incrementPoolSize(final int inc) {

         _maxPoolSize.set(_maxPoolSize.get() + inc);
      }


      public synchronized void execute(final Runnable runnable) {

         final SmartThread t = new SmartThread(runnable, _namePrefix + _taskCounter.get() + 1);

         final TaskCompleteListener listener = new TaskCompleteListener() {
            @Override
            public void taskCompleted(final Thread thread) {
               decrementTaskPool();
            }
         };

         t.addListener(listener);
         t.setDaemon(true);
         t.setPriority(_taskPriority.get());

         try {
            while (getTaskCount() >= getPoolSize()) {
               Thread.sleep(TASK_WAIT_PERIOD);
            }
         }
         catch (final InterruptedException e) {
            e.printStackTrace();
         }

         t.start();
         incrementTaskPool();
      }


      public int getTaskCount() {
         return _taskCounter.get();
      }


      public synchronized void addListener(final GroupCompleteListener listener) {
         _listeners.add(listener);
      }


      public synchronized void removeListener(final GroupCompleteListener listener) {
         _listeners.remove(listener);
      }


      public boolean isAlive() {
         return getTaskCount() > 0;
      }


      public int getPoolSize() {
         return _maxPoolSize.get();
      }


      private void incrementTaskPool() {
         _taskCounter.incrementAndGet();
         if (VERBOSE) {
            System.out.println("Running process at group[" + _groupId + "]: " + getTaskCount());
         }
      }


      private void decrementTaskPool() {
         _taskCounter.decrementAndGet();
         if (VERBOSE) {
            System.out.println("Pending process at group[" + _groupId + "]: " + getTaskCount());
         }
         if (getTaskCount() < 1) {
            if (VERBOSE) {
               System.out.println("Go to awaiting..");
            }
            awaitGroupTermination();
         }
      }


      private final void notifyListeners() {
         for (final GroupCompleteListener listener : _listeners) {
            listener.taskCompleted(this);
         }
      }


      private void awaitGroupTermination() {

         try {
            //wait for one second before notify listeners
            if (VERBOSE) {
               System.out.println("Awaiting group termination[" + _groupId + "]");
            }
            Thread.sleep(ALIVE_PERIOD);
            if (getTaskCount() < 1) {
               if (VERBOSE) {
                  System.out.println("notify listeners..");
               }
               notifyListeners();
            }
         }
         catch (final InterruptedException e) {
            e.printStackTrace();
         }
      }


      private String getGroupIndex() {
         final String[] splited = _namePrefix.split("-");

         try {
            final int index = Integer.parseInt(splited[splited.length - 1]);
            return String.valueOf(index);
         }
         catch (final Exception e) {
            Logger.getLogger(TaskGroup.class.getName()).log(Level.WARNING, "Invalid group index");
         }

         return _namePrefix;
      }

   } // class TaskGroup


   public static GConcurrentService createDefaultConcurrentService(final int groupsNumber,
                                                                   final String namePrefix) {

      return new GConcurrentService(groupsNumber, DEFAULT_MAX_POOL_SIZE, DEFAULT_TASK_PRIORITY, namePrefix, true);
   }


   public static GConcurrentService createConcurrentService(final int groupsNumber,
                                                            final int maxTaskPerGroup,
                                                            final int taskPriority,
                                                            final String namePrefix,
                                                            final boolean groupSwapping) {

      return new GConcurrentService(groupsNumber, maxTaskPerGroup, taskPriority, namePrefix, groupSwapping);
   }


   private GConcurrentService(final int groupsNumber,
                              final int maxTaskPerGroup,
                              final int taskPriority,
                              final String namePrefix,
                              final boolean groupSwapping) {

      _groupsNumber = groupsNumber;
      _groupSwapping = groupSwapping;
      _tasksPool = new TaskGroup[_groupsNumber];
      inintializeGroupPool(maxTaskPerGroup, taskPriority, namePrefix);
   }


   public void execute(final Runnable runnable,
                       final int groupId) {

      if ((groupId < 0) || (groupId >= _groupsNumber)) {
         final String msg = "Run process at invalid Group Id: " + groupId;
         Logger.getLogger(GConcurrentService.class.getName()).log(Level.WARNING, msg, groupId);
         return;
      }
      _tasksPool[groupId].execute(runnable);
   }


   public synchronized void awaitTermination() {
      try {
         while (getTotalTaskCount() > 0) {
            Thread.sleep(ALIVE_PERIOD);
         }
      }
      catch (final InterruptedException e) {
         e.printStackTrace();
      }
   }


   private void inintializeGroupPool(final int maximumPoolSize,
                                     final int threadPriority,
                                     final String namePrefix) {

      for (int i = 0; i < _groupsNumber; i++) {
         final String groupName = namePrefix + "-" + i;
         _tasksPool[i] = TaskGroup.createNewGroup(maximumPoolSize, threadPriority, groupName);

         final GroupCompleteListener listener = new GroupCompleteListener() {
            @Override
            public void taskCompleted(final TaskGroup group) {
               if (VERBOSE) {
                  System.out.println("Group [" + group._groupId + "] task completed.");
               }
               checkForTaskSwapping(group);
            }
         };
         _tasksPool[i].addListener(listener);
      }
   }


   private synchronized void checkForTaskSwapping(final TaskGroup groupId) {

      if (!_groupSwapping) {
         if (VERBOSE) {
            System.out.println("Swapping not allowd: " + groupId._groupId);
         }
         return;
      }

      if (getAliveGroups() < 1) {
         if (VERBOSE) {
            System.out.println("All groups has completed task " + groupId._groupId);
         }
         return;
      }

      final int aliveGroups = getAliveGroups();
      final int poolInc = groupId.getPoolSize() / aliveGroups;
      final int poolRest = groupId.getPoolSize() % aliveGroups;

      if (VERBOSE) {
         System.out.println("Swapping from group: " + groupId._groupId);
         System.out.println("Alive groups: " + aliveGroups + ", poolInc: " + poolInc + " ,poolRest: " + poolRest);
      }

      boolean firstAlive = true;
      for (final TaskGroup group : _tasksPool) {
         if (group._namePrefix.equals(groupId._namePrefix)) {
            continue;
         }
         if (group.isAlive()) {
            if (firstAlive) {
               group.incrementPoolSize(poolInc + poolRest);
               firstAlive = false;
            }
            else {
               group.incrementPoolSize(poolInc);
            }
         }
      }

   }


   private synchronized int getTotalTaskCount() {

      int count = 0;
      for (final TaskGroup group : _tasksPool) {
         count += group.getTaskCount();
      }
      return count;
   }


   private synchronized int getAliveGroups() {

      int count = 0;
      for (final TaskGroup group : _tasksPool) {
         if (group.getTaskCount() > 0) {
            count++;
         }
      }
      return count;
   }

}
