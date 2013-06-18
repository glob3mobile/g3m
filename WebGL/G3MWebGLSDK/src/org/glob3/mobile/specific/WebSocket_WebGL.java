

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;


public class WebSocket_WebGL
         extends
            IWebSocket {

   WebSocket_WebGL(final URL url,
                   final IWebSocketListener listener,
                   final boolean autodeleteListener) {
      super(url, listener, autodeleteListener);
   }

}
