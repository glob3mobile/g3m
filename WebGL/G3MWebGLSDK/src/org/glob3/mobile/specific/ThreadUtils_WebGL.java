package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IThreadUtils;

public class ThreadUtils_WebGL extends IThreadUtils {

   private G3MWidget_WebGL _g3mWidget = null;


   public ThreadUtils_WebGL(G3MWidget_WebGL w) {
      _g3mWidget = w;
   }


   @Override
   public void invokeInRendererThread(GTask task,
                                      boolean autoDelete) {
      // TODO this method must be implemented

//      if (_g3mWidget != null) {
//         final GTask t = task;
//         _g3mWidget.queueEvent(new Runnable() {
//            @Override
//            public void run() {
//               t.run();
//            }
//         });
//
//      }
//      else {
//         ILogger.instance().logError("ThreadUtils_WebGL _g3mWidget is null");
//      }

   }

}
