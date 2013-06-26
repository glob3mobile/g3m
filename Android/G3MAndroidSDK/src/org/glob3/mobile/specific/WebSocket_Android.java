

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;


public class WebSocket_Android
         extends
            IWebSocket {


   private WebSocketConnection _connection;


   WebSocket_Android(final URL url,
                     final IWebSocketListener listener,
                     final boolean autodeleteListener,
                     final boolean autodeleteWebSocket) {
      super(url, listener, autodeleteListener, autodeleteWebSocket);

      _connection = new WebSocketConnection();

      final WebSocketHandler handler = new WebSocketHandler() {
         @Override
         public void onOpen() {
            getListener().onOpen(WebSocket_Android.this);
         }


         @Override
         public void onTextMessage(final String message) {
            getListener().onMesssage(WebSocket_Android.this, message);
         }


         @Override
         public void onRawTextMessage(final byte[] message) {
            getListener().onError(WebSocket_Android.this, "Raw Text messages not yet supported");
         }


         @Override
         public void onClose(final int code,
                             final String reason) {
            getListener().onClose(WebSocket_Android.this);
            if (getAutodeleteWebSocket()) {
               dispose();
            }
         }


         @Override
         public void onBinaryMessage(final byte[] message) {
            getListener().onError(WebSocket_Android.this, "Binary messages not yet supported");
         }
      };

      try {
         _connection.connect(getURL().getPath(), handler);
      }
      catch (final WebSocketException e) {
         getListener().onError(this, e.getLocalizedMessage());
         if (getAutodeleteWebSocket()) {
            dispose();
         }
      }

   }


   @Override
   public void send(final String message) {
      _connection.sendTextMessage(message);
   }


   @Override
   public void close() {
      _connection.disconnect();
      _connection = null;
   }


   @Override
   public String toString() {
      return "WebSocket_Android [connection=" + _connection + "]";
   }


}
