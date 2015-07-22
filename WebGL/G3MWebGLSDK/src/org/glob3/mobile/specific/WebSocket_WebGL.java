

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.JavaScriptObject;


public class WebSocket_WebGL
         extends
            IWebSocket {

   private JavaScriptObject _socket;


   WebSocket_WebGL(final URL url,
                   final IWebSocketListener listener,
                   final boolean autodeleteListener,
                   final boolean autodeleteWebSocket) {
      super(url, listener, autodeleteListener, autodeleteWebSocket);

      jsInitialize(this, getURL()._path, getListener(), getAutodeleteWebSocket());
   }


   private native void jsInitialize(final WebSocket_WebGL webSocket,
                                    final String path,
                                    final IWebSocketListener listener,
                                    final boolean autodeleteWebSocket) /*-{
		if (!$wnd.WebSocket) {
			$wnd.WebSocket = $wnd.MozWebSocket;
		}

		if ($wnd.WebSocket) {
			var socket = new WebSocket(path);

			socket.onopen = function(event) {
				listener.@org.glob3.mobile.generated.IWebSocketListener::onOpen(Lorg/glob3/mobile/generated/IWebSocket;)(webSocket);
			}
			socket.onclose = function(event) {
				listener.@org.glob3.mobile.generated.IWebSocketListener::onClose(Lorg/glob3/mobile/generated/IWebSocket;)(webSocket);
			}
			socket.onerror = function(event) {
				listener.@org.glob3.mobile.generated.IWebSocketListener::onError(Lorg/glob3/mobile/generated/IWebSocket;Ljava/lang/String;)(webSocket, "" + event);
			}
			socket.onmessage = function(event) {
				if (typeof event.data === "string") {
					listener.@org.glob3.mobile.generated.IWebSocketListener::onMesssage(Lorg/glob3/mobile/generated/IWebSocket;Ljava/lang/String;)(webSocket, event.data);
				}
				else {
					listener.@org.glob3.mobile.generated.IWebSocketListener::onError(Lorg/glob3/mobile/generated/IWebSocket;Ljava/lang/String;)(webSocket, "Unsupported message type.");
				}
			}

			this.@org.glob3.mobile.specific.WebSocket_WebGL::_socket = socket;
		}
		else {
			listener.@org.glob3.mobile.generated.IWebSocketListener::onError(Lorg/glob3/mobile/generated/IWebSocket;Ljava/lang/String;)(webSocket, "Your browser does not support WebSocket.");
			if (autodeleteWebSocket) {
				webSocket.@org.glob3.mobile.specific.WebSocket_WebGL::dispose();
			}
		}
   }-*/;


   @Override
   public native void send(final String message) /*-{
		this.@org.glob3.mobile.specific.WebSocket_WebGL::_socket.send(message);
   }-*/;


   @Override
   public native void close() /*-{
		this.@org.glob3.mobile.specific.WebSocket_WebGL::_socket.close();
   }-*/;


}
