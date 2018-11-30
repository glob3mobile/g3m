

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IThreadUtils;


public final class ThreadUtils_Android
         extends
            IThreadUtils {


   private final G3MWidget_Android  _widgetAndroid;
   private final ThreadPoolExecutor _backgroundExecutor;
   private boolean                  _running = true;

   private final List<Runnable> _backgroundQueue     = new ArrayList<Runnable>();
   private final List<Runnable> _rendererThreadQueue = new ArrayList<Runnable>();


   public ThreadUtils_Android(final G3MWidget_Android widgetAndroid) {
      this(widgetAndroid, Math.max(1, Runtime.getRuntime().availableProcessors() / 2));
   }


   public ThreadUtils_Android(final G3MWidget_Android widgetAndroid,
                              final int numBackgroundThreads) {
      if (widgetAndroid == null) {
         throw new IllegalArgumentException("widgetAndroid can't be null");
      }
      _widgetAndroid = widgetAndroid;

      final BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();
      _backgroundExecutor = new ThreadPoolExecutor(numBackgroundThreads, numBackgroundThreads, 1, TimeUnit.DAYS, workQueue);
   }


   @Override
   public synchronized void invokeInRendererThread(final GTask task,
                                                   final boolean autoDelete) {
      if (task == null) {
         throw new IllegalArgumentException("task can't be null");
      }

      final Runnable runnable = new Runnable() {
         @Override
         public void run() {
            task.run(getContext());
            if (autoDelete) {
               task.dispose();
            }
         }
      };

      if (isRunning()) {
         _widgetAndroid.queueEvent(runnable);
      }
      else {
         _rendererThreadQueue.add(runnable);
      }
   }


   @Override
   public synchronized void invokeInBackground(final GTask task,
                                               final boolean autoDelete) {
      final Runnable runnable = new Runnable() {
         @Override
         public void run() {
            task.run(getContext());
            if (autoDelete) {
               task.dispose();
            }
         }
      };

      if (isRunning()) {
         _backgroundExecutor.execute(runnable);
      }
      else {
         _backgroundQueue.add(runnable);
      }
   }


   @Override
   public synchronized void onResume(final G3MContext context) {
      if (!_running) {
         _running = true;
         tryToDrainQueues();
      }
   }


   private void tryToDrainQueues() {
      if (isRunning()) {
         drainQueues();
      }
   }


   private boolean isRunning() {
      return _running && isInitialized();
   }


   private void drainQueues() {
      for (final Runnable runnable : _backgroundQueue) {
         _backgroundExecutor.execute(runnable);
      }
      _backgroundQueue.clear();

      for (final Runnable runnable : _rendererThreadQueue) {
         _widgetAndroid.queueEvent(runnable);
      }
      _rendererThreadQueue.clear();
   }


   @Override
   public synchronized void onPause(final G3MContext context) {
      _running = false;
   }


   @Override
   public void onDestroy(final G3MContext context) {
      onPause(context);
   }


   @Override
   protected void justInitialized() {
      tryToDrainQueues();
   }


}
