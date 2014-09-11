

package org.glob3.mobile.specific;

import java.net.URI;
import java.net.URISyntaxException;

import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


public class WebSocket_Android
         extends
            IWebSocket {

   //   static {
   //      System.setProperty("java.net.preferIPv6Addresses", "false");
   //      System.setProperty("java.net.preferIPv4Stack", "true");
   //   }


   //   private WebSocketConnection _connection;
   private WebSocketClient _webSocketClient;


   WebSocket_Android(final URL url,
                     final IWebSocketListener listener,
                     final boolean autodeleteListener,
                     final boolean autodeleteWebSocket) {
      super(url, listener, autodeleteListener, autodeleteWebSocket);

      //      _connection = new WebSocketConnection();


      try {
         final URI uri = new URI(url._path);
         _webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(final ServerHandshake handshakedata) {
               try {
                  getListener().onOpen(WebSocket_Android.this);
               }
               catch (final Exception e) {
                  e.printStackTrace();
               }
            }


            @Override
            public void onMessage(final String message) {
               try {
                  getListener().onMesssage(WebSocket_Android.this, message);
               }
               catch (final Exception e) {
                  e.printStackTrace();
               }
            }


            @Override
            public void onClose(final int code,
                                final String reason,
                                final boolean remote) {
               try {
                  getListener().onClose(WebSocket_Android.this);
               }
               catch (final Exception e) {
                  e.printStackTrace();
               }
               if (getAutodeleteWebSocket()) {
                  dispose();
               }
            }


            @Override
            public void onError(final Exception e) {
               try {
                  getListener().onError(WebSocket_Android.this, e.getLocalizedMessage());
               }
               catch (final Exception e2) {
                  e2.printStackTrace();
               }
               if (getAutodeleteWebSocket()) {
                  dispose();
               }
            }
         };

         _webSocketClient.connect();
      }
      catch (final URISyntaxException e1) {
         try {
            getListener().onError(WebSocket_Android.this, e1.getLocalizedMessage());
         }
         catch (final Exception e) {
            e.printStackTrace();
         }
         if (getAutodeleteWebSocket()) {
            dispose();
         }
      }

      //      final ConnectionHandler handler = new ConnectionHandler() {
      //         @Override
      //         public void onOpen() {
      //            getListener().onOpen(WebSocket_Android.this);
      //         }
      //
      //
      //         @Override
      //         public void onTextMessage(final String message) {
      //            getListener().onMesssage(WebSocket_Android.this, message);
      //         }
      //
      //
      //         @Override
      //         public void onRawTextMessage(final byte[] message) {
      //            getListener().onError(WebSocket_Android.this, "Raw Text messages not yet supported");
      //         }
      //
      //
      //         @Override
      //         public void onClose(final int code,
      //                             final String reason) {
      //            getListener().onClose(WebSocket_Android.this);
      //            if (getAutodeleteWebSocket()) {
      //               dispose();
      //            }
      //         }
      //
      //
      //         @Override
      //         public void onBinaryMessage(final byte[] message) {
      //            getListener().onError(WebSocket_Android.this, "Binary messages not yet supported");
      //         }
      //      };
      //
      //      try {
      //         _connection.connect(getURL().getPath(), handler);
      //      }
      //      catch (final WebSocketException e) {
      //         getListener().onError(this, e.getLocalizedMessage());
      //         if (getAutodeleteWebSocket()) {
      //            dispose();
      //         }
      //      }

   }


   @Override
   public void send(final String message) {
      //      _connection.sendTextMessage(message);
      _webSocketClient.send(message);
   }


   @Override
   public void close() {
      _webSocketClient.close();
      _webSocketClient = null;
      //      _connection.disconnect();
      //      _connection = null;
   }


   @Override
   public String toString() {
      //      return "WebSocket_Android [connection=" + _connection + "]";
      return "WebSocket_Android [webSocketClient=" + _webSocketClient + "]";
   }


}
