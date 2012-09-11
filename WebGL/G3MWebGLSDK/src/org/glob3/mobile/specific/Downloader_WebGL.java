

package org.glob3.mobile.specific;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.URL;

import com.google.gwt.user.client.Timer;


public class Downloader_WebGL
         implements
            IDownloader {

   final private int                                _maxConcurrentOperationCount;
   private long                                     _requestIdCounter;
   private long                                     _requestsCounter;
   private final long                               _cancelsCounter;
   private final Map<URL, Downloader_WebGL_Handler> _downloadingHandlers;
   private final Map<URL, Downloader_WebGL_Handler> _queuedHandlers;


   public Downloader_WebGL(final int maxConcurrentOperationCount) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIdCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      _downloadingHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _queuedHandlers = new HashMap<URL, Downloader_WebGL_Handler>();

      //      jsDefineURLObject();
   }


   @Override
   public void start() {
      sendRequest();
   }


   @Override
   public void stop() {
      // TODO Auto-generated method stub

   }


   @Override
   public long request(final URL url,
                       final long priority,
                       final IDownloadListener listener,
                       final boolean deleteListener) {

      final long requestId;
      Downloader_WebGL_Handler handler = null;

      _requestsCounter++;
      requestId = _requestIdCounter++;
      handler = _downloadingHandlers.get(url);

      if (handler != null) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, priority, requestId);
      }
      else {
         handler = _queuedHandlers.get(url);
         if (handler != null) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            // new handler, queue it
            handler = new Downloader_WebGL_Handler(url, listener, priority, requestId);
            _queuedHandlers.put(url, handler);
         }
      }

      return requestId;
      //      jsRequest(url.getPath());
   }


   @Override
   public void cancelRequest(final long requestId) {
      // TODO Auto-generated method stub

   }


   @Override
   public String statistics() {
      final StringBuilder_WebGL sb = new StringBuilder_WebGL();

      sb.add("Downloader_WebGL(downloading=").add(_downloadingHandlers.size());
      sb.add(", queued=").add(_queuedHandlers.size());
      sb.add(", totalRequests=").add(_requestsCounter);
      sb.add(", totalCancels=").add(_cancelsCounter);

      return sb.getString();
   }


   public void removeDownloadingHandlerForUrl(final URL url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_WebGL_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_WebGL_Handler selectedHandler = null;
      URL selectedURL = null;

      final Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> it = _queuedHandlers.entrySet().iterator();

      while (it.hasNext()) {
         final Map.Entry<URL, Downloader_WebGL_Handler> e = it.next();
         final URL url = e.getKey();
         final Downloader_WebGL_Handler handler = e.getValue();
         final long priority = handler.getPriority();

         if (priority > selectedPriority) {
            selectedPriority = priority;
            selectedHandler = handler;
            selectedURL = url;
         }
      }

      if (selectedHandler != null) {
         // move the selected handler to _downloadingHandlers collection
         _queuedHandlers.remove(selectedURL);
         _downloadingHandlers.put(selectedURL, selectedHandler);
      }

      return selectedHandler;
   }


   private void sendRequest() {
      final Downloader_WebGL thisDownloader = this;

      final Timer timer = new Timer() {
         @Override
         public void run() {
            if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
               final Downloader_WebGL_Handler handler = getHandlerToRun();

               if (handler != null) {
                  handler.runWithDownloader(thisDownloader);
                  sendRequest();
               }
            }

         }
      };

      timer.schedule(100);
   }


   //   private native static void jsRequest(String url) /*-{
   //		//                debugger;
   //
   //		//		function processResponse(xhr) {
   //		//			debugger;
   //		//			if (xhr.readyState == 4) {
   //		//				if (xhr.status == 200) {
   //		//
   //		//					var blob = xhr.response;
   //		//
   //		//					var img = @org.glob3.mobile.specific.Downloader_WebGL::jsCreateImage()();
   //		//					img.src = $wnd.urlUtil.createObjectURL(blob);
   //		//
   //		//				} else {
   //		//					alert("Error Retriving Data!");
   //		//				}
   //		//			}
   //		//		}
   //
   //		var xhReq = new XMLHttpRequest();
   //		var buf = new ArrayBuffer();
   //		xhReq.open("GET", url, true);
   //		xhReq.responseType = "arraybuffer";
   //		//		xhReq.responseType = "blob";
   //		//		if (xhReq.overrideMimeType) {
   //		//			xhReq.overrideMimeType("text/plain; charset=x-user-defined");
   //		//		} else {
   //		//			xhReq.setRequestHeader("Accept-Charset', 'x-user-defined");
   //		//		}
   //		xhReq.setRequestHeader("Cache-Control", "max-age=31536000");
   //		// onreadystate throws exception on firefox
   //		//		xhReq.onreadystatechange = function() {
   //		//			if (xhReq.readyState == 4) {
   //		//				@org.glob3.mobile.specific.Downloader_WebGL::jsProcessResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(xhReq);
   //		//			}
   //		//		};
   //		xhReq.onload = function() {
   //			if (xhReq.readyState == 4) {
   //				@org.glob3.mobile.specific.Downloader_WebGL::jsProcessResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(xhReq);
   //			}
   //		};
   //		console.log("sending ajax request");
   //		xhReq.send(buf);
   //   }-*/;
   //
   //
   //   private static native void jsProcessResponse(JavaScriptObject xhr) /*-{
   //		//            debugger;
   //		console.log("processing response");
   //		// TODO trigger events
   //		if (xhr.status == 200) {
   //			var bytes = new Uint8Array(xhr.response);
   //			for ( var i = 0; i < bytes.length; i++) {
   //				bytes[i] = 0xFF;
   //			}
   //
   //		} else {
   //			alert("Error Retriving Data!");
   //		}
   //   }-*/;


   //   private native static void jsDefineURLObject() /*-{
   //		$wnd.urlUtil = ($wnd.webkitURL != "undefined") ? $wnd.webkitURL
   //				: $wnd.URL;
   //   }-*/;


   //   private native static JavaScriptObject jsCreateImage() /*-{
   //		debugger;
   //		var img = $doc.createElement('img');
   //		img.onload = function(e) {
   //			console.log("img load");
   //			$wnd.urlUtil.revokeObjectURL(img.src);
   //			$doc.body.appendChild(img);
   //		};
   //
   //		return img;
   //   }-*/;
}
