
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.user.client.*;

public final class ThreadUtils_WebGL extends IThreadUtils {

   private final int _delayMillis;

   public ThreadUtils_WebGL(final int delayMillis) {
      _delayMillis = delayMillis;
   }

   private void invokeTask(final GTask task, final boolean autoDelete) {
      final Timer timer = new Timer() {
         @Override
         public void run() {
            task.run(getContext());
            if (autoDelete) {
               task.dispose();
            }
         }
      };
      timer.schedule(_delayMillis);
   }

   @Override
   public void invokeInRendererThread(final GTask task, final boolean autoDelete) {
      //invokeTask(task, autoDelete);
      task.run(getContext());
      if (autoDelete) {
         task.dispose();
      }
   }

   @Override
   public void invokeInBackground(final GTask task, final boolean autoDelete) {
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

   @Override
   protected void justInitialized() {

   }

}
