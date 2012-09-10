

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;

import com.google.gwt.user.client.Timer;


public class ThreadUtils_WebGL
         extends
            IThreadUtils {

   private final int       _delayMillis;
   private G3MWidget_WebGL _g3mWidget = null;


   public ThreadUtils_WebGL(final G3MWidget_WebGL w,
                            final int delayMillis) {
      _g3mWidget = w;
      _delayMillis = delayMillis;
   }


   @Override
   public void invokeInRendererThread(final GTask task,
                                      final boolean autoDelete) {
      if (_g3mWidget != null) {
         final GTask t = task;

         final Timer timer = new Timer() {
            @Override
            public void run() {
               t.run();
            }
         };

         timer.schedule(_delayMillis);
      }
      else {
         ILogger.instance().logError("ThreadUtils_WebGL: _g3mWidget is null");
      }
   }

}
