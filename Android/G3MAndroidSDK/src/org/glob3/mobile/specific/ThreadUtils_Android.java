

package org.glob3.mobile.specific;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IThreadUtils;


public final class ThreadUtils_Android
         extends
            IThreadUtils {

   private final G3MWidget_Android  _widgetAndroid;
   private final ThreadPoolExecutor _backgroundExecutor;


   public ThreadUtils_Android(final G3MWidget_Android widgetAndroid) {
      if (widgetAndroid == null) {
         throw new IllegalArgumentException("widgetAndroid can't be null");
      }
      _widgetAndroid = widgetAndroid;

      final BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();
      _backgroundExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.DAYS, workQueue);
   }


   @Override
   public void invokeInRendererThread(final GTask task,
                                      final boolean autoDelete) {
      if (task == null) {
         throw new IllegalArgumentException("task can't be null");
      }

      _widgetAndroid.queueEvent(new Runnable() {
         @Override
         public void run() {
            task.run();
            if (autoDelete) {
               task.dispose();
            }
         }
      });
   }


   @Override
   public void invokeInBackground(final GTask task,
                                  final boolean autoDelete) {
      _backgroundExecutor.execute(new Runnable() {
         @Override
         public void run() {
            task.run();
            if (autoDelete) {
               task.dispose();
            }
         }
      });
   }

}
