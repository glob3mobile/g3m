

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IThreadUtils;

import com.google.gwt.user.client.Timer;


public final class ThreadUtils_WebGL
         extends
            IThreadUtils {

   private final int _delayMillis;


   public ThreadUtils_WebGL(final int delayMillis) {
      _delayMillis = delayMillis;
   }


   private void invokeTask(final GTask task,
                           final boolean autoDelete) {
      final Timer timer = new Timer() {
         @Override
         public void run() {
            task.run(_context);
            if (autoDelete) {
               task.dispose();
            }
         }
      };
      timer.schedule(_delayMillis);
   }


   @Override
   public void invokeInRendererThread(final GTask task,
                                      final boolean autoDelete) {
      invokeTask(task, autoDelete);
   }


   @Override
   public void invokeInBackground(final GTask task,
                                  final boolean autoDelete) {
      invokeTask(task, autoDelete);
   }


   @Override
   public void onResume(final G3MContext context) {
   }


   @Override
   public void onPause(final G3MContext context) {
   }


   @Override
   public void onDestroy(final G3MContext context) {
   }

}
