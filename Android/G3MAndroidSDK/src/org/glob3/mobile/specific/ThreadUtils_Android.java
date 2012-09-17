

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;


public class ThreadUtils_Android
         extends
            IThreadUtils {

   private final G3MWidget_Android _g3mWidget;


   public ThreadUtils_Android(final G3MWidget_Android widget) {
      _g3mWidget = widget;
   }


   @Override
   public void invokeInRendererThread(final GTask task,
                                      final boolean autoDelete) {

      if (_g3mWidget == null) {
         ILogger.instance().logError("ThreadUtils_Android ERROR");
      }
      else {
         final GTask t = task;
         _g3mWidget.queueEvent(new Runnable() {
            @Override
            public void run() {
               t.run();
            }
         });
      }
   }

}
