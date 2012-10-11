

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IThreadUtils;


public final class ThreadUtils_Android
         extends
            IThreadUtils {

   private final G3MWidget_Android _widgetAndroid;


   public ThreadUtils_Android(final G3MWidget_Android widgetAndroid) {
      if (widgetAndroid == null) {
         throw new IllegalArgumentException("widgetAndroid can't be null");
      }
      _widgetAndroid = widgetAndroid;
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
         }
      });

   }

}
