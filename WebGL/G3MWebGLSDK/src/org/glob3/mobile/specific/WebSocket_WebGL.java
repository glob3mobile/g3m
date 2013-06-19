

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;


public class WebSocket_WebGL
         extends
            IWebSocket {

   WebSocket_WebGL(final URL url,
                   final IWebSocketListener listener,
                   final boolean autodeleteListener,
                   final boolean autodeleteWebSocket) {
      super(url, listener, autodeleteListener, autodeleteWebSocket);
   }


   @Override
   public void send(final String message) {
      throw new RuntimeException("NOT YET IMPLEMENTED!");
   }


   @Override
   public void close() {
      throw new RuntimeException("NOT YET IMPLEMENTED!");
   }


}
